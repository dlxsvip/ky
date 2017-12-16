package com.ky.logic.controller;

import com.ky.logic.common.cache.SystemCache;
import com.ky.logic.entity.UserEntity;
import com.ky.logic.entity.UserLoginCtrlEntity;
import com.ky.logic.model.info.ResponseMsg;
import com.ky.logic.service.IUserService;
import com.ky.logic.utils.AesUtil;
import com.ky.logic.utils.BCryptUtil;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.ScriptUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * @author yyl
 * @create 2017-07-04 13:11
 * @since 1.0
 **/
@Controller
@RequestMapping("/login")
public class LoginController {


    @Resource(name = "userService")
    private IUserService userService;


    @RequestMapping(value = {"/loginSuccess"})
    @ResponseBody
    public ResponseMsg loginSuccess(HttpServletRequest request, HttpServletResponse response) {
        ResponseMsg responseMsg = new ResponseMsg();

        try {
            Map<String, String[]> tmp = request.getParameterMap();
            UserEntity user = userService.queryByName(request.getParameter("username"));
            Integer sessionTimeout = SystemCache.getInstance().getInteger("login.session.timeout");

            // session 超时时间 单位 秒
            request.getSession().setMaxInactiveInterval(sessionTimeout);
            request.getSession().setAttribute("currentUser", user.getLoginName());

            UserLoginCtrlEntity userLoginCtrl = user.getUserLoginCtrl();
            if (userLoginCtrl.getIsNeedToLoginAas()) {
                responseMsg.createFailedResponse(null, "701", "初次登录需要修改初密码");
                return responseMsg;
            }

            //判断当前用户是否处于激活状态
            if (!user.getActive()) {
                responseMsg.createFailedResponse(null, "702", "该用户未激活");
                return responseMsg;
            }

            // 校验是否禁闭
            long n = userService.checkForbidTime(userLoginCtrl);
            if (n > 0) {
                // n 单位秒，格式化到 分
                n = n / (1000 * 60);

                responseMsg.createFailedResponse(null, "703", n + "");
                return responseMsg;
            }

            // 校验密码是否快到期
            boolean b = userService.isPwdExpire(userLoginCtrl);
            if (b) {
                responseMsg.createFailedResponse(null, "704", "密码即将到期，请修改");
                return responseMsg;
            }


            // 消除异常状态
            userLoginCtrl.setConsecutiveAuthFailuresCount(0);
            userLoginCtrl.setLastAttemptLoginTime(null);

            // 更新登陆时间
            user.setLoginTime(new Date());

            userService.modifyUser(user);
            responseMsg.createSuccessResponse("success");
        } catch (Exception e) {
            e.printStackTrace();
            responseMsg.createFailedResponse(null, "用户登录处理异常", "请联系管理员");
        }
        return responseMsg;
    }


    @RequestMapping(value = {"/loginFailed"})
    @ResponseBody
    public ResponseMsg loginFailed(HttpServletRequest request, HttpServletResponse response) {
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            String username = request.getParameter("username");

            // 校验用户名
            UserEntity user = userService.queryByName(username);
            if (null == user) {
                responseMsg.createFailedResponse(null, "登录名或密码错误", "1");
                return responseMsg;
            }

            // 校验密码
            String password = request.getParameter("password");
            // 解密密码
            password = AesUtil.INSTANCE.decrypt(password);
            Boolean isSuccess = BCryptUtil.checkPassword(password, user.getPassword());
            if (!isSuccess){
                // 记录失败次数
                UserLoginCtrlEntity loginCtrl = user.getUserLoginCtrl();
                Integer failNum = loginCtrl.getConsecutiveAuthFailuresCount();

                // 超过允许的最大失败次数，添加禁闭时间
                if (failNum >= SystemCache.getInstance().getInteger("login.fail.times")) {

                    long cur = System.currentTimeMillis();
                    Integer n = SystemCache.getInstance().getInteger("login.forbid.time");
                    long forbid = 1000 * 60 * n;

                    long forbidTime = cur + forbid;
                    loginCtrl.setLastAttemptLoginTime(new Timestamp(forbidTime));
                }

                // 失败次数
                loginCtrl.setConsecutiveAuthFailuresCount(++failNum);

                // 更新数据
                userService.modifyUser(user);

                responseMsg.createFailedResponse(null, "登录名或密码错误", "2");
                return responseMsg;
            }else{
                LoggerUtil.debugSysLog("","","用户名和密码正确，请检查其他项");
            }
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "登录名或密码错误", "");
        }

        return responseMsg;
    }



    @RequestMapping(value = "/getKey", method = RequestMethod.GET)
    @ResponseBody
    public String getKey(HttpServletRequest request, HttpServletResponse response) {
        try {
            String aesKey = AesUtil.INSTANCE.getKey();
            // 对密钥 进行js加密
            String rs = ScriptUtil.executeJsMethod("compileStr", aesKey);
            return rs;
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), "getKey", "获取key异常" + e.getMessage());
        }

        return "";
    }
}

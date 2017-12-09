package com.ky.logic.controller;

import com.ky.logic.entity.UserEntity;
import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.entity.UserRoleEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.model.info.ResponseInfo;
import com.ky.logic.model.request.UserRequest;
import com.ky.logic.model.response.UserResponse;
import com.ky.logic.service.IUserService;
import com.ky.logic.utils.AesUtil;
import com.ky.logic.utils.BCryptUtil;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.PrivilegeUtil;
import com.ky.logic.utils.export.TxtUtil;
import com.ky.logic.utils.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 用户管理
 *
 * @author yyl
 * @create 2017-07-04 13:12
 * @since 1.0
 **/
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource(name = "userService")
    private IUserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo create(@RequestBody UserRequest user) {
        ResponseInfo responseInfo = new ResponseInfo();
        String userId = null;
        try {
            if (!PrivilegeUtil.HasUserConfig()) {
                responseInfo.createFailedResponse(null, "没有用户管理权限", "");
                return responseInfo;
            }

            UserEntity userEntity = userService.queryByName(user.getLoginName());
            if (null != userEntity) {
                responseInfo.createFailedResponse(null, "存在相同登录名的用户", "添加失败");
                return responseInfo;
            }

            userId = userService.createUser(user);
            responseInfo.createSuccessResponse(userId);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "添加失败", e.getMessage());
        }

        return responseInfo;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo update(@RequestBody UserRequest user) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            String userId = userService.updateUser(user);
            responseInfo.createSuccessResponse(userId);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "修改失败", e.getMessage());
        }

        return responseInfo;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo delete(@RequestParam String userId) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            if (!PrivilegeUtil.HasUserConfig()) {
                responseInfo.createFailedResponse(null, "没有用户管理权限", "");
                return responseInfo;
            }

            userService.deleteUser(userId);
            responseInfo.createSuccessResponse(userId);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "删除失败", e.getMessage());
        }

        return responseInfo;
    }

    @RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
    @ResponseBody
    public ResponseInfo queryByPage(@RequestParam(required = false) String userId,
                                    @RequestParam(required = false) String loginName,
                                    @RequestParam(required = false) String realName,
                                    @RequestParam(required = false) String cellphoneNum,
                                    @RequestParam(required = false) String telephoneNum,
                                    @RequestParam(required = false) String email,
                                    @RequestParam(required = false) String orderBy,
                                    @RequestParam(required = false) Boolean orderAsc,
                                    @RequestParam(required = false) Integer pageNum,
                                    @RequestParam(required = false) Integer pageSize) {

        ResponseInfo responseInfo = new ResponseInfo();
        try {
            UserRequest request = new UserRequest();
            request.setUserId(userId);
            request.setLoginName(loginName);
            request.setRealName(realName);
            request.setCellphoneNum(cellphoneNum);
            request.setTelephoneNum(telephoneNum);
            request.setEmail(email);
            request.setOrderBy(orderBy);
            request.setOrderAsc(orderAsc);
            request.setPageNum(pageNum);
            request.setPageSize(pageSize);

            if (!PrivilegeUtil.HasUserConfig()) {
                responseInfo.createFailedResponse(null, "没有用户管理权限", "");
                return responseInfo;
            }

            UserEntity curLoginUser = userService.getCurLoginUser();
            if (StringUtils.equals("super", curLoginUser.getLoginName())) {
                request.setIsSuper(true);
            } else {
                request.setIsSuper(false);
            }

            Page<UserResponse> page = userService.queryByPage(request);

            responseInfo.createSuccessResponse(page);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "查询用户信息列表异常", e.getMessage());
        }

        return responseInfo;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public ResponseInfo query(@RequestParam String userId) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            UserResponse response = userService.queryInfo(userId);
            responseInfo.createSuccessResponse(response);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "查询用户详情异常", e.getMessage());
        }
        return responseInfo;
    }


    @RequestMapping(value = "/exportSystemInitPasswords", method = RequestMethod.GET)
    @ResponseBody
    public void exportSystemInitPasswords(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] userIds) {
        try {
            /*String[] arr = userIds.split(",");

            if (arr.length < 0) {
                return;
            }*/

            // arr 2 list
            //List<String> ids = Arrays.asList(arr);

            List<UserEntity> lists = userService.getAllUserSystemInitPassword(userIds);

            List<List<String>> data = new ArrayList<>();
            List<String> title = new ArrayList<>();
            title.add("序号");
            title.add("用户名");
            title.add("显示名");
            title.add("密码");
            data.add(title);

            List<String> item;
            for (int i = 0; i < lists.size(); i++) {
                UserEntity user = lists.get(i);
                item = new ArrayList<>();
                item.add(String.valueOf(i + 1));
                item.add(user.getLoginName());
                item.add(user.getNickName());
                item.add(user.getUserLoginCtrl().getSystemInitPassword());
                data.add(item);
            }

            TxtUtil.INSTANCE.export2httpTxt(response, "UserInitPassword", data);
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), "exportSystemInitPasswords", e.getMessage());
        }
    }

    /**
     * 校验当前登录用户的密码
     *
     * @param currentPassword 当前密码
     * @return 是否成功
     */
    @RequestMapping(value = "/checkPassword", method = RequestMethod.GET)
    @ResponseBody
    public ResponseInfo checkPassword(@RequestParam String currentPassword) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            UserEntity curLoginUser = userService.getCurLoginUser();
            if (null == curLoginUser) {
                responseInfo.createFailedResponse(null, "用户不存在", "请检查");
                return responseInfo;
            }

            currentPassword = AesUtil.INSTANCE.decrypt(currentPassword);
            if (StringUtils.isEmpty(currentPassword)) {
                responseInfo.createFailedResponse(null, "当前密码不能为空", "请检查");
                return responseInfo;
            }

            Boolean b = BCryptUtil.checkPassword(currentPassword, curLoginUser.getPassword());
            if (b) {
                responseInfo.createSuccessResponse("success");
            } else {
                responseInfo.createFailedResponse(null, "密码校验失败", "currentPassword");
            }
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "密码校验异常", e.getMessage());
        }

        return responseInfo;
    }


    /**
     * 重置密码
     *
     * @param map 参数
     * @return 结果
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo resetPassword(@RequestBody Map<String, String> map) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            String userId = map.get("userId");
            String newPassword = map.get("newPassword");

            UserEntity user = userService.queryByUserId(userId);
            if (null == user) {
                responseInfo.createFailedResponse(null, "用户不存在", "请检查id:" + userId);
                return responseInfo;
            }

            if (!PrivilegeUtil.HasUserConfig()) {
                responseInfo.createFailedResponse(null, "没有用户管理权限", "");
                return responseInfo;
            }

            newPassword = AesUtil.INSTANCE.decrypt(newPassword);
            // 前台传"",不知道为什么，有一定几率解出 undefined
            if (StringUtils.equals("undefined", newPassword)) {
                newPassword = "";
            }

            // 管理员 重置密码
            String msg = userService.resetPassword(user, newPassword);

            responseInfo.createSuccessResponse(msg);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "修改失败", e.getMessage());
        }

        return responseInfo;
    }

    /**
     * 修改密码
     *
     * @param map 参数
     * @return 结果
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo updatePassword(@RequestBody Map<String, String> map) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            String oldPassword = map.get("oldPassword");
            String newPassword = map.get("newPassword");

            oldPassword = AesUtil.INSTANCE.decrypt(oldPassword);
            if (StringUtils.isEmpty(oldPassword)) {
                responseInfo.createFailedResponse(null, "当前密码不能为空", "请检查");
                return responseInfo;
            }

            newPassword = AesUtil.INSTANCE.decrypt(newPassword);
            if (StringUtils.isEmpty(newPassword)) {
                responseInfo.createFailedResponse(null, "新密码不能为空", "请检查");
                return responseInfo;
            }

            UserEntity user = userService.getCurLoginUser();
            if (null == user) {
                responseInfo.createFailedResponse(null, "用户不存在", "请检查");
                return responseInfo;
            }

            // 普通用户 校验
            Boolean b = BCryptUtil.checkPassword(oldPassword, user.getPassword());
            if (b) {
                String msg = userService.updatePassword(user, newPassword);
                responseInfo.createSuccessResponse(msg);
            } else {
                responseInfo.createFailedResponse(null, "当前密码校验失败", "当前密码校验失败");
            }
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "修改失败", e.getMessage());
        }

        return responseInfo;
    }


    @RequestMapping(value = "/getConcurrentUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getConcurrentUserInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> userMap = new HashMap<String, Object>();
        try {
            UserEntity user = userService.getCurLoginUser();
            if (null == user) {
                return userMap;
            }

            List<String> roles = new ArrayList<>();
            Set<UserRoleEntity> roleEntities = user.getRole();
            for (UserRoleEntity en : roleEntities) {
                roles.add(en.getRoleName());
            }

            List<String> privileges = new ArrayList<>();
            Set<UserPrivilegeEntity> privilegeEntities = userService.getPrivilegesByUserId(user.getId());
            for (UserPrivilegeEntity en : privilegeEntities) {
                privileges.add(en.getPrivilege());
            }

            userMap.put("userId", user.getId());
            userMap.put("loginName", user.getLoginName());
            userMap.put("nickName", user.getNickName());
            userMap.put("email", user.getEmail());
            userMap.put("description", user.getDescription());
            userMap.put("roles", roles);
            userMap.put("privileges", privileges);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userMap;
    }

    @RequestMapping(value = "/queryUsersByRoleId", method = RequestMethod.GET)
    @ResponseBody
    public ResponseInfo queryUsersByRoleId(@RequestParam(required = false) String roleId,
                                           @RequestParam(required = false) String orderBy,
                                           @RequestParam(required = false) Boolean orderAsc,
                                           @RequestParam(required = false) Integer pageNum,
                                           @RequestParam(required = false) Integer pageSize) {

        ResponseInfo responseInfo = new ResponseInfo();
        try {
            Paging paging = new Paging();
            paging.setOrderBy(orderBy);
            paging.setOrderAsc(orderAsc);
            paging.setPageNum(pageNum);
            paging.setPageSize(pageSize);

            Page<UserResponse> page = userService.queryUsersByRoleId(roleId, paging);

            responseInfo.createSuccessResponse(page);
        } catch (Exception e) {
            e.printStackTrace();
            responseInfo.createFailedResponse(null, "根据角色查询用户异常", e.getMessage());
        }

        return responseInfo;
    }


    /**
     * 解除某个用户的禁闭时间
     *
     * @param userId 用户id
     * @return 结果
     */
    @RequestMapping(value = "/closeForbidTime", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo closeForbidTime(@RequestParam String userId) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            if (!PrivilegeUtil.HasUserConfig()) {
                responseInfo.createFailedResponse(null, "没有用户管理权限", "");
                return responseInfo;
            }


            userService.closeForbidTime(userId);
            responseInfo.createSuccessResponse(userId);
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "删除失败", e.getMessage());
        }

        return responseInfo;
    }
}

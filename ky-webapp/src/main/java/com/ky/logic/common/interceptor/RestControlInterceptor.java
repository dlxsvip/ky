package com.ky.logic.common.interceptor;

import com.ky.logic.common.cache.SystemCache;
import com.ky.logic.utils.LoggerUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yl on 2017/7/16.
 * 一种是实现HandlerInterceptor接口
 * 一种是继承HandlerInterceptorAdapter类
 * 可以拦截@RequestMapping注解的类和方法
 */
public class RestControlInterceptor implements HandlerInterceptor {


    /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false
     * 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * 如果返回true
     * 执行下一个拦截器,直到所有的拦截器都执行完毕
     * 再执行被拦截的Controller
     * 然后进入拦截器链,
     * 从最后一个拦截器往回执行所有的postHandle()
     * 接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        try {
            if (SystemCache.getInstance().containsKey("test.no.login.check")) {
                Boolean noLoginCheck = SystemCache.getInstance().getBoolean("test.no.login.check");
                if (noLoginCheck) {
                    return true;
                }
            }
        } catch (Exception e) {
            LoggerUtil.errorSysLog("校验", "login no check 配置错误", e.getMessage());
        }

        String requestString = request.getRequestURL().toString();
//        if (requestString.contains("/control")) {
//            String sessionId = request.getSession().getId();
//            // 校验登录用户
//            if (SessionUserCache.checkSession(sessionId)) {
//                return true;
//            } else {
//                String requestType = request.getHeader("X-Requested-With");
//                if (!org.apache.commons.lang3.StringUtils.isEmpty(requestType) && requestType.equalsIgnoreCase("XMLHttpRequest")) {
//                    response.setHeader("sessionstatus", "timeout");
//                    response.sendError(401, "session timeout.");
//
//                    //
//                    SessionUserCache.removeUserBySessionId(sessionId);
//                    return false;
//                } else {
//                    throw new SessionTimeoutException();
//                }
//            }
//        }

        return true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


}

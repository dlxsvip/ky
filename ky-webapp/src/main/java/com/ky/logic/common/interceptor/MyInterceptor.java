package com.ky.logic.common.interceptor;

import com.ky.logic.common.cache.SystemCache;
import com.ky.logic.common.exception.SessionTimeoutException;
import com.ky.logic.utils.LoggerUtil;
import com.ky.pm.utils.AKUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yl on 2017/8/4.
 * 一种是实现HandlerInterceptor接口
 * 一种是继承HandlerInterceptorAdapter类
 * 可以拦截@RequestMapping注解的类和方法
 */
public class MyInterceptor implements HandlerInterceptor {

    ThreadLocal<StopWatch> stopWatchLocal = new ThreadLocal<>();


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
        log(request, o);

        if (!control(request, response)) {
            return false;
        }

        if (!api(request)) {
            return false;
        }

        return true;
    }


    // 所有路径 日志记录
    private void log(HttpServletRequest request, Object o) {
        StopWatch stopWatch = new StopWatch(o.toString());
        stopWatch.start(o.toString());
        stopWatchLocal.set(stopWatch);

        String host = request.getRemoteHost();
        String url = request.getRequestURI();
        System.out.print("IP:" + host + " 访问" + url);
        LoggerUtil.debugSysLog("IP:" + host, "访问", url);
    }


    //  /control/** 路径 session 校验
    private boolean control(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        if (requestString.contains("/control")) {

            if(requestString.contains("login")){
                return true;
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            if (principal instanceof String && principal.equals("anonymousUser")) {
                String requestType = request.getHeader("X-Requested-With");
                if (!StringUtils.isEmpty(requestType) && requestType.equalsIgnoreCase("XMLHttpRequest")) {
                    response.setHeader("sessionstatus", "timeout");
                    response.sendError(401, "session timeout.");

                    return false;
                } else {
                    throw new SessionTimeoutException();
                }
            }else{
                return true;
            }

        }

        return true;
    }


    //  /api/** 路径 AK 校验
    private boolean api(HttpServletRequest request) {
        String requestString = request.getRequestURL().toString();
        if (requestString.contains("/api")) {
            try {
                // 校验访问的ak
                String signature = request.getHeader("Signature");
                String accessKeyId = request.getHeader("AccessKeyId");

                String accessKeySecret = "";
                String signatureMethod = "HMAC-SHA256";
                String version = "20160701";

                String method = request.getMethod();

                String newSignature = AKUtil.SignAK(method, accessKeyId, accessKeySecret, signatureMethod, version);
                if (StringUtils.equals(signature, newSignature)) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }


        return true;
    }


    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * 可在modelAndView中加入数据，比如当前时间
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     * <p/>
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        StopWatch stopWatch = stopWatchLocal.get();
        stopWatch.stop();
        stopWatchLocal.set(null);
        System.out.println("  耗时间：" + stopWatch.getTotalTimeSeconds() + " 秒");
    }
}

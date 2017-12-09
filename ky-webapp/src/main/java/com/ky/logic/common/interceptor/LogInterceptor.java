package com.ky.logic.common.interceptor;

import com.ky.logic.utils.LoggerUtil;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志拦截器
 * Created by yl on 2017/7/16.
 */
public class LogInterceptor implements HandlerInterceptor {

    ThreadLocal<StopWatch> stopWatchLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        StopWatch stopWatch = new StopWatch(o.toString());
        stopWatch.start(o.toString());
        stopWatchLocal.set(stopWatch);

        String host = request.getRemoteHost();
        String url = request.getRequestURI();
        System.out.print("IP:" + host + " 访问" + url);
        LoggerUtil.debugSysLog("IP:" + host, "访问", url);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        StopWatch stopWatch = stopWatchLocal.get();
        stopWatch.stop();
        stopWatchLocal.set(null);
        System.out.println("  耗时间：" + stopWatch.getTotalTimeSeconds() + " 秒");
    }
}

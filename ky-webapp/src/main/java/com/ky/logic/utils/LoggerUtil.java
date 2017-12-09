package com.ky.logic.utils;


import org.apache.log4j.Logger;

/**
 * Created by gaofeico on 2015/10/29.
 */
public class LoggerUtil {

    public static Logger logger = Logger.getLogger(LoggerUtil.class.getClass());


    public static void debugSysLog(String className, String methodName, String context){
        logger.debug("[" + className + "][" + methodName + "]:" + context);
    }

    public static void infoSysLog(String className, String methodName, String context){
        logger.info("[" + className + "][" + methodName + "]:" + context);
    }

    public static void errorSysLog(String className, String methodName, String context){
        logger.error("[" + className + "][" + methodName + "]:" + context);
    }

    public static void warnSysLog(String className, String methodName, String context){
        logger.warn("[" + className + "][" + methodName + "]:" + context);
    }

    public static void printStackTrace(String className, String methodName, Exception e) {
        logger.error("[" + className + "][" + methodName + "]:", e);
    }

}

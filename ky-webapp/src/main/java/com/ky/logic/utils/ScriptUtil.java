package com.ky.logic.utils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;

/**
 * Created by yl on 2017/8/12.
 */
public class ScriptUtil {
    private static final String JS_FILE = "/script/utils.js";

    public static String executeJsMethod(String method, String param) throws Exception {
        String rs = "";
        ScriptEngineManager manage = new ScriptEngineManager();
        ScriptEngine engine = manage.getEngineByName("js");

        try (InputStreamReader reader = new InputStreamReader(ScriptUtil.class.getResourceAsStream(JS_FILE))) {
            engine.eval(reader);

            if (engine instanceof Invocable) {
                Invocable invoke = (Invocable) engine;
                rs = (String) invoke.invokeFunction(method, param);
            }
        } catch (Exception e) {
            LoggerUtil.errorSysLog(ScriptUtil.class.getName(), "executeJsMethod", "执行js方法异常" + e.getMessage());
            throw e;
        }

        return rs;
    }


}

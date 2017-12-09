package com.ky.logic.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JacksonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    public static final ObjectMapper mapper = new ObjectMapper();

    public String obj2Json(Object obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }

    public static String obj2JsonBySafe(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("obj2JsonBySafe error, e : " + e.getMessage());
        }
        return null;
    }


    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     * (1)转换为普通JavaBean：readValue(json,Student.class)
     * (2)转换为List,如List<Student>,将第二个参数传递为Student
     * [].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List
     *
     * @param jsonStr
     * @param valueType
     * @return
     */
    public static <T> T json2bean(String jsonStr, Class<T> valueType) throws Exception {
        return mapper.readValue(jsonStr, valueType);
    }

    public <T> T json2beanBySafe(String json, Class<T> valueType) {
        try {
            return mapper.readValue(json, valueType);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("json2beanBySafe error : " + e.getMessage());
        }
        return null;
    }


    public static void sendResponse(HttpServletResponse response, String jsonContent) {
        if (null == response) {
            return;
        }

        PrintWriter pWriter = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            pWriter = response.getWriter();
            pWriter.print(jsonContent);
            pWriter.flush();
        } catch (IOException e) {
            logger.debug("[JacksonUtil][sendResponse]:Catch exception {}", e);
        } finally {
            if (null != pWriter) {
                pWriter.close();
            }
        }

    }

    public static void sendResponse(HttpServletResponse response, Object beanObject) {
        try {
            String jsonContent = mapper.writeValueAsString(beanObject);
            sendResponse(response, jsonContent);
        } catch (Exception e) {
            logger.debug("[JacksonUtil][sendResponse]:Catch exception {}", e);
        }
    }

    public static String getJsonStr(ObjectMapper mapper, ObjectNode map) {
        String resultStr = "";
        try {
            resultStr = mapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultStr;
    }


}

package com.ky.sdk.utils;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by yl on 2017/7/3.
 */
public enum JacksonUtil {

    INSTANCE;

    private static final ObjectMapper mapper = new ObjectMapper();

    public String obj2str(Object obj) {
        String result = "";
        try {
            result = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public <T> T json2bean(String json, Class<T> valueType) {
        try {
            return mapper.readValue(json, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

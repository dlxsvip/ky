package com.ky.logic.common.init;

import com.ky.logic.utils.JacksonUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yl on 2017/8/1.
 */
@Component
public class PrivilegeConfigs {

    private static final String PRIVILEGE_CONFIG = "/privilegeConfigure.json";

    private static List<Map<String, String>> privilegeMap = new ArrayList<>();

    @PostConstruct
    public void init() {
        readProperties();
    }


    @PreDestroy
    public void destroy() {
        clear();
    }

    public void readProperties() {
        InputStream inputFile = null;
        try {
            inputFile = PrivilegeConfigs.class.getResourceAsStream(PRIVILEGE_CONFIG);
            String jsonData = IOUtils.toString(inputFile, "UTF-8");
            privilegeMap = JacksonUtil.json2bean(jsonData,ArrayList.class);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(inputFile);
        }
    }

    public static void clear() {
        privilegeMap.clear();
    }

    public static List<Map<String, String>> getPrivilegeMap() {
        return privilegeMap;
    }

}

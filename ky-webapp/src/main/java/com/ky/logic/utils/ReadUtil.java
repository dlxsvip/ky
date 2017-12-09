package com.ky.logic.utils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 读工具<br>
 * Created by yyl on 2016/9/22.
 */
public class ReadUtil {

    public static final int BUFF_SIZE = 1024 * 10;

    /**
     * 读 Properties
     *
     * @param path    文件路径
     * @param charset 字符集
     * @return Map
     */
    public static Map<String, String> read2map(String path, String charset) throws Exception {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        Map<String, String> map = read2map(in, charset);
        closeInputStream(in);
        return map;
    }

    /**
     * 读取文件
     *
     * @param file 配置文件路径
     * @return map
     */
    public static Map<String, String> read2map(File file, String charset) throws Exception {
        if (!file.exists()) {
            return null;
        }

        return read2map(new FileInputStream(file), charset);
    }


    /**
     * 读文件流
     *
     * @param in      文件流
     * @param charset 字符集
     * @return Map
     */
    public static Map<String, String> read2map(InputStream in, String charset) throws Exception {
        return read2map(new InputStreamReader(in, charset));
    }

    /**
     * 读
     *
     * @return Map
     */
    public static Map<String, String> read2map(Reader r) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        // 读取配置文件
        Properties prop = new Properties();
        prop.load(r);

        //
        Enumeration en = prop.propertyNames();
        while (en.hasMoreElements()) {
            String strKey = (String) en.nextElement();
            String strValue = prop.getProperty(strKey);
            map.put(strKey, strValue);
        }

        closeReader(r);
        return map;
    }


    public static ConcurrentMap<String, Object> read2concurrentMap(String fileName, String charset) throws Exception {
        return read2concurrentMap(new InputStreamReader(getRootFileInputStream(fileName), charset));
    }


    /**
     * 读
     *
     * @return ConcurrentMap
     */
    public static ConcurrentMap<String, Object> read2concurrentMap(Reader r) throws Exception {
        ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();
        // 读取配置文件
        Properties prop = new Properties();
        prop.load(r);

        //
        Enumeration en = prop.propertyNames();
        while (en.hasMoreElements()) {
            String strKey = (String) en.nextElement();
            String strValue = prop.getProperty(strKey);
            map.put(strKey, strValue);
        }

        closeReader(r);
        return map;
    }


    /**
     * 流转字节
     *
     * @param in 文件流
     * @return 文件字节
     * @throws Exception
     */
    public static byte[] read2byte(InputStream in) throws Exception {
        byte[] b = null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[BUFF_SIZE];
            int len = 0;
            while ((len = in.read(buffer, 0, BUFF_SIZE)) != -1) {
                out.write(buffer, 0, len);
            }

            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    /**
     * 本地文件转字节
     *
     * @param localFile 本地文件
     * @return 文件字节
     */
    public static byte[] localFile2byte(File localFile) {
        try (InputStream in = new FileInputStream(localFile)) {
            return read2byte(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 本地文件转字节
     *
     * @param localFile 本地文件
     * @return 文件字节
     */
    public static byte[] localFile2byte(String localFile) {
        return localFile2byte(new File(localFile));
    }


    /**
     * 网络文件转字节
     *
     * @param urlFile 网络文件
     * @return 文件字节
     */
    public static byte[] urlFile2byte(String urlFile) {
        try (InputStream in = new URL(urlFile).openStream()) {
            return read2byte(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取跟目录下的 文件流
     *
     * @param filePath "/xx.txt" 已 / 开头
     * @return 文件流
     */
    public static InputStream getRootFileInputStream(String filePath) {
        return ReadUtil.class.getResourceAsStream(filePath);
    }

    /**
     * 获取类加载器目录下的 文件流
     *
     * @param filePath ""
     * @return 文件流
     */
    public static InputStream getFileInputStream(String filePath) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
    }

    /**
     * 读取自定义文件
     *
     * @param filePath
     * @param charset
     * @return map
     */
    public static Map<String, String> readFile(String filePath, String charset) throws Exception {
        Reader r = new InputStreamReader(getFileInputStream(filePath), charset);

        Map<String, String> map = new HashMap<String, String>();
        BufferedReader reader = new BufferedReader(r);
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.contains("=")) {
                String[] split = line.split("=");
                if (split.length == 1)
                    map.put(split[0], "");
                else
                    map.put(split[0], split[1]);
            }
        }

        closeBufferedReader(reader);

        return map;
    }


    /**
     * 在服务器端获取发送过来的内容
     *
     * @param request
     * @return
     */
    public static String receiveContent(HttpServletRequest request) throws Exception {
        byte[] b = new byte[BUFF_SIZE];
        String result = "";

        ServletInputStream sis = request.getInputStream();
        int line = sis.readLine(b, 0, b.length);
        while (line != -1) {
            result = result + new String(b, 0, line);
            line = sis.readLine(b, 0, b.length);
        }

        return result;
    }


    public static void closeBufferedReader(BufferedReader reader) {
        if (null != reader) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeReader(Reader reader) {
        if (null != reader) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeInputStream(InputStream in) {
        if (null != in) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.ky.logic.utils;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

/**
 * web 项目路径<br>
 * Created by yyl on 2016/10/19.
 */
public class WebPathUtil {

    public static String getRootPath() {
        String path = WebPathUtil.class.getResource(File.separator).getPath();
        try {
            path = URLDecoder.decode(path, "utf-8");
        } catch (Exception e) {
            path = WebPathUtil.class.getResource(File.separator).getPath();
            path = path.replaceAll("%20", " ");
            LoggerUtil.errorSysLog(WebPathUtil.class.getName(), "getRootPath", e.getMessage());
        }

        return path;
    }


    /**
     * 获取web项目 工程根目录路径
     *
     * @return web根目录路径
     */
    public static String getProjectDir() {
        String path = getRootPath();
        return path.substring(0, path.lastIndexOf("WEB-INF"));
    }

    /**
     * 获取web项目 WEB-INF目录路径
     *
     * @return WEB-INF目录路径
     */
    public static String getWEB_INFDir() {
        return getProjectDir() + "WEB-INF/";
    }


    /**
     * 获取web项目 view目录路径
     *
     * @return view目录路径
     */
    public static String getViewDir() {
        return getProjectDir() + "view" + File.separator;
    }

    /**
     * 获取 view 子目录路径
     *
     * @return 路径
     */
    public static String getViewSubDir(String dir) {
        String subdir = getViewDir() + dir + File.separator;
        mkDir(subdir);

        return subdir;
    }

    /**
     * 获取 view 子目录app目录路径
     *
     * @return app目录路径
     */
    public static String getAppDir() {
        return getViewSubDir("app");
    }

    /**
     * 获取 app 子目录路径
     *
     * @return 子目录dir路径
     */
    public static String getAppSubDir(String dir) {
        String subdir = getAppDir() + dir + File.separator;
        mkDir(subdir);

        return subdir;
    }

    /**
     * 获取 view 子目录data目录路径
     *
     * @return app目录路径
     */
    public static String getDataDir() {
        return getViewSubDir("data");
    }


    /**
     * 获取clazz类路径 项目路径
     *
     * @return clazz路径
     */
    public static String getClassPath(Class clazz) {
        String classPath = clazz.getResource("").getPath();
        classPath = classPath.substring(1);

        return classPath;
    }


    /**
     * 获取类根路径
     *
     * @return class目录路径
     */
    public static String getClassRootPath() {
        String classPath = WebPathUtil.class.getClassLoader().getResource("/").getPath();
        if (null == classPath) {
            classPath = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
        }
        classPath = classPath.substring(1);

        return classPath;
    }


    /**
     * 获取文件URL
     *
     * @param file 文件
     * @return 路径
     */
    private static URL getURL(String file) {
        return WebPathUtil.class.getClassLoader().getResource(file);
    }

    /**
     * 获取文件路径
     *
     * @param file 文件
     * @return 路径
     */
    public static String getFilePath(String file) {
        if (0 == file.indexOf("/")) {
            return WebPathUtil.class.getResource(file).getPath().substring(1);
        } else {
            return getURL(file).getPath().substring(1);
        }
    }

    /**
     * 获取文件路径
     *
     * @param file 文件
     * @return 路径
     */
    public static String getFilePath2(String file) {
        if (0 == file.indexOf("/")) {
            return WebPathUtil.class.getResource(file).getPath().substring(1);
        } else {
            return getURL(file).getFile().substring(1);
        }
    }


    /**
     * 获取父级路径
     *
     * @return 父级路径
     */
    public static String getParentPath(String path) {
        return new File(path).getParent();
    }


    /**
     * 创建目录结构
     *
     * @param dirPath
     */
    public static void mkDir(String dirPath) {
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }






}

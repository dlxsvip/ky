package com.ky.logic.utils;

import org.apache.commons.lang3.StringUtils;

import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by yl on 2017/8/31.
 */
public class FileUtil {

    /**
     * 获取本地文件名称
     *
     * @param filePath 本地文件全路径
     * @return 文件名称
     */
    public static String getLocalFileName(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "";
        }

        return file.getName();
    }

    /**
     * 文件名称获取
     * 字符串截取文件名
     *
     * @param file 文件全路径
     * @return 文件名
     */
    public static String getFileName(String file) {
        String fileName = file;
        try {
            if (null != file && file.length() > 0) {
                if (file.contains(File.separator)) {
                    fileName = file.substring(file.lastIndexOf(File.separator) + 1);
                } else if (file.contains("/")) {
                    fileName = file.substring(file.lastIndexOf("/") + 1);
                } else if (file.contains("\\")) {
                    fileName = file.substring(file.lastIndexOf("\\") + 1);
                }
            }

            return MimeUtility.encodeText(fileName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return fileName;
    }


    /**
     * 获取文件后缀名 包含 .
     *
     * @param file 文件路径
     * @return 文件后缀名
     */
    public static String postfix(String file) {
        String suffix = "";
        if (null != file && file.length() > 0 && file.contains(".")) {
            // 文件后缀名
            suffix = file.substring(file.lastIndexOf("."));
        }

        return suffix;
    }

    /**
     * 获取文件后缀名 不包含 .
     *
     * @param file 文件路径
     * @return 文件后缀名
     */
    public static String pfix(String file) {
        String suffix = postfix(file);
        if (StringUtils.isEmpty(suffix)) {
            return "";
        }

        // 文件后缀名
        return file.substring(file.indexOf(".") + 1, file.length());
    }


    public static byte[] file2byte(String filePath) {
        return ReadUtil.localFile2byte(filePath);
    }

    public static byte[] file2byte(File file) {
        return ReadUtil.localFile2byte(file);
    }

    public static boolean byte2file(byte[] buf, String filePath, String fileName) {
        File dir = new File(filePath);
        if (!dir.exists() && dir.isDirectory()) {
            dir.mkdirs();
        }

        return WriteUtil.byte2local(buf, new File(filePath + File.separator + fileName));
    }

    // 删除目录和文件
    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists()) {
            return;
        }

        if (path.isFile()) {
            boolean b = path.delete();
            if (b) {
                System.out.println("删除:" + path.getPath());
            }
            return;
        }

        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }

        path.delete();
    }
}

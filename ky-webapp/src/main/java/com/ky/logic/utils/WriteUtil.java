package com.ky.logic.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 写工具
 * Created by yl on 2017/8/17.
 */
public class WriteUtil {


    /**
     * 文件字节写入本地
     *
     * @param fileByte  文件字节
     * @param localFile 写入本地文件路径
     * @return 是否成功
     */
    public static boolean byte2local(byte[] fileByte, File localFile) {
        try (FileOutputStream out = new FileOutputStream(localFile)) {
            // 写入文件
            out.write(fileByte);
            out.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 文件写入本地
     *
     * @param imgBytes  文件字节
     * @param localFile 写入本地文件路径
     * @return 是否成功
     */
    public static boolean byte2local(byte[] imgBytes, String localFile) {
        return byte2local(imgBytes, new File(localFile));
    }

    /**
     * 本地拷贝
     *
     * @param localFile  本地文件
     * @param local2Path 拷贝到路径
     * @return 是否成功
     */
    public static boolean local2local(File localFile, File local2Path) {
        try (FileInputStream in = new FileInputStream(localFile)) {
            byte[] fileByte = new byte[(int) localFile.length()];
            in.read(fileByte);
            return byte2local(fileByte, local2Path);
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 本地拷贝
     *
     * @param localFile  本地文件
     * @param local2Path 拷贝到路径
     * @return 是否成功
     */
    public static boolean local2local(File localFile, String local2Path) {
        return local2local(localFile, new File(local2Path));
    }


    /**
     * 本地拷贝
     *
     * @param localFile  本地文件
     * @param local2Path 拷贝到路径
     * @return 是否成功
     */
    public static boolean local2local(String localFile, File local2Path) {
        return local2local(new File(localFile), local2Path);
    }

    /**
     * 本地拷贝
     *
     * @param localFile  本地文件
     * @param local2Path 拷贝到路径
     * @return 是否成功
     */
    public static boolean local2local(String localFile, String local2Path) {
        return local2local(new File(localFile), local2Path);
    }


    /**
     * 远端文件写入本地
     *
     * @param urlFile   远端文件路径
     * @param localPath 写入本地路径
     * @return 是否成功
     */
    public static boolean url2local(String urlFile, String localPath) {
        byte[] fileByte = ReadUtil.urlFile2byte(urlFile);
        return byte2local(fileByte, localPath);
    }

    /**
     * 远端文件写入本地
     *
     * @param httpFilePath 远端文件路径
     * @param localPath    写入本地路径
     * @return 是否成功
     */
    public static boolean http2local(String httpFilePath, String localPath) {
        byte[] fileByte = HttpUtil.INSTANCE.get(httpFilePath);
        return byte2local(fileByte, localPath);
    }


}

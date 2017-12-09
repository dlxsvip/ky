package com.ky.logic.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by yl on 2017/7/26.
 */
public enum ZipUtil {
    INSTANCE;

    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte


    public void zip(String src, String zipName) {
        System.out.println("压缩中...");

        try {
            File s = new File(src);
            if (!s.exists()) {
                s.mkdirs();
            }

            //创建zip输出流                             //创建缓冲输出流
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipName)));

            File srcFile = new File(src);

            // 压缩
            compress(out, srcFile, srcFile.getName());

            out.closeEntry();
            out.close();

            System.out.println("压缩完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void unzip(String zipName, String toDir) {
        try {
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipName)));

            File f = null;
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null && !entry.isDirectory()) {

                f = new File(toDir, entry.getName());
                if (!f.exists()) {
                    (new File(f.getParent())).mkdirs();
                }

                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(toDir + File.separator + entry.getName()), BUFF_SIZE);
                byte[] buf = new byte[BUFF_SIZE];
                int len;
                while ((len = zin.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                out.flush();
                out.close();


            }
            zin.close();

            System.out.println("解压成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 压缩
    private void compress(ZipOutputStream out, File sourceFile, String base) {
        try {
            System.out.println(base);

            if (sourceFile.isDirectory()) {
                //如果路径为目录（文件夹）

                //取出文件夹中的文件（或子文件夹）
                File[] list = sourceFile.listFiles();

                if (list.length == 0) {
                    //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                    base += File.separator;
                    out.putNextEntry(new ZipEntry(base + File.separator));
                } else {
                    //如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                    for (int i = 0; i < list.length; i++) {
                        compress(out, list[i], base + File.separator + list[i].getName());
                    }

                }

            } else {
                //如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
                out.putNextEntry(new ZipEntry(base));

                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
                //将源文件写入到zip文件中
                byte[] buf = new byte[BUFF_SIZE];
                int len;
                while ((len = bis.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                bis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

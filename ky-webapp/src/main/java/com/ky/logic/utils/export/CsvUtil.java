package com.ky.logic.utils.export;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV 导入导出工具
 * Created by yl on 2017/8/28.
 */
public enum CsvUtil {
    INSTANCE;

    public static final String ENCODE = "UTF-8";

    public void exportCsv(HttpServletResponse response, String fileName, List<String> dataList) {
        try {
            response.reset();
            response.setContentType("application/OCTET-STREAM;charset=" + ENCODE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, ENCODE) + ".csv");

            OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), ENCODE);

            // 加上UTF-8文件的标识字符
            osw.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));

            // 写入数据
            if (dataList != null && !dataList.isEmpty()) {
                for (String data : dataList) {
                    //osw.write(data);
                    osw.write(data + "\r");
                }
            }

            // 刷新
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出
     *
     * @param file     csv文件(路径+文件名)，csv文件不存在会自动创建
     * @param dataList 数据
     */
    public boolean exportCsv(File file, List<String> dataList) {
        boolean isSuccess = false;
        try (FileOutputStream out = new FileOutputStream(file); OutputStreamWriter osw = new OutputStreamWriter(out, ENCODE);
             BufferedWriter bw = new BufferedWriter(osw)) {

            // 加上UTF-8文件的标识字符
            bw.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));

            // 写入数据
            if (dataList != null && !dataList.isEmpty()) {
                for (String data : dataList) {
                    bw.append(data).append("\r");
                }
            }

            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSuccess;
    }

    /**
     * 读取 CSV 数据
     *
     * @param file csv文件(路径+文件)
     * @return 数据
     */
    public List<String> importCsv(File file) {
        List<String> dataList = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line = "";
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }
}

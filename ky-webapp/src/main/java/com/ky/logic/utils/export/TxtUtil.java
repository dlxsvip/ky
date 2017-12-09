package com.ky.logic.utils.export;

import com.ky.logic.model.KeyPersonAnalysisListModel;
import com.ky.logic.model.KeywordAnalysisListModel;
import com.ky.logic.model.info.TextInfo;
import com.ky.logic.utils.DateUtil;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.VideoUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by yl on 2017/7/25.
 */
public enum TxtUtil {

    INSTANCE;

    public static final String ENCODE = "UTF-8";
    public static final String ENTER = "\r\n";
    public static final int DEFAULT_LENGTH = 16;


    /**
     * 导出文本到 http请求里
     *
     * @param response http 响应
     * @param fileName 文件名
     * @param txt      文本数据
     */
    public void createTxt2http(HttpServletResponse response, String fileName, String txt) {
        try (ServletOutputStream outSTr = response.getOutputStream();
             BufferedOutputStream buff = new BufferedOutputStream(outSTr)) {

            response.reset();
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, ENCODE) + ".txt");

            buff.write(txt.getBytes(ENCODE));
            buff.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 导出本地文本
     *
     * @param file 文件(路径+文件名)
     * @param txt  文本数据
     */
    public void createTxt2local(File file, String txt) {
        try (FileOutputStream out = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(out, ENCODE);
             BufferedWriter buff = new BufferedWriter(osw)) {

            buff.write(txt);
            buff.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 导出文本到 http请求里
     *
     * @param response http 响应
     * @param fileName 文件名
     * @param lines    行数据列表
     */
    public void create2http(HttpServletResponse response, String fileName, List<String> lines) {
        createTxt2http(response, fileName, lines2txt(lines));
    }

    /**
     * 导出本地文本
     *
     * @param file  文件(路径+文件名)
     * @param lines 行数据列表
     */
    public void create2local(File file, List<String> lines) {
        createTxt2local(file, lines2txt(lines));
    }


    /**
     * 导出文本到 http请求里
     *
     * @param response http 响应
     * @param fileName 文件名
     * @param data     行、列 二维数组数据
     */
    public void export2httpTxt(HttpServletResponse response, String fileName, List<List<String>> data) {
        createTxt2http(response, fileName, data2txt(data));
    }

    /**
     * 导出本地文本
     *
     * @param file 文件(路径+文件名)
     * @param data 行、列 二维数组数据
     */
    public void export2localTxt(File file, List<List<String>> data) {
        createTxt2local(file, data2txt(data));
    }


    /**
     * 导出文本到 http请求里
     *
     * @param response http 响应
     * @param fileName 文件名
     * @param data     行、列 二维数组数据 带宽度
     */
    public void export2http(HttpServletResponse response, String fileName, List<List<TextInfo>> data) {
        createTxt2http(response, fileName, info2txt(data));
    }

    /**
     * 导出本地文本
     *
     * @param file 文件(路径+文件名)
     * @param data 行、列 二维数组数据 带宽度
     */
    public void export2local(File file, List<List<TextInfo>> data) {
        createTxt2local(file, info2txt(data));
    }


    /**
     * 导出关键词分析结果
     *
     * @param response 响应
     * @param lists    数据
     */
    public void exportKeywordAnalysisInfo2Text(HttpServletResponse response, List<KeywordAnalysisListModel> lists) {
        StringBuilder write = new StringBuilder();
        String enter = "\r\n";

        int length = 15;

        try (ServletOutputStream outSTr = response.getOutputStream();
             BufferedOutputStream buff = new BufferedOutputStream(outSTr)) {

            // 标题
            write.append(appendStr4Length("序号", 6))
                    .append(appendStr4Length("关键字", 12))
                    .append(appendStr4Length("采集主机", length))
                    .append(appendStr4Length("主机IP", length))
                    .append(appendStr4Length("采集频道", length))
                    .append(appendStr4Length("采集时间", 24))
                    .append(appendStr4Length("视频名称", 30))
                    .append(enter);

            for (int i = 0; i < lists.size(); i++) {
                KeywordAnalysisListModel info = lists.get(i);
                // 内容
                write.append(appendStr4Length(String.valueOf(i + 1), 6));
                write.append(appendStr4Length(info.getKeywordName(), 12));
                write.append(appendStr4Length(info.getHostName(), length));
                write.append(appendStr4Length(info.getHostIp(), length));
                write.append(appendStr4Length(info.getChannelName(), length));
                write.append(appendStr4Length(info.getCreateTime(), 24));
                write.append(appendStr4Length(info.getVideoName(), 30));
                write.append(enter);
            }
            buff.write(write.toString().getBytes("UTF-8"));
            buff.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 导出关键人分析结果
     *
     * @param response 响应
     * @param lists    数据
     */
    public void exportKeyPersonAnalysisInfo2Text(HttpServletResponse response, List<KeyPersonAnalysisListModel> lists) {
        StringBuilder write = new StringBuilder();
        String enter = "\r\n";

        int length = 15;

        try (ServletOutputStream outSTr = response.getOutputStream();
             BufferedOutputStream buff = new BufferedOutputStream(outSTr)) {

            // 标题
            write.append(appendStr4Length("序号", 6))
                    .append(appendStr4Length("关键人", 12))
                    .append(appendStr4Length("采集主机", length))
                    .append(appendStr4Length("主机IP", length))
                    .append(appendStr4Length("采集频道", length))
                    .append(appendStr4Length("采集时间", 24))
                    .append(appendStr4Length("视频名称", 30))
                    .append(enter);

            for (int i = 0; i < lists.size(); i++) {
                KeyPersonAnalysisListModel info = lists.get(i);
                // 内容
                write.append(appendStr4Length(String.valueOf(i + 1), 6));
                write.append(appendStr4Length(info.getPersonName(), 12));
                write.append(appendStr4Length(info.getHostName(), length));
                write.append(appendStr4Length(info.getHostIp(), length));
                write.append(appendStr4Length(info.getChannelName(), length));
                write.append(appendStr4Length(info.getCreateTime(), 24));
                write.append(appendStr4Length(info.getVideoName(), 30));
                write.append(enter);
            }
            buff.write(write.toString().getBytes("UTF-8"));
            buff.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 导出关键人分析结果
     *
     * @param response   响应
     * @param alarmCache 数据
     */
    public void exportAlarmData(HttpServletResponse response, Map<String, Map<String, Object>> alarmCache) {
        StringBuilder write = new StringBuilder();
        String enter = "\r\n";

        int length = 15;

        try (ServletOutputStream outSTr = response.getOutputStream();
             BufferedOutputStream buff = new BufferedOutputStream(outSTr)) {

            // 标题
            write.append(appendStr4Length("序号", 6))
                    .append(appendStr4Length("名称", 12))
                    .append(appendStr4Length("采集类型", 12))
                    .append(appendStr4Length("告警时间", 24))
                    .append(appendStr4Length("主机IP", length))
                    .append(appendStr4Length("采集频道", length))
                    .append(appendStr4Length("视频名称", 40))
                    .append(appendStr4Length("开始时间", 10))
                    .append(appendStr4Length("结束时间", 10))
                    .append(appendStr4Length("信息", 10))
                    .append(enter);

            int i = 0;
            Set<Map.Entry<String, Map<String, Object>>> entries = alarmCache.entrySet();
            Iterator<Map.Entry<String, Map<String, Object>>> it = entries.iterator();
            while (it.hasNext()) {
                Map.Entry<String, Map<String, Object>> entry = it.next();
                Map<String, Object> info = entry.getValue();
                // 内容
                write.append(appendStr4Length(String.valueOf(i + 1), 6));
                write.append(appendStr4Length((String) info.get("name"), 12));

                String type = (String) info.get("type");
                type = StringUtils.equals("word", type) ? "关键词" : "关键人";
                write.append(appendStr4Length(type, 12));

                Long time = (Long) info.get("time");
                String timeStr = null == time ? "" : DateUtil.date2str(time, DateUtil.YYYY_MM_DD_HH_MM_SS);
                write.append(appendStr4Length(timeStr, 24));
                write.append(appendStr4Length((String) info.get("hostName"), length));
                write.append(appendStr4Length((String) info.get("channelName"), length));
                write.append(appendStr4Length((String) info.get("videoName"), 40));

                Integer s = (Integer) info.get("startTime");
                String startTime = null == s ? "" : VideoUtil.getTime(s);
                Integer e = (Integer) info.get("endTime");
                String endTime = null == e ? "" : VideoUtil.getTime(e);
                write.append(appendStr4Length(startTime, 10));
                write.append(appendStr4Length(endTime, 10));
                write.append(appendStr4Length((String) info.get("text"), 10));
                write.append(enter);
                i++;
            }

            buff.write(write.toString().getBytes("UTF-8"));
            buff.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void historyTitle(String fileName) {
        //try (FileWriter out = new FileWriter(fileName)) {  FileWriter 不能解决UTF-8 问题
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"))) {
            StringBuilder title = new StringBuilder();
            // 标题
            title.append(appendStr4Length("序号", 6))
                    .append(appendStr4Length("名称", 12))
                    .append(appendStr4Length("采集类型", 12))
                    .append(appendStr4Length("告警时间", 24))
                    .append(appendStr4Length("主机IP", 15))
                    .append(appendStr4Length("采集频道", 15))
                    .append(appendStr4Length("视频名称", 40))
                    .append(appendStr4Length("开始时间", 10))
                    .append(appendStr4Length("结束时间", 10))
                    .append(appendStr4Length("信息", 10))
                    .append("\r\n");

            String str = title.toString();
            out.write(str, 0, str.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void historyData(String fileName, LinkedList<Map<String, Object>> msg) {
        // 获取行号
        int lines = getTotalLines(fileName);
        // 追加 写
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"))) {
            StringBuilder write = new StringBuilder();
            for (Map<String, Object> info : msg) {
                if (null == info) {
                    continue;
                }

                // 内容
                write.append(appendStr4Length(String.valueOf(lines), 6));
                write.append(appendStr4Length((String) info.get("name"), 12));

                String type = (String) info.get("type");
                type = StringUtils.equals("word", type) ? "关键词" : "关键人";
                write.append(appendStr4Length(type, 12));

                Long time = (Long) info.get("time");
                String timeStr = null == time ? "" : DateUtil.date2str(time, DateUtil.YYYY_MM_DD_HH_MM_SS);
                write.append(appendStr4Length(timeStr, 24));
                write.append(appendStr4Length((String) info.get("hostName"), 15));
                write.append(appendStr4Length((String) info.get("channelName"), 15));
                write.append(appendStr4Length((String) info.get("videoName"), 40));

                Integer s = (Integer) info.get("startTime");
                String startTime = null == s ? "" : VideoUtil.getTime(s);
                Integer e = (Integer) info.get("endTime");
                String endTime = null == e ? "" : VideoUtil.getTime(e);
                write.append(appendStr4Length(startTime, 10));
                write.append(appendStr4Length(endTime, 10));
                write.append(appendStr4Length((String) info.get("text"), 10));
                write.append("\r\n");
                lines++;
            }

            String str = write.toString();
            out.write(str, 0, str.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String appendStr4Length(String str, int length) {
        String methodName = "appentStr4Length";
        if (str == null) {
            str = "--";
        }
        try {
            int strLen = 0;//计算原字符串所占长度,规定中文占两个,其他占一个
            for (int i = 0; i < str.length(); i++) {
                if (isChinese(str.charAt(i))) {
                    strLen = strLen + 2;
                } else {
                    strLen = strLen + 1;
                }
            }
            if (strLen >= length) {
                return str;
            }
            int remain = length - strLen;//计算所需补充空格长度
            for (int i = 0; i < remain; i++) {
                str = str + " ";
            }
        } catch (Exception e) {
            LoggerUtil.printStackTrace(this.getClass().getName(), methodName, e);
        }
        return str;
    }


    // 根据Unicode编码完美的判断中文汉字和符号
    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }


    // 获取文件行号
    private int getTotalLines(String fileName) {
        int totalLines = 0;
        try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(fileName))) {
            String line = null;
            while ((line = lineNumberReader.readLine()) != null) {
                totalLines++;
            }
        } catch (Exception e) {
            return 0;
        }

        return totalLines;
    }

    // 组装文本数据
    private String lines2txt(List<String> data) {
        StringBuilder write = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            // 行数据
            write.append(data.get(i));
            write.append(ENTER);
        }

        return write.toString();
    }

    /*private List<String> data2lines(List<List<String>> data) {
        List<String> lines = new ArrayList<>();

        StringBuilder line;
        List<String> item;
        for (int i = 0; i < data.size(); i++) {
            line = new StringBuilder();

            // 行数据
            item = data.get(i);
            for (int j = 0; j < item.size(); j++) {
                // 列数据
                line.append(appendStr4Length(item.get(j), DEFAULT_LENGTH));
            }

            lines.add(line.toString());
        }

        return lines;
    }*/

    // 组装文本数据
    private String data2txt(List<List<String>> data) {
        StringBuilder write = new StringBuilder();
        List<String> item;
        for (int i = 0; i < data.size(); i++) {
            // 行数据
            item = data.get(i);
            for (int j = 0; j < item.size(); j++) {
                // 列数据
                write.append(appendStr4Length(item.get(j), DEFAULT_LENGTH));
            }
            write.append(ENTER);
        }

        return write.toString();
    }


    // 组装文本数据
    private String info2txt(List<List<TextInfo>> data) {
        StringBuilder write = new StringBuilder();
        List<TextInfo> item;
        for (int i = 0; i < data.size(); i++) {
            // 行数据
            item = data.get(i);
            for (int j = 0; j < item.size(); j++) {
                // 列数据
                TextInfo info = item.get(j);
                write.append(appendStr4Length(info.getData(), info.getWeigh()));
            }
            write.append(ENTER);
        }

        return write.toString();
    }



}

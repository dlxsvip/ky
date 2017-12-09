package com.ky.logic.controller;

import com.ky.logic.common.cache.AlarmPushCache;
import com.ky.logic.utils.*;
import com.ky.logic.utils.export.ExcelUtil;
import com.ky.logic.utils.export.TxtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * 实时数据
 * Created by yl on 2017/7/26.
 */
@Controller
@RequestMapping("/captureChannel")
public class RealTimeDataController {

    /**
     * 导出实时告警
     * 文本格式
     *
     * @param request  http请求
     * @param response http响应
     */
    @RequestMapping(value = "/exportRealTimeData", method = RequestMethod.GET)
    @ResponseBody
    public void exportRealTimeData(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Map<String, Object>> alarmCache = AlarmPushCache.getAlarmCache();
            response.reset();
            response.setContentType("text/plain");
            String time = DateUtil.date2str(new Date(), "yyyy-MM-dd-HH-mm-ss");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("实时告警数据_" + time, "utf-8") + ".txt");
            TxtUtil.INSTANCE.exportAlarmData(response, alarmCache);
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), "exportKeywordInfos", "导出异常，" + e.getMessage());
        }
    }

    /**
     * 导出实时告警
     * excel
     *
     * @param request  http请求
     * @param response http响应
     */
    @RequestMapping(value = "/exportRealTimeDataByXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportRealTimeDataByXls(HttpServletRequest request, HttpServletResponse response) {
        try {



            List<List<String>> data = new ArrayList<>();

            List<String> title = new ArrayList<>();
            title.add("名称");
            title.add("采集类型");
            title.add("告警时间");
            title.add("主机IP");
            title.add("采集频道");
            title.add("视频名称");
            title.add("开始时间");
            title.add("结束时间");
            title.add("信息");
            data.add(title);

            Map<String, Map<String, Object>> alarmCache = AlarmPushCache.getAlarmCache();
            Set<Map.Entry<String, Map<String, Object>>> entries = alarmCache.entrySet();
            Iterator<Map.Entry<String, Map<String, Object>>> it = entries.iterator();
            List<String> cell;
            while (it.hasNext()) {
                Map.Entry<String, Map<String, Object>> entry = it.next();
                Map<String, Object> info = entry.getValue();

                cell = new ArrayList<>();
                cell.add((String) info.get("name"));

                String type = (String) info.get("type");
                type = StringUtils.equals("word", type) ? "关键词" : "关键人";
                cell.add(type);

                Long time = (Long) info.get("time");
                String timeStr = null == time ? "" : DateUtil.date2str(time, DateUtil.YYYY_MM_DD_HH_MM_SS);
                cell.add(timeStr);

                cell.add((String) info.get("hostName"));
                cell.add((String) info.get("channelName"));
                cell.add((String) info.get("videoName"));

                Integer s = (Integer) info.get("startTime");
                String startTime = null == s ? "" : VideoUtil.getTime(s);
                Integer e = (Integer) info.get("endTime");
                String endTime = null == e ? "" : VideoUtil.getTime(e);

                cell.add(startTime);
                cell.add(endTime);
                cell.add((String) info.get("text"));


                data.add(cell);
            }

            String curTime = DateUtil.date2str(new Date(), "yyyy-MM-dd-HH-mm-ss");
            String fileName = "告警数据_" + curTime;

            ExcelUtil.INSTANCE.createWorkbook(response, fileName);
            ExcelUtil.INSTANCE.createSheet("告警数据", data);
            ExcelUtil.INSTANCE.writeSheet();
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), "exportRealTimeDataByXls", "导出异常，" + e.getMessage());
        }
    }

    /**
     * 导出历史告警
     *
     * @param request  http请求
     * @param response http响应
     */
    @RequestMapping(value = "/exportHistoryData", method = RequestMethod.GET)
    @ResponseBody
    public void exportHistoryData(HttpServletRequest request, HttpServletResponse response) {
        try (ServletOutputStream outSTr = response.getOutputStream(); BufferedOutputStream buff = new BufferedOutputStream(outSTr)) {

            response.reset();
            response.setContentType("application/zip;charset=UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            String time = DateUtil.date2str(new Date(), "yyyy-MM-dd");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("历史告警数据_" + time, "utf-8") + ".zip");


            // 数据目录
            String dataDir = WebPathUtil.getViewSubDir("data");
            // 待压缩的源目录 history 目录
            String src = WebPathUtil.getViewSubDir("data" + File.separator + "history");

            // 压缩包临时目录
            long todayDir = DateUtil.clearHours(new Date()).getTime();

            String tempZipDir = dataDir + "ziptmp" + File.separator + todayDir + File.separator;
            File zipDir = new File(tempZipDir);
            if (!zipDir.exists()) {
                zipDir.mkdirs();
            }

            // 压缩包临时名称
            String zipName = tempZipDir + IdUtil.uuid(8) + ".zip";
            // 生成压缩包
            ZipUtil.INSTANCE.zip(src, zipName);


            File zipFile = new File(zipName);
            // 读取压缩包
            FileInputStream in = new FileInputStream(zipFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                buff.write(buf, 0, len);
            }

            buff.flush();
            in.close();

            // 删除压缩包
            zipFile.delete();
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), "exportHistoryData", "导出异常，" + e.getMessage());
        }
    }
}

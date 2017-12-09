package com.ky.logic.common.job.send;


import com.ky.logic.utils.DateUtil;
import com.ky.logic.utils.WebPathUtil;
import com.ky.logic.utils.export.TxtUtil;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

/**
 * 历史告警数据备份任务
 * Created by yl on 2017/7/26.
 */
public class HistoricalDataBakJob implements Runnable {
    LinkedList<Map<String, Object>> msg = new LinkedList<>();

    public HistoricalDataBakJob(LinkedList<Map<String, Object>> msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        execute();
    }

    private void execute() {
        try {
            Date today = DateUtil.clearHours(new Date());
            String filePath = WebPathUtil.getViewSubDir("data" + File.separator + "history" + File.separator + today.getTime());

            /* 构建文件目录 */
            File fileDir = new File(filePath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            String fileName = "实时告警历史数据_" + DateUtil.date2str(today, DateUtil.YYYY_MM_DD) + ".txt";

            String fileAllPath = filePath + File.separator + fileName;
            File file = new File(fileAllPath);
            if (!file.exists()) {
                // 生成并写入标题
                TxtUtil.INSTANCE.historyTitle(fileAllPath);
            }

            // 追加写内容
            TxtUtil.INSTANCE.historyData(fileAllPath, msg);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

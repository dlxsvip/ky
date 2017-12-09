package com.ky.logic.common.task;

import com.ky.logic.common.cache.SystemCache;
import com.ky.logic.utils.DateUtil;
import com.ky.logic.utils.FileUtil;
import com.ky.logic.utils.WebPathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

/**
 * 定时删除本地历史告警数据
 * Created by yl on 2017/7/27.
 */
@Service
public class DeleteAlarmHistoryDataTask {

    /**
     * 是否 执行
     */
    private Boolean open;

    @Autowired
    public void setOpen(@Value("${task.delete.alarmHistoryData.open}") Boolean open) {
        this.open = open;
    }


    @Scheduled(cron = "${task.delete.alarmHistoryData.cycle}")
    private void execute() {
        if (open) {

            // 删除 今天之前的 告警数据
            //long todayDir = DateUtil.clearHours(new Date()).getTime();

            int n = SystemCache.getInstance().getInteger("delete.alarmHistoryData.day");

            // 删除 n 天前的目录
            long deleteDir = DateUtil.clearHours(DateUtil.addDay(new Date(), -n)).getTime();

            // 读取目录
            String ziptmp = WebPathUtil.getViewSubDir("data/history");
            File tmp = new File(ziptmp);
            if (!tmp.exists()) {
                return;
            }

            File[] files = tmp.listFiles();
            if (null == files) {
                return;
            }

            for (File f : files) {
                // 文件创建时间
                long dirTime = Long.parseLong(f.getName());
                //System.out.println(f.getName());
                //System.out.println(f.getPath());
                //System.out.println(f.getParent());
                if (deleteDir > dirTime) {
                    FileUtil.deleteAllFilesOfDir(f);
                }

            }

        }
    }


}

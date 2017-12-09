package com.ky.logic.common.task;

import com.ky.logic.common.cache.SystemCache;
import com.ky.logic.model.OssConfigModel;
import com.ky.logic.model.Paging;
import com.ky.logic.service.ISystemConfigService;
import com.ky.logic.service.IVideoInfoService;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.Oss;
import com.ky.logic.utils.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时删除 OSS上 视频数据
 * Created by yl on 2017/7/27.
 */
@Service
public class DeleteOssVideoTask {

    /**
     * 是否 执行
     */
    private Boolean open;

    @Resource(name = "videoInfoService")
    private IVideoInfoService videoInfoService;

    @Resource(name = "SystemConfigService")
    private ISystemConfigService systemConfigService;

    @Autowired
    public void setOpen(@Value("${task.delete.OssVideo.open}") Boolean open) {
        this.open = open;
    }


    @Scheduled(cron = "${task.delete.OssVideo.cycle}")
    private void execute() {
        if (open) {
            try {
                int n = SystemCache.getInstance().getInteger("delete.OssVideo.day");

                OssConfigModel ossConfigModel = systemConfigService.getConfigOss();
                Oss oss = new Oss(ossConfigModel.getEndpoint(), ossConfigModel.getAccesskeyID(), ossConfigModel.getAccesskeyKey(), true);
                if (null == oss) {
                    LoggerUtil.errorSysLog(this.getClass().getName(), "deleteVideoFormOss", "没有获取到系统OSS");
                    return;
                }

                optVideoInfoByPage(oss, ossConfigModel.getBucketname(), n);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void optVideoInfoByPage(Oss oss, String bucketName, int n) {
        try {
            int num = -1;// 循环的次数

            int pageNumber = 1;  //页码
            int pageSize = 100;  //每页的条数
            Paging paging = null;

            do {
                // 先查询出 一页100个  n 天前的 视频，查出总页数后，在判断是否还要继续查询
                paging = new Paging();
                paging.setPageNum(pageNumber);
                paging.setPageSize(pageSize);

                Page<String> page = videoInfoService.queryRemoteAddressByCreateTime(n, paging);

                int totalPages = page.getTotalPages();
                if (num == -1) {
                    num = totalPages;
                }
                num -= 1;

                List<String> list = page.getResult();
                if (null == list || list.size() == 0) {
                    break;
                }


                delete4oss(oss, bucketName, list);

                pageNumber += 1;
            } while (num > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void delete4oss(Oss oss, String bucketName, List<String> remoteAddress) {
        try {
            // 返回被删除的文件
            List<String> deletedObjects = oss.deleteObjects(bucketName, remoteAddress);
            if (null == deletedObjects || deletedObjects.isEmpty()) {
                return;
            }

            // 更新 视频oss地址为空
            videoInfoService.updateVideoRemoteAddress(remoteAddress);
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), "delete4oss", "删除OSS 视频异常" + e.getMessage());
            e.printStackTrace();
        }
    }
}

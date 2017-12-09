package com.ky.logic.service.impl;

import com.ky.logic.dao.IVideoInfoDao;
import com.ky.logic.entity.VideoInfoEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.service.IVideoInfoService;
import com.ky.logic.utils.page.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yl on 2017/7/13.
 */
@Service(value = "videoInfoService")
public class VideoInfoService implements IVideoInfoService {

    @Resource(name = "videoInfoDao")
    private IVideoInfoDao videoInfoDao;

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public VideoInfoEntity query(String videoId) throws Exception {
        return videoInfoDao.query(videoId);
    }

    /**
     * 查询n 天前的视频信息
     *
     * @param n 几天前
     * @return 视频列表
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<VideoInfoEntity> queryBeforeCreateTime(int n, Paging paging) throws Exception {
        return videoInfoDao.queryBeforeCreateTime(n, paging);
    }

    /**
     * 查询n 天前的视频OSS地址
     *
     * @param n      几天前
     * @param paging
     * @return OSS地址列表
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Page<String> queryRemoteAddressByCreateTime(int n, Paging paging) throws Exception {
        Page<String> page = new Page<>();
        try {
            long totalRows = videoInfoDao.queryCountByCreateTime(n);
            if (totalRows == 0L) {
                return page;
            }

            page.setTotalRows(totalRows);
            page.setCurPage(paging.getPageNum());
            page.setPageSize(paging.getPageSize());

            List<String> list = videoInfoDao.queryRemoteAddressByCreateTime(n, paging);
            page.setResult(list);

            // 总页数
            int totalPages;
            if (0 != paging.getPageSize()) {
                if (totalRows % paging.getPageSize() == 0) {
                    totalPages = (int) totalRows / paging.getPageSize();
                } else {
                    totalPages = (int) totalRows / paging.getPageSize() + 1;
                }
            } else {
                totalPages = (int) totalRows;
            }
            page.setTotalPages(totalPages);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return page;
    }

    /**
     * 更新 视频oss地址为空
     *
     * @param remoteAddress 视频地址列表
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateVideoRemoteAddress(List<String> remoteAddress) {
        StringBuffer adds = new StringBuffer();
        for (int i = 0; i < remoteAddress.size(); i++) {
            adds.append("'").append(remoteAddress.get(i)).append("'");

            if (i < remoteAddress.size() - 1) {
                adds.append(",");
            }
        }
        videoInfoDao.updateVideoRemoteAddress(adds.toString());
    }
}

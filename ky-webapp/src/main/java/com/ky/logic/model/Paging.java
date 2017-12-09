package com.ky.logic.model;

import java.io.Serializable;

/**
 * Created by yl on 2017/7/13.
 */
public class Paging implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 是否升序
     */
    private Boolean orderAsc;

    /**
     * 起始页
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;


    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Boolean getOrderAsc() {
        return orderAsc;
    }

    public void setOrderAsc(Boolean orderAsc) {
        if (null == orderAsc) {
            this.orderAsc = false;
        } else {
            this.orderAsc = orderAsc;
        }
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if (null == pageNum) {
            this.pageNum = 0;
        } else {
            this.pageNum = pageNum;
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (null == pageSize) {
            this.pageSize = 0;
        } else {
            this.pageSize = pageSize;
        }
    }
}

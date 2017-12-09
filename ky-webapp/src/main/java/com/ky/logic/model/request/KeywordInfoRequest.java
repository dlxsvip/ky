package com.ky.logic.model.request;


import com.ky.logic.model.Paging;

import java.io.Serializable;

/**
 * Created by yl on 2017/7/7.
 */
public class KeywordInfoRequest extends Paging implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 关键词ID
     */
    private String keywordId;

    /**
     * 关键词
     */
    private String keywordName;

    /**
     * 添加时间
     */
    private String createTime;

    /**
     * 是否启用
     */
    private Boolean available;

    /**
     * 描述
     */
    private String description;

    /**
     * 标签
     */
    private String label;

    public String getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

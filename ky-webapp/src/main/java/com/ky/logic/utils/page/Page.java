package com.ky.logic.utils.page;

import com.ky.logic.utils.page.restriction.AbstractCriterionCreater;
import com.ky.logic.utils.page.restriction.RelationalTable;
import org.hibernate.Criteria;

import java.util.ArrayList;
import java.util.List;

public class Page<E> {

    private int curPage;
    private int pageSize;
    private int startRow;
    private long totalRows;
    private int totalPages;
    private DBCriteriaBuilder crBuilder = new DBCriteriaBuilder();
    private List<E> result;
    private String distinctColumn;

    private List<String> FetchModeList;

    public Page(int curPage, int pageSize) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.startRow = curPage > 0 ? (curPage - 1) * pageSize : 0;
    }

    public Page() {
        this.curPage = 0;
        this.pageSize = 0;
        this.startRow = 0;
    }

    public Page(int curPage, int startRow, int pageSize) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.startRow = startRow;
    }


    public void setResultTransformer() {
        crBuilder.setResultTransformer(true);
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<E> getResult() {
        return result;
    }

    public void setResult(List<E> result) {
        this.result = result;
    }

    public boolean getOrderAscending() {
        return crBuilder.getOrderAscending();
    }

    public void setOrderAscending(boolean orderAscending) {
        crBuilder.setOrderAscending(orderAscending);
    }

    public String getOrderBy() {
        return crBuilder.getOrderBy();
    }

    public void setOrderBy(String orderBy) {
        if (null != orderBy) {
            crBuilder.setOrderBy(orderBy);
        }
    }

    public List<String> getFetchModeList() {
        return FetchModeList;
    }

    public void setFetchModeList(List<String> fetchModeList) {
        FetchModeList = fetchModeList;
    }

    public void buildCriteria(Criteria cr) {
        crBuilder.setCr(cr);

        crBuilder.build();
    }

    public List<E> getMergeResult(Page<E> page) {
        List<E> result = new ArrayList<E>();

        if (0 != page.getStartRow()) {
            result = page.getResult().subList(page.getStartRow()*page.getPageSize(), page.getStartRow()*page.getPageSize()+page.getPageSize()+1);
        } else {
            result = page.getResult();
        }

        return result;
    }

    public List<Parameter> getFilterParameters() {
        return crBuilder.getFilterParams();
    }

    public Page addCriterionCreater(AbstractCriterionCreater criterionCreater) {
        crBuilder.addCriterionCreater(criterionCreater);
        return this ;
    }

    /***
     * associationPath 关联表或者关联字段名称
     * aliasName 别名
     * distinctColumn 用于区分数据条目的主表字段（主表主键）
     */
    public void addRelationalTable(String associationPath, String aliasName, String distinctColumn) {
        if (null != associationPath) {
            crBuilder.addRelationalTable(new RelationalTable(associationPath, aliasName));
            this.distinctColumn = distinctColumn;
        }
    }

//    public void addRelationalTable(String associationPath, String distinctColumn) {
//        if (null != associationPath) {
//            crBuilder.addRelationalTable(new RelationalTable(associationPath));
//            this.distinctColumn = distinctColumn;
//        }
//    }

    public String getDistinctColumn() {
        return distinctColumn;
    }

    public void setDistinctColumn(String distinctColumn) {
        this.distinctColumn = distinctColumn;
    }

    @Override
    public String toString() {
        return "Page{" +
                "curPage=" + curPage +
                ", pageSize=" + pageSize +
                ", startRow=" + startRow +
                ", totalRows=" + totalRows +
                ", totalPages=" + totalPages +
                ", result=" + result +
                '}';
    }

}

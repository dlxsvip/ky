package com.ky.logic.utils.page;

import org.hibernate.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 2016/2/1.
 */
public class DtDreamPage<T, E> {

    private int curPage;
    private int pageSize;
    private int startRow;
    private long totalRows;
    private int totalPages;
    private DtDreamDBCriteriaBuilder crBuilder = new DtDreamDBCriteriaBuilder();
    private List<T> aliResult;
    private List<E> openstackResult;

    private List<String> aliFetchModeList;
    private List<String> openstackFetchModeList;

    public DtDreamPage(int curPage, int pageSize) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.startRow = curPage > 0 ? (curPage - 1) * pageSize : 0;
    }

    public DtDreamPage() {
        this.curPage = 0;
        this.pageSize = 0;
        this.startRow = 0;
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

    public List<E> getOpenstackResult() {
        return openstackResult;
    }

    public void setOpenstackResult(List<E> openstackResult) {
        this.openstackResult = openstackResult;
    }

    public List<T> getAliResult() {
        return aliResult;
    }

    public void setAliResult(List<T> aliResult) {
        this.aliResult = aliResult;
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
        crBuilder.setOrderBy(orderBy);
    }

    public String getOrderByAli() {
        return crBuilder.getOrderByAli();
    }

    public void setOrderByAli(String orderByAli) {
        crBuilder.setOrderByAli(orderByAli);
    }

    public List<String> getAliFetchModeList() {
        return aliFetchModeList;
    }

    public void setAliFetchModeList(List<String> aliFetchModeList) {
        this.aliFetchModeList = aliFetchModeList;
    }

    public List<String> getOpenstackFetchModeList() {
        return openstackFetchModeList;
    }

    public void setOpenstackFetchModeList(List<String> openstackFetchModeList) {
        this.openstackFetchModeList = openstackFetchModeList;
    }

    public void buildAliCriteria(Criteria aliCr) {
        crBuilder.setAliCr(aliCr);

        crBuilder.buildAli();
    }

    public void buildOpenstackCriteria(Criteria openstackCriteria) {
        crBuilder.setOpenstackCr(openstackCriteria);

        crBuilder.buildOpenstack();
    }

    public List<Parameter> getAliFilterParameters() {
        return crBuilder.getAliFilterParams();
    }

    public List<Parameter> getOpenstackFilterParameters() {
        return crBuilder.getOpenstackFilterParams();
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


    @Override
    public String toString() {
        return "Page{" +
                "curPage=" + curPage +
                ", pageSize=" + pageSize +
                ", startRow=" + startRow +
                ", totalRows=" + totalRows +
                ", totalPages=" + totalPages +
                ", aliResult=" + aliResult +
                ", openstackResult=" + openstackResult +
                '}';
    }
}

package com.ky.pm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResponseInfo {
    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";
    private String result;
    /*此字段预留，后面增加二级错误码*/
    private String responseCode;
    private Object data;
    private String errorMsg;
    private String errorDetail;

    public ResponseInfo(String responseCode, String result, Object data, String errorMsg, String errorDetail) {
        this.responseCode = responseCode;
        this.result = result;
        this.data = data;
        this.errorMsg = errorMsg;
        this.errorDetail = errorDetail;
    }

    public ResponseInfo() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    /*操作成功的返回值封装*/
    public void createSuccessResponse(Object data) {
        this.result = SUCCESS;
        this.responseCode = "200";
        this.data = data;
        this.errorMsg = "";
        this.errorDetail = "";
    }
    /*操作失败的返回值封装，其中errorMsg必填，errDetail和data大家视情况而定*/
    public void createFailedResponse( Object data, String errorMsg, String errorDetail) {
        this.result = FAILED;
        this.responseCode = "";
        if (null == data) {
            this.data ="";
        } else {
            this.data = data;
        }
        this.errorMsg = errorMsg;

        if(null == errorDetail) {
            this.errorDetail = "";
        } else {
            this.errorDetail = errorDetail;
        }
    }

    /*如果pageSize和pageNum不为空，则返回结果按分页方式返回，否则返回结果不进行分页*/
    public void createSuccessResponsePage(Integer pageSize, Integer pageNum, Object data, Long totalRows) {
        Map<String,Object> map = new HashMap<String,Object>();
        this.result = SUCCESS;
        this.responseCode = "200";
        if ((null != pageSize) && (null != pageNum) && (1 <= pageSize) && (0 <= pageNum)){
            map.put("pageSize",pageSize);
            map.put("pageNum",pageNum);
            map.put("totalRows",totalRows);
            map.put("rows",data);
        } else {
            map.put("totalRows",totalRows);
            map.put("rows",data);
        }
        this.data = map;
        this.errorMsg = "";
        this.errorDetail = "";
    }

    /*如果pageSize和pageNum不为空，则返回结果按分页方式返回，否则返回结果不进行分页*/
    public void createFailedResponsePage(Integer pageSize, Integer pageNum,String errorMsg, String errorDetail) {
        Map<String,Object> map = new HashMap<String,Object>();
        this.result = FAILED;
        this.responseCode = "";
        if ((null != pageSize) && (null != pageNum) && (1 <= pageSize) && (0 <= pageNum)){
            map.put("pageSize",pageSize);
            map.put("pageNum",pageNum);
            map.put("totalRows",0);
            map.put("rows",new ArrayList<String>());
        } else {
            map.put("totalRows",0);
            map.put("rows",new ArrayList<String>());
        }
        this.data = map;
        if(null == errorMsg) {
            this.errorMsg = "";
        } else {
            this.errorMsg = errorMsg;
        }
        if (null == errorDetail) {
            this.errorDetail = "";
        } else {
            this.errorDetail = errorDetail;
        }
    }

    public boolean succeeded(){
        return SUCCESS.equals(this.result) ;
    }
}

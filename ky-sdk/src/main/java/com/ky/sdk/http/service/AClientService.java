package com.ky.sdk.http.service;


import com.ky.sdk.http.client.ManageClient;
import com.ky.pm.utils.AKUtil;
import com.ky.sdk.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yl on 2017/7/22.
 */
public class AClientService {

    private ManageClient client;

    public AClientService(ManageClient client) {
        this.client = client;
    }

    public String doGet(String function, Map<String, String> params) {
        return HttpUtil.INSTANCE.doGet(getUrl(function), params, getHeaders("GET"));
    }

    public String doPost(String function, String type, Map<String, String> params) {
        return HttpUtil.INSTANCE.doPost(getUrl(function), type, params, getHeaders("POST"));
    }

    public String doPost(String function,String jsonParam) {
        return HttpUtil.INSTANCE.doJsonPost(getUrl(function), jsonParam, getHeaders("POST"));
    }

    public String getUrl(String function) {
        return client.getUrl() + client.getApi() + function;
    }

    public Map<String, String> getHeaders(String method) {
        String accessKeyId = client.getAccessKeyId();
        String accessKeySecret = client.getAccessKeySecret();
        String signatureMethod = client.getSignatureMethod();
        String version = client.getVersion();
        String signature = AKUtil.SignAK(method, accessKeyId, accessKeySecret, signatureMethod, version);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("AccessKeyId", accessKeyId);
        headers.put("Signature", signature);
        headers.put("SignatureMethod", signatureMethod);
        headers.put("Version", version);
        return headers;
    }
}

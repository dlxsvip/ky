package com.ky.logic.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by yl on 2017/7/3.
 */
public enum HttpUtil {

    INSTANCE;


    /**
     * http get 请求
     *
     * @param url 请求地址
     * @return 字符串结果
     */
    public String doGet(String url) {
        return doGet(url, null);
    }


    /**
     * http get 请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @return 字符串结果
     */
    public String doGet(String url, Map<String, String> headers) {
        return doGet(url, null, headers);
    }


    /**
     * http get 请求
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 字符串结果
     */
    public String doGet(String url, Map<String, String> params, Map<String, String> headers) {
        // 获取响应实体
        HttpEntity entity = get(url, params, headers);
        String result = getResult(entity);
        return result;
    }

    public byte[] get(String url) {
        byte[] data = null;
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = createSSLClientDefault();

            // 设置参数
            url = setparams(url, null);

            httpGet = new HttpGet(url);

            // 设置头消息
            setHeader(httpGet, null);

            // 执行get请求.
            response = httpClient.execute(httpGet);
            // 打印响应状态
            //System.out.println(response.getStatusLine());

            // 获取响应实体
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();

            data = readByte(in, null);
            in.close();
        } catch (Exception e) {
        } finally {
            httpResponseClose(response);
            httpAbort(httpGet);
            httpClientClose(httpClient);
        }

        return data;
    }

    public HttpEntity get(String url, Map<String, String> params, Map<String, String> headers) {
        HttpEntity entity = null;
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = createSSLClientDefault();

            // 设置参数
            url = setparams(url, params);

            httpGet = new HttpGet(url);

            // 设置头消息
            setHeader(httpGet, headers);

            // 执行get请求.
            response = httpClient.execute(httpGet);
            // 打印响应状态
            //System.out.println(response.getStatusLine());

            // 获取响应实体
            entity = response.getEntity();
        } catch (Exception e) {
            LoggerUtil.errorSysLog(HttpUtil.class.getName(), "doGet", "get 请求异常：" + e.getMessage());
        } finally {
            httpResponseClose(response);
            httpAbort(httpGet);
            httpClientClose(httpClient);
        }

        return entity;
    }

    public String doFormGet(String url, String urlParam, Map<String, String> headers) {
        String result = "";
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = createSSLClientDefault();

            // 设置参数
            if (!url.contains("?")) {
                url += "?";
            }

            url += urlParam;

            httpGet = new HttpGet(url);

            // 设置头消息
            setHeader(httpGet, headers);

            // 执行get请求.
            response = httpClient.execute(httpGet);
            // 打印响应状态
            System.out.println(response.getStatusLine());

            // 获取响应实体
            HttpEntity entity = response.getEntity();
            result = getResult(entity);
        } catch (Exception e) {
            result = "get 请求异常:" + e.getMessage();
        } finally {
            httpResponseClose(response);
            httpAbort(httpGet);
            httpClientClose(httpClient);
        }

        return result;
    }

    public String doPost(String url) {
        return doPost(url, "form", null);
    }

    /**
     * http post 请求
     *
     * @param url    请求地址
     * @param type   参数体形式:json|form
     * @param params 请求参数
     * @return 字符串结果
     */
    public String doPost(String url, String type, Map<String, String> params) {
        return doPost(url, type, params, null);
    }


    /**
     * http post 请求
     *
     * @param url     请求地址
     * @param type    参数体形式:json|form
     * @param params  请求参数
     * @param headers 请求头
     * @return 字符串结果
     */
    public String doPost(String url, String type, Map<String, String> params, Map<String, String> headers) {
        String result = "";
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;

        try {
            httpClient = createSSLClientDefault();

            // 创建httpPost
            httpPost = new HttpPost(url);
            // 设置头消息
            setHeader(httpPost, headers);

            // 设置post参数
            setPostParamsEntity(httpPost, params, type);

            // 执行post请求.
            response = httpClient.execute(httpPost);

            // 打印响应状态
            //System.out.println(response.getStatusLine());

            // 获取响应实体
            HttpEntity entity = response.getEntity();
            result = getResult(entity);
        } catch (Exception e) {
            result = "post 请求异常:" + e.getMessage();
        } finally {
            httpResponseClose(response);
            httpAbort(httpPost);
            httpClientClose(httpClient);
        }

        return result;
    }

    public String doJsonPost(String url, String jsonParam, Map<String, String> headers) {
        String result = "";
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;

        try {
            httpClient = createSSLClientDefault();

            // 创建httpPost
            httpPost = new HttpPost(url);
            // 设置头消息
            setHeader(httpPost, headers);

            // 设置post参数
            setPostJsonParamsEntity(httpPost, jsonParam);

            // 执行post请求.
            response = httpClient.execute(httpPost);

            // 打印响应状态
            System.out.println(response.getStatusLine());

            // 获取响应实体
            HttpEntity entity = response.getEntity();
            result = getResult(entity);
        } catch (Exception e) {
            result = "post 请求异常:" + e.getMessage();
        } finally {
            httpResponseClose(response);
            httpAbort(httpPost);
            httpClientClose(httpClient);
        }

        return result;
    }

    public String doFormPost(String url, String urlParam, Map<String, String> headers) {
        String result = "";
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;

        try {
            httpClient = createSSLClientDefault();

            // 创建httpPost
            httpPost = new HttpPost(url);
            // 设置头消息
            setHeader(httpPost, headers);

            // 设置url参数
            setPostFormParamsEntity(httpPost, urlParam);

            // 执行post请求.
            response = httpClient.execute(httpPost);

            // 打印响应状态
            System.out.println(response.getStatusLine());

            // 获取响应实体
            HttpEntity entity = response.getEntity();
            result = getResult(entity);
        } catch (Exception e) {
            result = "post 请求异常:" + e.getMessage();
        } finally {
            httpResponseClose(response);
            httpAbort(httpPost);
            httpClientClose(httpClient);
        }

        return result;
    }

    public String doPut(String url) {
        return doPut(url, null);
    }

    public String doPut(String url, Map<String, String> headers) {
        return doPut(url, null, null, headers);
    }

    public String doPut(String url, String type, Map<String, String> params, Map<String, String> headers) {
        String result = "";
        CloseableHttpClient httpClient = null;
        HttpPut httpPut = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = createSSLClientDefault();

            // 创建httpPut
            httpPut = new HttpPut(url);

            // 设置头消息
            setHeader(httpPut, headers);

            // 设置put参数
            setPutParamsEntity(httpPut, params, type);

            // 执行put请求.
            response = httpClient.execute(httpPut);

            // 打印响应状态
            System.out.println(response.getStatusLine());

            // 获取响应实体
            HttpEntity entity = response.getEntity();
            result = getResult(entity);
        } catch (Exception e) {
            result = "put 请求异常:" + e.getMessage();
        } finally {
            httpResponseClose(response);
            httpAbort(httpPut);
            httpClientClose(httpClient);
        }
        return result;
    }

    public String doDelete(String url) {
        return doDelete(url, null);
    }

    public String doDelete(String url, Map<String, String> headers) {
        return doDelete(url, null, null, headers);
    }

    public String doDelete(String url, String type, Map<String, String> params, Map<String, String> headers) {
        String result = "";
        CloseableHttpClient httpClient = null;
        HttpDelete httpDelete = null;
        CloseableHttpResponse response = null;

        try {
            httpClient = createSSLClientDefault();
            // 创建httpDelete
            httpDelete = new HttpDelete(url);

            // 设置头消息
            setHeader(httpDelete, headers);

            // 设置delete参数


            // 执行delete请求.
            response = httpClient.execute(httpDelete);

            // 打印响应状态
            System.out.println(response.getStatusLine());

            // 获取响应实体
            HttpEntity entity = response.getEntity();
            result = getResult(entity);
        } catch (Exception e) {
            result = "delete 请求异常:" + e.getMessage();
        } finally {
            httpResponseClose(response);
            httpAbort(httpDelete);
            httpClientClose(httpClient);
        }

        return result;
    }

    // 不需要导入证书，SSL信任所有证书，使用该方法
    public CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                    null, new TrustStrategy() {
                        // 信任所有证书
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) throws CertificateException {
                            return true;
                        }
                    }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();

        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        return HttpClients.createDefault();
    }


    // 设置头消息
    private void setHeader(HttpRequest request, Map<String, String> headers) {
        if (null == headers || headers.isEmpty()) {
            return;
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }

    }

    // 设置参数
    private String setparams(String url, Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return url;
        }

        StringBuffer urlSb = new StringBuffer();
        urlSb.append(url);
        if (!url.contains("?")) {
            urlSb.append("?");
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlSb.append(entry.getKey());
            urlSb.append("=");
            urlSb.append(entry.getValue());
            urlSb.append("&");
        }

        return urlSb.toString();
    }


    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    private void setPostParamsEntity(HttpPost httpPost, Map<String, String> params, String type) {
        if (null == params || params.isEmpty()) {
            return;
        }

        if (StringUtils.equals("json", type)) {
            // 发送json 参数   "application/json;charset=UTF-8"
            setPostJsonParamsEntity(httpPost, JacksonUtil.obj2JsonBySafe(params));
        } else {
            // 发送标准的 URL编码后的 form 参数  "application/x-www-form-urlencoded"
            List<NameValuePair> formParam = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formParam.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParam, Consts.UTF_8);
            httpPost.setEntity(formEntity);
        }
    }

    private void setPostJsonParamsEntity(HttpPost httpPost, String jsonParams) {
        if (StringUtils.isEmpty(jsonParams)) {
            return;
        }

        // 发送json 参数   "application/json;charset=UTF-8"
        StringEntity stringEntity = new StringEntity(jsonParams, Consts.UTF_8);//解决中文乱码问题
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
    }

    private void setPostFormParamsEntity(HttpPost httpPost, String urlParams) {
        if (StringUtils.isEmpty(urlParams)) {
            return;
        }

        String[] params = urlParams.split("&");

        // 发送标准的 URL编码后的 form 参数  "application/x-www-form-urlencoded"
        List<NameValuePair> formParam = new ArrayList<NameValuePair>();
        for (String param:params){
            String[] item = param.split("=");
            formParam.add(new BasicNameValuePair(item[0], item[1]));
        }


        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParam, Consts.UTF_8);
        httpPost.setEntity(formEntity);
    }

    private void setPutParamsEntity(HttpPut httpPut, Map<String, String> params, String type) {
        if (null == params) {
            return;
        }

        if (StringUtils.equals("json", type)) {
            // 发送json 参数   "application/json;charset=UTF-8"
            StringEntity stringEntity = new StringEntity(JacksonUtil.obj2JsonBySafe(params), Consts.UTF_8);//解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPut.setEntity(stringEntity);
        } else {
            // 发送标准的 URL编码后的 form 参数  "application/x-www-form-urlencoded"
            List<NameValuePair> formParam = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formParam.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParam, Consts.UTF_8);
            httpPut.setEntity(formEntity);
        }
    }


    public String getResult(HttpEntity entity) {
        String result = "";
        try {
            if (null != entity) {
                // 打印响应内容长度
                // System.out.println(entity.getContentLength());
                result = EntityUtils.toString(entity);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    public void httpResponseClose(CloseableHttpResponse response) {
        try {
            if (null != response) {
                response.close();
            }
        } catch (IOException e) {
            response = null;
        }
    }

    public void httpAbort(HttpUriRequest request) {
        if (null != request) {
            request.abort();
        }
    }


    public void httpClientClose(CloseableHttpClient httpClient) {
        try {
            if (null != httpClient) {
                httpClient.close();
            }
        } catch (IOException e) {
            httpClient = null;
        }
    }

    public byte[] readByte(InputStream in, String charset) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }


        byte[] b = out.toByteArray();

        return b;
    }

}

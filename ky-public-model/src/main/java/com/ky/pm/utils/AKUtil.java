package com.ky.pm.utils;



import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yl on 2016/12/15.
 */
public class AKUtil {


    /**
     * 计算请求的AK
     *
     * @param method          请求方法  GET、POST、PUT、DELETE
     * @param accessKeyId     ak ID
     * @param accessKeySecret ak 密钥
     * @param signatureMethod 加密方法
     * @param version         加密版本
     * @return 加密串
     */
    public static String SignAK(String method, String accessKeyId, String accessKeySecret, String signatureMethod, String version) {
        //签名头部为KEY和Secret
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("AccessKeyId", accessKeyId);
        parameters.put("SignatureMethod", signatureMethod);
        parameters.put("Version", version);

        return simpleSignRequest(method, parameters, accessKeySecret);
    }

    private static String simpleSignRequest(String method, Map<String, String> parameters, String accessKeySecret) {
        String signature = null;
        try {
            String e = composeStringToSign(method.toUpperCase(), parameters);
            System.out.println(e);
            if ("HMAC-SHA256".equals(parameters.get("SignatureMethod"))) {
                signature = signSHA256(e, accessKeySecret + "&");
            } else if ("HMAC-SHA1".equals(parameters.get("SignatureMethod"))) {
                signature = signSHA1(e, accessKeySecret + "&");
            }
        } catch (InvalidKeyException e1) {
            e1.printStackTrace();
        }

        return signature;
    }

    public static String composeStringToSign(String method, Map<String, String> queries) {
        StringBuilder result = new StringBuilder();

        try {
            String[] sortedKeys = queries.keySet().toArray(new String[0]);
            Arrays.sort(sortedKeys);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sortedKeys.length; ++i) {
                String key = sortedKeys[i];
                sb.append("&").append(percentEncode(key)).append("=").append(percentEncode(queries.get(key)));
            }


            result.append(method);
            result.append("&");
            result.append(percentEncode("/"));
            result.append("&");
            result.append(percentEncode(sb.toString().substring(1)));

            return result.toString();
        } catch (UnsupportedEncodingException var13) {
            throw new RuntimeException("UTF-8 encoding is not supported.");
        }
    }


    public static String signSHA256(String source, String accessSecret) throws InvalidKeyException, IllegalStateException {
        try {
            Mac e = Mac.getInstance("HmacSHA256");
            e.init(new SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = e.doFinal(source.getBytes("UTF-8"));
            return B64Util.encode(signData);
        } catch (NoSuchAlgorithmException var5) {
            throw new RuntimeException("HMAC-SHA1 not supported.");
        } catch (UnsupportedEncodingException var6) {
            throw new RuntimeException("UTF-8 not supported.");
        }
    }

    public static String signSHA1(String source, String accessSecret) throws InvalidKeyException, IllegalStateException {
        try {
            Mac e = Mac.getInstance("HmacSHA1");
            e.init(new SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
            byte[] signData = e.doFinal(source.getBytes("UTF-8"));
            return B64Util.encode(signData);
        } catch (NoSuchAlgorithmException var5) {
            throw new RuntimeException("HMAC-SHA1 not supported.");
        } catch (UnsupportedEncodingException var6) {
            throw new RuntimeException("UTF-8 not supported.");
        }
    }

    public static String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }

    public static String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~") : null;
    }

}

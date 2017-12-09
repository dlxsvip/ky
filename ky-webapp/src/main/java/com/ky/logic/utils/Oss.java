package com.ky.logic.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import com.ky.logic.model.SMGeneratePresignedUrlRequest;
import com.ky.logic.model.SMObjectMetadata;
import com.ky.logic.model.SMPutObjectResult;
import com.ky.logic.model.SMResponseHeaderOverrides;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;


/**
 * Created by yl on 2017/7/11.
 */
public class Oss {

    private OSSClient _OssClient;

    /**
     * 构造函数，创建OSS客户端
     *
     * @return
     */
    public OSSClient get_OssClient() {
        return this._OssClient;
    }

    public Oss(OSSClient _OssClient) {
        this._OssClient = _OssClient;
    }

    public Oss(String endpoint, String accessKeyId, String accessKeySecret) {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSupportCname(false);
        clientConfiguration.setSLDEnabled(false);
        // 创建OSS客户端
        this._OssClient = new OSSClient(endpoint, accessKeyId, accessKeySecret, clientConfiguration);
    }

    // 创建 可以删除资源的 OSS 客户端
    public Oss(String endpoint, String accessKeyId, String accessKeySecret, Boolean delete) {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        if (delete) {
            // 可以删除资源
            clientConfiguration.setSupportCname(true);
            clientConfiguration.setSLDEnabled(true);
        } else {
            clientConfiguration.setSupportCname(false);
            clientConfiguration.setSLDEnabled(false);
        }

        this._OssClient = new OSSClient(endpoint, accessKeyId, accessKeySecret, clientConfiguration);
    }

    // 创建 自定义配置的 OSS 客户端
    public Oss(String endpoint, String accessKeyId, String accessKeySecret, ClientConfiguration clientConfiguration) {

        this._OssClient = new OSSClient(endpoint, accessKeyId, accessKeySecret, clientConfiguration);
    }

    /**
     * 上传指定的数据到OSS中指定的OSS。
     * 1.如果用户上传了Content-MD5请求头，OSS会计算body的Content-MD5并检查一致性，如果不一致，将返回InvalidDigest错误码。
     * 2.如果请求头中的“Content-Length”值小于实际请求体（body）中传输的数据长度，OSS仍将成功创建文件；但Object大小只等于“Content-Length”中定义的大小，其他数据将被丢弃。
     * 3.如果试图添加的Object的同名文件已经存在，并且有访问权限。新添加的文件将覆盖原来的文件，成功返回200 OK。
     * 4.如果在PutObject的时候，携带以x-oss-meta-为前缀的参数，则视为user meta，比如x-oss-meta-location。一个Object可以有多个类似的参数，但所有的user meta总大小不能超过2k。
     * 5.如果Head中没有加入Content length参数，会返回411 Length Required错误。错误码：MissingContentLength。
     * 6.如果设定了长度，但是没有发送消息Body，或者发送的body大小小于给定大小，服务器会一直等待，直到time out，返回400 Bad Request消息。错误码：RequestTimeout。此时OSS上的这个文件内容是用户已经上传完的数据。
     * 7.如果试图添加的Object所在的Bucket不存在，返回404 Not Found错误。错误码：NoSuchBucket。
     * 8.如果试图添加的Object所在的Bucket没有访问权限，返回403 Forbidden错误。错误码：AccessDenied。
     * 9.如果添加文件长度超过5G，返回错误消息400 Bad Request。错误码：InvalidArgument。
     * 10.如果传入的Object key长度大于1023，返回400 Bad Request。错误码：InvalidObjectName。
     * 11.PUT一个Object的时候，OSS支持4个 HTTP RFC2616协议规定的Header 字段：Cache-Control、Expires、Content-Encoding、Content-Disposition。如果上传Object时设置了这些Header，则这个Object被下载时，相应的Header值会被自动设置成上传时的值。
     * 12.如果上传Object时指定了x-oss-server-side-encryption Header，则必须设置其值为AES256，否则会返回400和相应错误提示：InvalidEncryptionAlgorithmError。指定该Header后，在响应头中也会返回该Header，OSS会对上传的Object进行加密编码存储，当这个Object被下载时，响应头中会包含x-oss-server-side-encryption，值被设置成该Object的加密算法。
     *
     * @param bucketName Bucket名称。
     * @param key        object的key。
     * @param input      输入流。
     * @param metadata   object的元信息{@link SMObjectMetadata}，若该元信息未包含Content-Length，
     *                   则采用chunked编码传输请求数据。
     * @return 请求结果{@link SMPutObjectResult}实例。
     */
    public SMPutObjectResult putObject(String bucketName, String key, InputStream input, SMObjectMetadata metadata) throws OSSException, ClientException {
        ObjectMetadata aliMeta = new ObjectMetadata();
        aliMeta.setContentLength(metadata.getContentLength());


        PutObjectResult aliResult = this._OssClient.putObject(bucketName, key, input, aliMeta);
        SMPutObjectResult smResult = new SMPutObjectResult();
        smResult.seteTag(aliResult.getETag());

        // 关闭client
        _OssClient.shutdown();

        return smResult;
    }

    /**
     * 上传字符串
     *
     * @param bucketName oss 名称
     * @param key        唯一名称
     * @param str        字符串
     */
    public PutObjectResult putObject(String bucketName, String key, String str) {
        PutObjectResult result = _OssClient.putObject(bucketName, key, new ByteArrayInputStream(str.getBytes()));

        // 关闭client
        _OssClient.shutdown();

        return result;
    }


    /**
     * 上传网络流
     *
     * @param bucketName oss 名称
     * @param key        唯一名称
     * @param url        网络地址
     */
    public PutObjectResult putObjectByUrl(String bucketName, String key, String url) {
        try {
            InputStream inputStream = new URL(url).openStream();
            return putObject(bucketName, key, inputStream);
        } catch (IOException e) {
            return null;
        }

    }

    /**
     * 上传文件流
     *
     * @param bucketName oss 名称
     * @param key        唯一名称
     * @param input      输入流
     */
    public PutObjectResult putObject(String bucketName, String key, InputStream input) {
        PutObjectResult result = _OssClient.putObject(bucketName, key, input);

        // 关闭client
        _OssClient.shutdown();

        return result;
    }


    /**
     * 上传本地文件
     *
     * @param bucketName oss 名称
     * @param key        唯一名称
     * @param file       本地文件
     */
    public PutObjectResult putObject(String bucketName, String key, File file) {
        PutObjectResult result = _OssClient.putObject(bucketName, key, file);

        // 关闭client
        _OssClient.shutdown();

        return result;
    }


    /**
     * 生成一个包含签名信息并可以访问 数据 的URL。
     *
     * @param request {@link SMGeneratePresignedUrlRequest}对象。
     * @return 包含签名信息并可以访问{@link SMOSSObject}的URL。
     * @throws ClientException
     */
    public URL generatePresignedUrl(SMGeneratePresignedUrlRequest request) {
        URL url = null;
        GeneratePresignedUrlRequest aliRequest = new GeneratePresignedUrlRequest(request.getBucketName(), request.getKey());
        aliRequest.setContentMD5(request.getContentMD5());
        aliRequest.setContentType(request.getContentType());
        aliRequest.setExpiration(request.getExpiration());
        aliRequest.setQueryParameter(request.getQueryParam());
        aliRequest.setUserMetadata(request.getUserMetadata());

        HttpMethod aliMethod = HttpMethod.GET;
        switch (request.getMethod()) {
            case DELETE:
                aliMethod = HttpMethod.DELETE;
                break;
            case GET:
                aliMethod = HttpMethod.GET;
                break;
            case HEAD:
                aliMethod = HttpMethod.HEAD;
                break;
            case POST:
                aliMethod = HttpMethod.POST;
                break;
            case PUT:
                aliMethod = HttpMethod.PUT;
                break;
            case OPTIONS:
                aliMethod = HttpMethod.OPTIONS;
        }
        aliRequest.setMethod(aliMethod);

        ResponseHeaderOverrides aliRHO = new ResponseHeaderOverrides();
        SMResponseHeaderOverrides smRHO = request.getResponseHeaders();
        aliRHO.setCacheControl(smRHO.getCacheControl());
        aliRHO.setContentDisposition(smRHO.getContentDisposition());
        aliRHO.setContentEncoding(smRHO.getContentEncoding());
        aliRHO.setContentLangauge(smRHO.getContentLangauge());
        aliRHO.setContentType(smRHO.getContentType());
        aliRHO.setExpires(smRHO.getExpires());
        aliRequest.setResponseHeaders(aliRHO);

        try {
            url = this._OssClient.generatePresignedUrl(aliRequest);
        } catch (OSSException e) {
            // do nothing
        } catch (ClientException e) {
            // do nothing
        }

        return url;
    }


    /**
     * 删除单个文件
     *
     * @param bucketName oss名称
     * @param key        文件名
     */
    public void deleteObject(String bucketName, String key) {
        GenericRequest request = new GenericRequest(bucketName, key);
        this._OssClient.deleteObject(request);
    }

    /**
     * 删除多个单个文件
     *
     * @param bucketName oss名称
     * @param keys       文件名列表
     * @return
     */
    public List<String> deleteObjects(String bucketName, List<String> keys) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        deleteObjectsRequest.setKeys(keys);
        DeleteObjectsResult deleteObjectsResult = this._OssClient.deleteObjects(deleteObjectsRequest);
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();

        return deletedObjects;
    }

    /**
     * 删除文件
     * 1.DeleteObject要求对该Object要有写权限。
     * 2.如果要删除的Object不存在，OSS也返回状态码204（ No Content）。
     * 3.如果Bucket不存在，返回404 Not Found。
     */
    public void deleteFile(String bucketName, String key) {
        try {
            this._OssClient.deleteObject(bucketName, key);
        } catch (OSSException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 下载到本地文件
     *
     * @param bucketName oss名称
     * @param key        唯一名称
     * @param filePath   本地路径
     * @return 结果
     */
    public ObjectMetadata getObject(String bucketName, String key, String filePath) {
        return getObject(bucketName, key, new File(filePath));
    }

    /**
     * 下载到本地文件
     *
     * @param bucketName oss名称
     * @param key        唯一名称
     * @param file       文件
     * @return 结果
     */
    public ObjectMetadata getObject(String bucketName, String key, File file) {
        ObjectMetadata objectMetadata = _OssClient.getObject(new GetObjectRequest(bucketName, key), file);
        _OssClient.shutdown();

        return objectMetadata;
    }
}

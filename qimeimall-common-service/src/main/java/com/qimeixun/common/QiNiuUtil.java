package com.qimeixun.common;

import com.google.gson.Gson;
import com.qimeixun.constant.SystemConfigConstants;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.service.SysConfigService;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/322:01
 */

@Component
public class QiNiuUtil {


    @Resource
    SysConfigService sysConfigService;


    public Map uploadImgFile(String name, MultipartFile file) {
        try {
            //构造一个带指定 Region 对象的配置类
            Configuration cfg = new Configuration(Region.region0());
            //...其他参数参考类注释
            UploadManager uploadManager = new UploadManager(cfg);
            //...生成上传凭证，然后准备上传
            String accessKey = sysConfigService.getSysConfigValueFromRedis(SystemConfigConstants.QINIU_APP_KEY);
            String secretKey = sysConfigService.getSysConfigValueFromRedis(SystemConfigConstants.QINIU_APP_SECRET);
            String bucket = sysConfigService.getSysConfigValueFromRedis(SystemConfigConstants.QINIU_BUCKET_NAME);
            String endpoint = sysConfigService.getSysConfigValueFromRedis(SystemConfigConstants.QINIU_DOMAIN);
            //默认不指定key的情况下，以文件内容的hash值作为文件名
            String key = null;

            byte[] uploadBytes = file.getBytes();
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            HashMap<Object, Object> hashMap = new HashMap<>();
            hashMap.put("url", endpoint + "/" + putRet.key);
            return hashMap;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("上传失败");
        }
    }

//    public String getFileDomain() {
//        return fileDomain;
//    }
//
//    private UploadManager uploadManager;
//    private BucketManager bucketManager;
//    private Configuration c;
//    private Client client;
//    // 密钥配置
//    private Auth auth;
//
//    public Client getClient(){
//        if (client==null) {
//            client=new Client(getConfiguration());
//        }
//        return client;
//    }
//
//    public BucketManager getBucketManager() {
//        if (bucketManager == null) {
//            bucketManager = new BucketManager(getAuth(), getConfiguration());
//        }
//        return bucketManager;
//    }
//
//    public UploadManager getUploadManager() {
//        if (uploadManager == null) {
//            uploadManager = new UploadManager(getConfiguration());
//        }
//        return uploadManager;
//    }
//
//    public Configuration getConfiguration() {
//        if (c == null) {
//            Zone z = Zone.autoZone();
//            c = new Configuration(z);
//        }
//        return c;
//    }
//
//    public Auth getAuth() {
//        if (auth == null) {
//            auth = Auth.create(getAccessKey(), getSecretKey());
//        }
//        return auth;
//    }
//    //简单上传模式的凭证
//    public String getUpToken() {
//        return getAuth().uploadToken(getBucketName());
//    }
//    //覆盖上传模式的凭证
//    public String getUpToken(String fileKey) {
//        return getAuth().uploadToken(getBucketName(), fileKey);
//    }
//
//    /**
//     * 将本地文件上传
//     * @param filePath 本地文件路径
//     * @param fileKey 上传到七牛后保存的文件路径名称
//     * @return
//     * @throws IOException
//     */
//    public String upload(String filePath, String fileKey) throws IOException {
//        Response res;
//        try {
//            res = getUploadManager().put(filePath, fileKey, getUpToken(fileKey));
//            // 解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(res.bodyString(), DefaultPutRet.class);
//            return fileDomain + "/" + putRet.key;
//        } catch (QiniuException e) {
//            res = e.response;
//            e.printStackTrace();
//            return "上传失败";
//        }
//    }
//
//    /**
//     * 上传二进制数据
//     * @param data
//     * @param fileKey
//     * @return
//     * @throws IOException
//     */
//    public String upload(byte[] data, String fileKey) throws IOException {
//        Response res;
//        try {
//            res = getUploadManager().put(data, fileKey, getUpToken(fileKey));
//            // 解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(res.bodyString(), DefaultPutRet.class);
//            return fileDomain + "/" + putRet.key;
//        } catch (QiniuException e) {
//            res = e.response;
//            e.printStackTrace();
//            return "上传失败";
//        }
//    }
//
//    /**
//     * 上传输入流
//     * @param inputStream
//     * @param fileKey
//     * @return
//     * @throws IOException
//     */
//    public String upload(InputStream inputStream, String fileKey) throws IOException {
//        Response res;
//        try {
//            res = getUploadManager().put(inputStream, fileKey, getUpToken(fileKey),null,null);
//            // 解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(res.bodyString(), DefaultPutRet.class);
//            return fileDomain + "/" + putRet.key;
//        } catch (QiniuException e) {
//            res = e.response;
//            e.printStackTrace();
//            return "上传失败";
//        }
//    }
//
//    /**
//     * 删除文件
//     * @param fileKey
//     * @return
//     * @throws QiniuException
//     */
//    public boolean delete(String fileKey) throws QiniuException {
//        Response response = bucketManager.delete(this.getBucketName(), fileKey);
//        return response.statusCode == 200 ? true:false;
//    }
//
//    /**
//     * 获取公共空间文件
//     * @param fileKey
//     * @return
//     */
//    public String getFile(String fileKey) throws Exception{
//        String encodedFileName = URLEncoder.encode(fileKey, "utf-8").replace("+", "%20");
//        String url = String.format("%s/%s", fileDomain, encodedFileName);
//        return url;
//    }
//
//    /**
//     * 获取私有空间文件
//     * @param fileKey
//     * @return
//     */
//    public String getPrivateFile(String fileKey) throws Exception{
//        String encodedFileName = URLEncoder.encode(fileKey, "utf-8").replace("+", "%20");
//        String url = String.format("%s/%s", fileDomain, encodedFileName);
//        Auth auth = Auth.create(accessKey, secretKey);
//        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
//        String finalUrl = auth.privateDownloadUrl(url, expireInSeconds);
//        return finalUrl;
//    }
}

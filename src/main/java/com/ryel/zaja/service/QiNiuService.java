package com.ryel.zaja.service;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;


/**
 * Created by billyu on 2016/12/14.
 */
@Service
public class QiNiuService {

    //设置好账号的ACCESS_KEY和SECRET_KEY
    public final static String ACCESS_KEY = "evBnv6eykpwWmQGm5Tdo4rKXJBXz17LzP61VVQCL";
    public final static String SECRET_KEY = "bRKFlpavXFQ6dBKa1omZScElSGGpH5TMa9KGQsy1";
    //要上传的空间
    public final static String bucketname = "zaja";
    //上传到七牛后保存的文件名

    //上传文件的路径
    //String FilePath = "/.../...";

    public final String domain = "http://oi0y2qwer.bkt.clouddn.com/";


    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    Zone z = Zone.zone0();

    Configuration c = new Configuration(z);

    UploadManager uploadManager = new UploadManager(c);

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String  upload(String FilePath, String key) throws IOException {


        try {
            //调用put方法上传
            Response res = uploadManager.put(FilePath, key, getUpToken());
            //打印返回的信息
            System.out.println(res.bodyString());
            String bodyString = res.bodyString().toString();

            return bodyString;

        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return null;
    }

    public String getFileName(){
        StringBuffer name = new StringBuffer();

        UUID uuid = UUID.randomUUID();
        String UUIDString = uuid.toString();
        name.append(UUIDString);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        name.append(timestamp.getTime());

        name.append(".jpg");

        return name.toString();
    }



    public  String getDomain() {
        return domain;
    }
}



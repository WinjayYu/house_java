package com.ryel.zaja.utils;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by billyu on 2016/12/8.
 */
public class VerifyCodeUtil {

    public static String send(String mobile, String verCode, String type) {
        String msg = "";
        if("1".equals(type)){
            msg = "您正在绑定手机号，验证码：";
        }else{
            msg = "您正在修改密码，验证码：";
        }
        // just replace key here
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(
                "api", "key-312d01f92a34086bbcc57f31047578b4"));
        WebResource webResource = client.resource(
                "http://sms-api.luosimao.com/v1/send.json");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("mobile", mobile);
        formData.add("message",  msg + verCode + ", 5分钟内有效. 【ZAJA】");
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
        String textEntity = response.getEntity(String.class);
        int status = response.getStatus();
        //System.out.print(textEntity);
        //System.out.print(status);
        return textEntity;
    }

    public static String sendTip(String mobile, String msg,String type) {
        //经纪人审核成功
        if("0".equals(type)){
            msg = "您的经纪人申请信息已经通过，尽快登录，开启经纪人之旅。";
        }else if("1".equals(type)){
            //经纪人审核失败
            msg = "感谢您申请【ZAJA】经纪人，" + msg + "，期待您的再次提交。";
        }else{
            //房源审核失败
            msg = "感谢您提交宝贵的房源信息，" + msg + ",期待您的再次提交。";
        }
        // just replace key here
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(
                "api", "key-312d01f92a34086bbcc57f31047578b4"));
        WebResource webResource = client.resource(
                "http://sms-api.luosimao.com/v1/send.json");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("mobile", mobile);
        formData.add("message",  msg + "【ZAJA】");
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
        String textEntity = response.getEntity(String.class);
        int status = response.getStatus();
        //System.out.print(textEntity);
        //System.out.print(status);
        return textEntity;
    }

    public static String testStatus() {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(
                "api", "key-312d01f92a34086bbcc57f31047578b4"));
        WebResource webResource = client.resource("http://sms-api.luosimao.com/v1/status.json");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        ClientResponse response = webResource.get(ClientResponse.class);
        String textEntity = response.getEntity(String.class);
        int status = response.getStatus();
        //System.out.print(status);
        //System.out.print(textEntity);
        return textEntity;
    }

    public static String getVerCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(new Random().nextInt(10));
        }
        return sb.toString();
    }
}

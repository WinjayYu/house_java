package com.ryel.zaja.utils;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.net.URLEncoder;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

/**
 * Created by billyu on 2016/12/8.
 */
public class VerifyCodeUtil {

    private static final String address = "120.77.15.48";//远程地址：不带http://;
    private static final int port = 7862;//远程端口
    private static final String account = "833066";
    private static final String token = "H28k3z";
    private static final String extno= "10690066";//接入号


    public static String send(String mobile, String verCode, String type){
        String msg = "";
        if("1".equals(type)){
            msg = "【ZAJA】您正在绑定手机号，验证码：";
        }else{
            msg = "【ZAJA】您正在修改密码，验证码：";
        }

        msg = msg + verCode + ", 5分钟内有效";

        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        }catch (Exception e){

        }

        String uriStr = MessageFormat.format("http://{0}:{1}/sms", address,String.valueOf(port));
        return getResponse(uriStr,MessageFormat.format(
                "action=send&account={0}&password={1}&mobile={2}&content={3}&extno={4}",account, token, mobile, msg,extno));

    }

    /**
     * 获取请求数据
     *
     * @param uriStr
     * 			请求路径
     * @param param
     * 			请求参数
     * @return
     * */
    private static String getResponse(String uriStr, String param)
    {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(uriStr);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setConnectTimeout(50000);
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("connection","Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String sendTip(String mobile, String msg,String type) {
        //经纪人审核成功
        if("0".equals(type)){
            msg = "【ZAJA】您的经纪人申请信息已经通过，尽快登录，开启经纪人之旅。";
        }else if("1".equals(type)){
            //经纪人审核失败
            msg = "【ZAJA】感谢您申请经纪人，" + msg + "，期待您的再次提交。";
        }else{
            //房源审核失败
            msg = "【ZAJA】感谢您提交宝贵的房源信息，" + msg + ",期待您的再次提交。";
        }

        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        }catch (Exception e){

        }

        String uriStr = MessageFormat.format("http://{0}:{1}/sms", address,String.valueOf(port));
        return getResponse(uriStr,MessageFormat.format(
                "action=send&account={0}&password={1}&mobile={2}&content={3}&extno={4}",account, token, mobile, msg,extno));
    }


    /*public static String send(String mobile, String verCode, String type) {
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
    }*/

    public static String getVerCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
        }
        return sb.toString();
    }
}

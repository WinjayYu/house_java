package com.ryel.zaja.fillter;

import org.apache.commons.codec.binary.Hex;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.sql.Timestamp;

/**
 * Created by billyu on 2017/1/3.
 */
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        System.out.println(">>>MyInterceptor1>>>>>>>在请求处理之前进行调用（Controller方法调用之前）");

//        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        String KEY = httpServletRequest.getHeader("KEY");
        String TOKEN = httpServletRequest.getHeader("TOKEN");
        if(null == KEY || null == TOKEN){
            return false;
        }
        Long subTime = Long.parseLong(KEY) - 5;
        String timestamp = new StringBuffer(subTime.toString()).reverse().toString();
        Long currentTime = new Timestamp(System.currentTimeMillis()).getTime();
        long distance = (new Timestamp(System.currentTimeMillis())).getTime() - Long.parseLong(timestamp);
        if(distance > 600000){
            return false;
        }
        String result = timestamp + "ZAJA";

//        byte[] bytesOfMessage = result.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(result.getBytes());
        byte byteData[] = md.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<byteData.length;i++) {
            String hex=Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        String serverToken = hexString.toString().toUpperCase();

//        String serverToken = Hex.encodeHexString(byteData);

        if(!TOKEN.equals(serverToken)){
            return false;
        }

        return true;// 只有返回true才会继续向下执行，返回false取消当前请求
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//        System.out.println(">>>MyInterceptor1>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//
//        System.out.println(">>>MyInterceptor1>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");

    }
}

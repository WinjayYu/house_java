package com.ryel.zaja.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class BizUtil {

    public static String getOrderCode() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(new Date());
        int x = (int) (Math.random() * 900) + 100;
        return "DD" + dateString + x;
    }

    public static void main(String[] args) {
        System.out.println(getOrderCode());
    }
}


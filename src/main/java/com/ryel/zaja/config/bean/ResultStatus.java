package com.ryel.zaja.config.bean;

/**
 * Author: koabs
 * 8/22/16.
 */

//三种状态，1表示error, 0表示成功，-1表示用户未登录
public class ResultStatus {
    // 用户未登入
    public static Integer Not_Login = -1;

    public static Integer Error = 1;

    public static Integer Success = 0;

}

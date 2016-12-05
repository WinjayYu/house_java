package com.ryel.zaja.config;

/**
 * Created by billyu on 2016/12/3.
 */
public interface Error_code {
    String ERROR_CODE_0001 = "error_01";  //服务器异常

    String ERROR_CODE_0002 = "error_02";  //参数错误

    String ERROR_CODE_0003 = "error_03";  //用户不存在

    String ERROR_CODE_0004 = "error_04";  //用户名或密码错误

    String ERROR_CODE_0005 = "error_05";  //用户被禁用

    String ERROR_CODE_0006 = "error_06";  //手机号被占用

    String ERROR_CODE_0007 = "error_07";  //昵称被占用

    String ERROR_CODE_0008 = "error_08";  //验证码错误

    String ERROR_CODE_0009 = "error_09";  //验证码不正确

    String ERROR_CODE_0010 = "error_10";  //验证码已过期，请重新发送验证码

    String ERROR_CODE_0011 = "error_11";  //原密码错误

    String ERROR_CODE_0012 = "error_12";  //手机号未注册用户

    String ERROR_CODE_0013 = "error_13";  //已经关注过

    String ERROR_CODE_0014 = "error_14";  //没有数据

    String ERROR_CODE_0015 = "error_15";  //当前用户已经绑定此第三方

    String ERROR_CODE_0016 = "error_16";  //没有收藏记录

    String ERROR_CODE_0017 = "error_17";  //手机号不存在

    String ERROR_CODE_0018 = "error_18";  //不能绑定已经登录过的第三方账号

    String ERROR_CODE_0019 = "error_19";  //操作失败

}

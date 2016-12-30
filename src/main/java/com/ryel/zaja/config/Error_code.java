package com.ryel.zaja.config;

/**
 * Created by billyu on 2016/12/3.
 */
public interface Error_code {
    String ERROR_CODE_0001 = "error_01";  //【通用】服务器异常

    String ERROR_CODE_0002 = "error_02";  //【通用】参数错误

    String ERROR_CODE_0003 = "error_03";  //【通用】用户不存在

    String ERROR_CODE_0004 = "error_04";  //【登录】用户名或密码错误

    String ERROR_CODE_0005 = "error_05";  //【通用】用户被禁用

    String ERROR_CODE_0006 = "error_06";  //【注册】手机号被占用

    String ERROR_CODE_0007 = "error_07";  //【更改昵称】昵称被占用

    String ERROR_CODE_0008 = "error_08";  //【验证码】验证码发送失败

    String ERROR_CODE_0009 = "error_09";  //【验证码】验证码不正确

    String ERROR_CODE_0010 = "error_10";  //【验证码】验证码已过期，请重新发送验证码

    String ERROR_CODE_0011 = "error_11";  //【更改密码】原密码错误

    String ERROR_CODE_0012 = "error_12";  //【发送订单】手机号未注册用户

    String ERROR_CODE_0013 = "error_13";  //【关注】已经关注过

    String ERROR_CODE_0014 = "error_14";  //【通用】没有数据

    String ERROR_CODE_0015 = "error_15";  //【绑定第三方】当前用户已经绑定此第三方

    String ERROR_CODE_0016 = "error_16";  //【收藏】没有收藏记录

    String ERROR_CODE_0017 = "error_17";  //【通用】手机号不存在

    String ERROR_CODE_0018 = "error_18";  //【绑定第三方】不能绑定已经登录过的第三方账号

    String ERROR_CODE_0019 = "error_19";  //【通用】操作失败

    String ERROR_CODE_0020 = "error_20";  //【搜索】此小区内没有房源

    String ERROR_CODE_0021 = "error_21";  //【收藏】您已收藏过

    String ERROR_CODE_0022 = "error_22";  // 【登陆】登录时，用户名或密码为空

    String ERROR_CODE_0023 = "error_23";  // 【通用】必填参数为空或参数错误

    String ERROR_CODE_0024 = "error_24";  // 【注册】用户已经存在

    String ERROR_CODE_0025 = "error_25";  // 【通用】数据异常，请刷新后重试

    String ERROR_CODE_0026 = "error_26";   // 【经纪人发布房源】发布房源时，房源已经存在已支付的订单

    String ERROR_CODE_0027 = "error_27";   // 【经纪人注册】经济人注册时，身份证号已经存在了

    String ERROR_CODE_0028 = "error_28";   // 【经纪人发布房源】经济人已经发布过房源，不能重复发布

    String ERROR_CODE_0029 = "error_29";   // 【登录】该账号不是经纪人账号

    String ERROR_CODE_0030 = "error_30";   // 【注册】该手机号已经绑定相应的身份证信息

    String ERROR_CODE_0031 = "error_31";   // 【注册】该手机号为用户账号，请走用户申请流程

    String ERROR_CODE_0032 = "error_32";   // 【接单】您已接过此单，不能重复接单

    String ERROR_CODE_0033 = "error_33";   // 【接单】不能接收自己发布的订单

    String ERROR_CODE_0034 = "error_34";   //  【订单流程】非法操作

    String ERROR_CODE_0035 = "error_35";   //  【删除房源】经纪人与房屋信息不匹配


}

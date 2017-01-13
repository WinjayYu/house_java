package com.ryel.zaja.config;

/**
 * Created by billyu on 2016/12/3.
 */
public interface Error_code {
    String ERROR_CODE_0001 = "error_01";  //【通用】服务器异常

    String ERROR_CODE_0004 = "error_04";  //【登录】用户名或密码错误

    String ERROR_CODE_0006 = "error_06";  //【注册】手机号被占用

    String ERROR_CODE_0008 = "error_08";  //【验证码】验证码发送失败

    String ERROR_CODE_0009 = "error_09";  //【验证码】验证码不正确

    String ERROR_CODE_0010 = "error_10";  //【验证码】验证码已过期，请重新发送验证码

    String ERROR_CODE_0012 = "error_12";  //【发送订单】手机号未注册用户

    String ERROR_CODE_0019 = "error_19";  //【通用】操作失败

    String ERROR_CODE_0021 = "error_21";  //【收藏】您已收藏过

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

    String ERROR_CODE_0034 = "error_34";   //  【更改账户】新手机号码已是ZAJA账户

    String ERROR_CODE_0035 = "error_35";   //  【删除房源】经纪人与房屋信息不匹配

    String ERROR_CODE_0036 = "error_36";   //  【登录】此账号没审核通过

    String ERROR_CODE_0037 = "error_37";   //  【忘记密码和修改密码】新密码不能与原密码相同

    String ERROR_CODE_0038 = "error_38";   //  【第三方登陆】此账户已经绑定过其他第三方

    String ERROR_CODE_0039 = "error_39";   //  【申请经纪人】您已申请过

    String ERROR_CODE_0040 = "error_40";   //  【平安】创建账户失败

    String ERROR_CODE_0041 = "error_41";   //  【平安】用户id为空

    String ERROR_CODE_0042 = "error_42";   //  【平安】找不到用户信息

    String ERROR_CODE_0043 = "error_43";   //  【平安】该用户没有见证宝账户

    String ERROR_CODE_0044 = "error_44";   //  【平安】查询见证宝账户信息异常

    String ERROR_CODE_0045 = "error_45";   //  【平安】接口必填参数为空

    String ERROR_CODE_0046 = "error_46";   //  【平安】bankType错误

    String ERROR_CODE_0047 = "error_47";   //  【平安】绑定提现账户异常

    String ERROR_CODE_0048 = "error_48";   //  【平安】绑定提现账户发送验证码失败

    String ERROR_CODE_0049 = "error_49";   //  【平安】转账时交易记录不存在

    String ERROR_CODE_0050 = "error_50";   //  【平安】交易发生异常

    String ERROR_CODE_0051 = "error_51";   //  【平安】提现异常

}

package com.ryel.zaja.config.enums;

/**
 * Created by billyu on 2017/2/20.
 */

/**
 * 一些常量
 */
public enum Constant {
    AGENT_HEAD  ("https://img.zaja.xin/agent_head.png", "经纪人头像"),
    USER_HEAD   ("https://img.zaja.xin/head.jpg", "用户头像"),
    ;

    Constant(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

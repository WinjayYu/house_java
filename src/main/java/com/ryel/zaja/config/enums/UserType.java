package com.ryel.zaja.config.enums;

/**
 * Created by billyu on 2016/12/18.
 */
public enum UserType {
    USER("10","用户"),AGENT("20","经纪人"),;

    private String code;
    private String desc;

    UserType(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

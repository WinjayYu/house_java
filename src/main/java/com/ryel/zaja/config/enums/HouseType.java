package com.ryel.zaja.config.enums;

/**
 * Created by billyu on 2016/12/18.
 */
public enum HouseType {
    FIRST("10","经济人二次发布"),SECOND("20","经济人一手发布"),;

    private String code;
    private String desc;

    HouseType(String code, String desc){
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

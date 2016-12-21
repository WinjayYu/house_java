
package com.ryel.zaja.config.enums;


/**
 * Created by billyu on 2016/12/10.
 */

public enum  SellHouseStatus {
    PUBLISHED("10","发布"),

    ACCEPTED("20","经济人已接单"),

    IN_CONNECT("30","房屋交接中"),

    CLOSED("40","房屋已关闭"),;

    private String code;
    private String desc;

    SellHouseStatus(String code, String desc) {
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


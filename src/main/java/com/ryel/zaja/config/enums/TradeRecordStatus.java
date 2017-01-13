package com.ryel.zaja.config.enums;

public enum TradeRecordStatus {
    COMMON_ACCOUNT("10", "已转入担保账户"),
    IN_ACCOUNT("20", "已转入收款人账户"),
    ;

    TradeRecordStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;
    private String pinganCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
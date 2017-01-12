package com.ryel.zaja.config.enums;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum PinganApiEnum {
    CREATE_ACCOUNT("create_account", "创建见证宝账户", "6000"),
    GET_WALLET_BALANCE_INFO("get_wallet_balance_info", "查询见证宝余额信息", "6037"),
    ;

    PinganApiEnum(String code, String name, String pinganCode) {
        this.code = code;
        this.name = name;
        this.pinganCode = pinganCode;
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

    public String getPinganCode() {
        return pinganCode;
    }

    public void setPinganCode(String pinganCode) {
        this.pinganCode = pinganCode;
    }
}
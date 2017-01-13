package com.ryel.zaja.config.enums;

public enum PinganApiEnum {
    CREATE_ACCOUNT                     ("create_account",                       "创建见证宝账户",                "6000"),
    GET_WALLET_BALANCE_INFO            ("get_wallet_balance_info",              "查询见证宝余额信息",            "6037"),
    BIND_WITHDRAW_CARD                 ("bind_withdraw_card",                   "绑定提现账户",                  "6066"),
    SEND_MSG_FOR_BIND_WITHDRAW_CARD    ("send_msg_for_bind_withdraw_card",      "绑定提现账户发送验证码",       "6067"),
    OUT_ACCOUNT_TO_COMMON_ACCOUNT      ("out_account_to_common_account",        "从付款账号转账到公共账户FuncFlag=1", "6034"),
    COMMON_ACCOUNT_TO_IN_ACCOUNT       ("common_account_to_in_account",         "从公共账户账号到公共账户FuncFlag=2", "6034"),
    WITHDRAW                           ("withdraw",                             "提现",                          "6033"),
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
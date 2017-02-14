package com.ryel.zaja.config.enums;

public enum PinganApiEnum {
    CREATE_ACCOUNT                     ("create_account",                       "创建见证宝账户",                "6000"),
    GET_WALLET_BALANCE_INFO            ("get_wallet_balance_info",              "查询见证宝余额信息",             "6037"),
    BIND_WITHDRAW_CARD_SEND_MSG        ("bind_withdraw_card_send_msg",          "绑定提现账户发送验证码",          "6066"),
    BIND_WITHDRAW_CARD                 ("bind_withdraw_card",                   "绑定提现账户",                  "6067"),
    REMOVE_WITHDRAW_CARD               ("remove_withdraw_card",                 "解除提现银行卡绑定",             "6065"),
    WITHDRAW_MONEY_SEND_MSG            ("withdraw_money_send_msg",              "提现短信验证",                   "6082"),
    WITHDRAW                           ("withdraw",                             "提现",                          "6085"),
    RECHARGE_FROZEN_MONEY              ("recharge_frozen_money",                "会员清分+冻结资金",                "6007"),
    UNFROZEN_MONEY                     ("unfrozen_money",                "会员解冻资金",                "6007"),
    REFUND_COMMISSION                     ("refund_commission",             "会员清分冻结交易撤销",                "6077")
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
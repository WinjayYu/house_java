package com.ryel.zaja.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_wallet_account")
public class UserWalletAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer userId;

    // 绑定提现账户-会员证件号码
    private String idCode;

    // 绑定提现账户-会员账户（银行卡号）
    private String aCctId;

    // 绑定提现账户-银行类型 10
    private String bankType;

    // 绑定提现账户-开户行名称
    private String bankName;

    // 绑定提现账户-大小额号
    private String bankCode;

    // 绑定提现账户-银行名称
    private String sBankName;

    // 绑定提现账户-超级网银行号
    private String sBankCode;

    //10 正常 20 解除绑定
    private String status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getaCctId() {
        return aCctId;
    }

    public void setaCctId(String aCctId) {
        this.aCctId = aCctId;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getsBankName() {
        return sBankName;
    }

    public void setsBankName(String sBankName) {
        this.sBankName = sBankName;
    }

    public String getsBankCode() {
        return sBankCode;
    }

    public void setsBankCode(String sBankCode) {
        this.sBankCode = sBankCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

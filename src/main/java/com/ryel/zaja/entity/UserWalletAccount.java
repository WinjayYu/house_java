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

    // 创建账户-子账户账号
    private String CustAcctId;

    // 创建账户-交易网会员代码
    private String ThirdCustId;

    // 创建账户-会员昵称
    private String NickName;

    // 创建账户-手机号码
    private String MobilePhone;

    // 绑定提现账户-会员名称
    private String CustName;

    // 绑定提现账户-会员证件类型
    private String IdType;

    // 绑定提现账户-会员证件号码
    private String IdCode;

    // 绑定提现账户-会员账户（银行卡号）
    private String AcctId;

    // 绑定提现账户-银行类型
    private String BankType;

    // 绑定提现账户-开户行名称
    private String BankName;

    // 绑定提现账户-超级网银行号
    private String BankCode;


    public UserWalletAccount(){}

    public UserWalletAccount(Integer id){this.id = id;}

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

    public String getCustAcctId() {
        return CustAcctId;
    }

    public void setCustAcctId(String custAcctId) {
        CustAcctId = custAcctId;
    }

    public String getThirdCustId() {
        return ThirdCustId;
    }

    public void setThirdCustId(String thirdCustId) {
        ThirdCustId = thirdCustId;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getMobilePhone() {
        return MobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        MobilePhone = mobilePhone;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getIdType() {
        return IdType;
    }

    public void setIdType(String idType) {
        IdType = idType;
    }

    public String getIdCode() {
        return IdCode;
    }

    public void setIdCode(String idCode) {
        IdCode = idCode;
    }

    public String getAcctId() {
        return AcctId;
    }

    public void setAcctId(String acctId) {
        AcctId = acctId;
    }

    public String getBankType() {
        return BankType;
    }

    public void setBankType(String bankType) {
        BankType = bankType;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBankCode() {
        return BankCode;
    }

    public void setBankCode(String bankCode) {
        BankCode = bankCode;
    }
}

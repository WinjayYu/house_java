package com.ryel.zaja.entity;

import java.io.Serializable;

public class CommonAccountInfo implements Serializable {
    private static final long serialVersionUID = 1L;

//    子账户	CustAcctId	C(32)	必输	可重复
//    子账户属性	CustType	C(1)	必输	可重复（1：普通会员子账号 2：挂账子账号  3：手续费子账号 4：利息子账号5：平台担保子账号）
//    交易网会员代码	ThirdCustId	C(32)	必输	可重复
//    子账户名称	CustName	C(120)	必输	可重复
//    账户可用余额	TotalBalance	9(15)	必输	可重复
//    账户可提现金额	TotalTranOutAmount	9(15)	必输	可重复
//    维护日期	TranDate	C(8)	必输	可重复（开户日期或修改日期）

    private String CustAcctId;

    private String CustType;

    private String ThirdCustId;

    private String CustName;

    private String TotalBalance;

    private String TotalTranOutAmount;

    public String getCustAcctId() {
        return CustAcctId;
    }

    public void setCustAcctId(String custAcctId) {
        CustAcctId = custAcctId;
    }

    public String getCustType() {
        return CustType;
    }

    public void setCustType(String custType) {
        CustType = custType;
    }

    public String getThirdCustId() {
        return ThirdCustId;
    }

    public void setThirdCustId(String thirdCustId) {
        ThirdCustId = thirdCustId;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getTotalBalance() {
        return TotalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        TotalBalance = totalBalance;
    }

    public String getTotalTranOutAmount() {
        return TotalTranOutAmount;
    }

    public void setTotalTranOutAmount(String totalTranOutAmount) {
        TotalTranOutAmount = totalTranOutAmount;
    }
}

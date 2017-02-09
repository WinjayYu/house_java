package com.ryel.zaja.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ryel.zaja.utils.CustomJsonDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "trade_record")
public class TradeRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String ThirdHtId;

    private String OutCustAcctId;

    private String OutThirdCustId;

    private String OutCustName;

    private String InCustAcctId;

    private String InThirdCustId;

    private String InCustName;

    private String TranAmount;

    private String Status;

    private String OrderId;

    private String FrontLogNo;

    public String getFrontLogNo() {
        return FrontLogNo;
    }

    public void setFrontLogNo(String frontLogNo) {
        FrontLogNo = frontLogNo;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date AddTime;

    public TradeRecord(){}

    public TradeRecord(Integer id){this.id = id;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThirdHtId() {
        return ThirdHtId;
    }

    public void setThirdHtId(String thirdHtId) {
        ThirdHtId = thirdHtId;
    }

    public String getOutCustAcctId() {
        return OutCustAcctId;
    }

    public void setOutCustAcctId(String outCustAcctId) {
        OutCustAcctId = outCustAcctId;
    }

    public String getOutThirdCustId() {
        return OutThirdCustId;
    }

    public void setOutThirdCustId(String outThirdCustId) {
        OutThirdCustId = outThirdCustId;
    }

    public String getOutCustName() {
        return OutCustName;
    }

    public void setOutCustName(String outCustName) {
        OutCustName = outCustName;
    }

    public String getInCustAcctId() {
        return InCustAcctId;
    }

    public void setInCustAcctId(String inCustAcctId) {
        InCustAcctId = inCustAcctId;
    }

    public String getInThirdCustId() {
        return InThirdCustId;
    }

    public void setInThirdCustId(String inThirdCustId) {
        InThirdCustId = inThirdCustId;
    }

    public String getInCustName() {
        return InCustName;
    }

    public void setInCustName(String inCustName) {
        InCustName = inCustName;
    }

    public String getTranAmount() {
        return TranAmount;
    }

    public void setTranAmount(String tranAmount) {
        TranAmount = tranAmount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Date getAddTime() {
        return AddTime;
    }

    public void setAddTime(Date addTime) {
        AddTime = addTime;
    }
}

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

    private String thirdHtId;

    private String outCustAcctId;

    @ManyToOne
    @JoinColumn(name = "outThirdCustId")
    private User outThirdCustId;

    private String outCustName;

    private String inCustAcctId;

    @ManyToOne
    @JoinColumn(name = "inThirdCustId")
    private User inThirdCustId;

    private String inCustName;

    private String tranAmount;

    private String status;

    private Integer orderId;

    private String frontLogNo;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date addTime;

    public TradeRecord(){}

    public TradeRecord(Integer id){this.id = id;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getOutThirdCustId() {
        return outThirdCustId;
    }

    public void setOutThirdCustId(User outThirdCustId) {
        this.outThirdCustId = outThirdCustId;
    }

    public User getInThirdCustId() {
        return inThirdCustId;
    }

    public void setInThirdCustId(User inThirdCustId) {
        this.inThirdCustId = inThirdCustId;
    }

    public String getThirdHtId() {
        return thirdHtId;
    }

    public void setThirdHtId(String thirdHtId) {
        this.thirdHtId = thirdHtId;
    }

    public String getOutCustAcctId() {
        return outCustAcctId;
    }

    public void setOutCustAcctId(String outCustAcctId) {
        this.outCustAcctId = outCustAcctId;
    }

    public String getOutCustName() {
        return outCustName;
    }

    public void setOutCustName(String outCustName) {
        this.outCustName = outCustName;
    }

    public String getInCustAcctId() {
        return inCustAcctId;
    }

    public void setInCustAcctId(String inCustAcctId) {
        this.inCustAcctId = inCustAcctId;
    }

    public String getInCustName() {
        return inCustName;
    }

    public void setInCustName(String inCustName) {
        this.inCustName = inCustName;
    }

    public String getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(String tranAmount) {
        this.tranAmount = tranAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getFrontLogNo() {
        return frontLogNo;
    }

    public void setFrontLogNo(String frontLogNo) {
        this.frontLogNo = frontLogNo;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}

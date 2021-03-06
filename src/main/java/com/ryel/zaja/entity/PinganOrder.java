package com.ryel.zaja.entity;

/**
 * Created by Nathan on 2017/1/15.
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ryel.zaja.utils.CustomJsonDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 平安银行的订单流水表
 */
@Entity
@Table(name = "pingan_order")
public class PinganOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String pinganOrderId;

    private String masterId;

    private String customerId;//会员号

    private String amount;//订单金额

    private String charge;//手续费

    private String currency;//币种 RMB

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @Column(name = "orderTime")
    private Date orderTime;//下单时间 对应平安 paydate

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @Column(name = "payTime")
    private Date payTime;//支付时间 对应平安 date

    private String validtime;//订单有效时间

    private String objectName;//款项描述

    private Integer orderId;//备注字段

    private String accNo;//银行卡后4位

    private String mobile;//手机号 对应平安 telephone


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getValidtime() {
        return validtime;
    }

    public void setValidtime(String validtime) {
        this.validtime = validtime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }


    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public String getPinganOrderId() {
        return pinganOrderId;
    }

    public void setPinganOrderId(String pinganOrderId) {
        this.pinganOrderId = pinganOrderId;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


}

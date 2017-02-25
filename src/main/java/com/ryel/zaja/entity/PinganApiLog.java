package com.ryel.zaja.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ryel.zaja.utils.CustomJsonDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "pingan_api_log")
public class PinganApiLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String code;

    private String name;

    @Column(name = "pingan_code")
    private String pinganCode;

    private String request;

    private String response;

    @Column(name = "third_logno")
    private String thirdLogNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @Column(name = "add_time")
    private Date addTime;

    public PinganApiLog(){}

    public PinganApiLog(Integer id){this.id = id;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getThirdLogNo() {
        return thirdLogNo;
    }

    public void setThirdLogNo(String thirdLogNo) {
        this.thirdLogNo = thirdLogNo;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}

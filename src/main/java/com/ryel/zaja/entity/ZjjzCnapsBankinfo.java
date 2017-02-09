package com.ryel.zaja.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by billyu on 2017/2/8.
 */
@Entity
@Table(name = "zjjz_cnaps_bankinfo")
public class ZjjzCnapsBankinfo implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private String bankno;

    private String status;

    private String bankclscode;

    private String citycode;

    private String bankname;

    public String getBankno() {
        return bankno;
    }

    public void setBankno(String bankno) {
        this.bankno = bankno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBankclscode() {
        return bankclscode;
    }

    public void setBankclscode(String bankclscode) {
        this.bankclscode = bankclscode;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }
}

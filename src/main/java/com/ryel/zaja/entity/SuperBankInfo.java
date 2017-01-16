package com.ryel.zaja.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 超级网银号
 */
@Entity
@Table(name = "super_bank_info")
public class SuperBankInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String bankCode;

    private String bankName;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}

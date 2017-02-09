package com.ryel.zaja.entity.vo;

import java.io.Serializable;

/**
 * Created by billyu on 2017/2/9.
 */
public class ZjjzCnapsBankinfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bankno;

    private String bankname;

    public String getBankno() {
        return bankno;
    }

    public ZjjzCnapsBankinfoVo(String bankno, String bankname){
        this.bankno = bankno;
        this.bankname = bankname;
    }

    public void setBankno(String bankno) {
        this.bankno = bankno;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }
}

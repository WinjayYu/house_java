package com.ryel.zaja.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by billyu on 2016/12/2.
 */
public class BuyHouseVo implements Serializable{
    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer start;

    private Integer length;

    private Integer pageNum;

    private Integer pageSize;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}

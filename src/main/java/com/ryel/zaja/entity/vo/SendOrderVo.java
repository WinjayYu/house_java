package com.ryel.zaja.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by billyu on 2016/12/2.
 */
public class SendOrderVo implements Serializable{
    private static final long serialVersionUID = 1L;

    private String mobile;

    private Integer userId;

    private Integer agentId;

    private Integer houseId;

    private Integer communityUid;

    private BigDecimal sellPrice;

    private BigDecimal commission;

    private BigDecimal area;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public Integer getCommunityUid() {
        return communityUid;
    }

    public void setCommunityUid(Integer communityUid) {
        this.communityUid = communityUid;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }
}

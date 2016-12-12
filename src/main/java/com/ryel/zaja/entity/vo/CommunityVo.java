package com.ryel.zaja.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by billyu on 2016/12/9.
 */
public class CommunityVo implements Serializable{
    private static final long serialVersionUID = 1L;

    private String uid;

    private BigDecimal longitude;

    private BigDecimal latitude;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
}

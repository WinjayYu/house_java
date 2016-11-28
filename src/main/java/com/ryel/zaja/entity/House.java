package com.ryel.zaja.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * APP 注册用户
 */
@Entity
@Table(name = "user")
public class House implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "house_type")
    private String houseType;

    private String address;

    private BigDecimal area;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "source_house_status")
    private String sourceHouseStatus;

    @Column(name = "middleman_house_status")
    private String middlemanHouseStatus;

    private String longitude;

    private String latitude;

    private String type;

    @JoinColumn(name = "source_house_id")
    private House sourceHouseId;

    @Column(name = "publish_date")
    private Date publishDate;

    private String tags;

    private String year;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getSourceHouseStatus() {
        return sourceHouseStatus;
    }

    public void setSourceHouseStatus(String sourceHouseStatus) {
        this.sourceHouseStatus = sourceHouseStatus;
    }

    public String getMiddlemanHouseStatus() {
        return middlemanHouseStatus;
    }

    public void setMiddlemanHouseStatus(String middlemanHouseStatus) {
        this.middlemanHouseStatus = middlemanHouseStatus;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public House getSourceHouseId() {
        return sourceHouseId;
    }

    public void setSourceHouseId(House sourceHouseId) {
        this.sourceHouseId = sourceHouseId;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}

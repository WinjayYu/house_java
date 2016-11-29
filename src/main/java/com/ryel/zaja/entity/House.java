package com.ryel.zaja.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * APP 注册用户
 */
@Entity
@Table(name = "house")
public class House implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //经纪人id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "house_type")
    private String houseType;

    private String address;

    private BigDecimal area;

    @Column(name = "sell")
    private BigDecimal sellPrice;

    //10=待审核、20=上架、30=下架
    private String sourceHouseStatus;


    private String longitude;

    private String latitude;

    //10=经济人二次发布、20=经济人一手发布
    private String type;

    @JoinColumn(name = "sell_house_id")
    private House sellHouseId;

    @Column(name = "publish_date")
    private Date publishDate;

    //标签，如(近地铁|交通方便)，中间以|隔开
    private String tags;

    //年代
    private String year;

    private String feature;

    private String imgs;

    @Column(name = "fitment_level")
    private String fitmentLevel;

    //佣金
    private BigDecimal commission;

    private String direction;

    public House(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getSourceHouseStatus() {
        return sourceHouseStatus;
    }

    public void setSourceHouseStatus(String sourceHouseStatus) {
        this.sourceHouseStatus = sourceHouseStatus;
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

    public House getSellHouseId() {
        return sellHouseId;
    }

    public void setSellHouseId(House sellHouseId) {
        this.sellHouseId = sellHouseId;
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

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getFitmentLevel() {
        return fitmentLevel;
    }

    public void setFitmentLevel(String fitmentLevel) {
        this.fitmentLevel = fitmentLevel;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}

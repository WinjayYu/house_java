package com.ryel.zaja.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ryel.zaja.utils.CustomJsonDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 房源
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
    @JoinColumn(name = "agent_id")
    private User agent;

    @Column(name = "house_type")
    private String houseType;

    private String address;

    private BigDecimal area;

    @Column(name = "sell_price")
    private BigDecimal sellPrice;

    //10=待审核、20=上架、30=下架
    private String status;

    private BigDecimal longitude;

    private BigDecimal latitude;

    //10=经济人二次发布、20=经济人一手发布
    private String type;

    @ManyToOne
    @JoinColumn(name = "sell_house_id")
    private SellHouse sellHouse;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @Column(name = "add_time")
    private Date addTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @Column(name = "last_modified_time")
    private Date lastModifiedTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @Column(name = "publish_time")
    private Date publishTime;

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

    private String cityname;

    private String useage;

    private String floor;


    @ManyToOne
    @JoinColumn(name = "community_uid")
    private Community community;

    private String district;

    private String title;

    private String cover;

    public House(){}

    public House(Integer id){this.id = id;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SellHouse getSellHouse() {
        return sellHouse;
    }

    public void setSellHouse(SellHouse sellHouse) {
        this.sellHouse = sellHouse;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
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

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getUseage() {
        return useage;
    }

    public void setUseage(String useage) {
        this.useage = useage;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}

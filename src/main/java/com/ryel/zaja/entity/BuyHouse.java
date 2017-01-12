package com.ryel.zaja.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ryel.zaja.utils.CustomJsonDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 买房需求
 */
@Entity
@Table(name = "buy_house")
public class BuyHouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "min_price")
    private BigDecimal minPrice;

    @Column(name = "max_price")
    private BigDecimal maxPrice;

    @Column(name = "min_area")
    private BigDecimal minArea;

    @Column(name = "max_area")
    private BigDecimal maxArea;

    private String layout;

    private String renovation;

    @ManyToOne
    @JoinColumn(name = "community_uid", referencedColumnName = "uid")
    private Community community;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @Column(name = "add_time")
    private Date addTime;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @Column(name = "last_modified_time")
    private Date lastModifiedTime;

    private Integer num;

    public BuyHouse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinArea() {
        return minArea;
    }

    public void setMinArea(BigDecimal minArea) {
        this.minArea = minArea;
    }

    public BigDecimal getMaxArea() {
        return maxArea;
    }

    public void setMaxArea(BigDecimal maxArea) {
        this.maxArea = maxArea;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getRenovation() {
        return renovation;
    }

    public void setRenovation(String renovation) {
        this.renovation = renovation;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}

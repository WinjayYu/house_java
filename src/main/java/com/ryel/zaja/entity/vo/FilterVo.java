package com.ryel.zaja.entity.vo;

import java.math.BigDecimal;

/**接收筛选房源的参数
 * Created by billyu on 2016/12/6.
 */
public class FilterVo {

    private String sellPrice;

    private String area;

    private String type;

    private String decoration;

    private String floor;


    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDecoration() {
        return decoration;
    }

    public void setDecoration(String decoration) {
        this.decoration = decoration;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
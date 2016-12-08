package com.ryel.zaja.entity;

import javax.persistence.*;

/**
 * Created by billyu on 2016/12/7.
 */
@Entity
@Table(name = "click")
public class Click {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "house_id")
    private House house;

    @Column(name = "click_num")
    private int clickNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }
}

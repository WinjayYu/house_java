package com.ryel.zaja.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by billyu on 2016/12/7.
 */
@Entity
@Table(name = "recommend")
public class Recommend implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "house_id")
    private House house;

    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

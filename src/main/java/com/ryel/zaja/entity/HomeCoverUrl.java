package com.ryel.zaja.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by billyu on 2016/12/10.
 */
@Entity
@Table(name = "home_cover_url")
public class HomeCoverUrl implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String img;

    private String describe;

    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

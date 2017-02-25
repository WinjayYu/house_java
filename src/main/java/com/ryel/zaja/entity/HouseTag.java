package com.ryel.zaja.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nathan on 2016/12/29.
 */
@Entity
@Table(name = "house_tag")
public class HouseTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String tag;

    @Column(name = "tag_house_num")
    private Integer tagNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getTagNum() {
        return tagNum;
    }

    public void setTagNum(Integer tagNum) {
        this.tagNum = tagNum;
    }
}

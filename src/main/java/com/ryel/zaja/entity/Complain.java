package com.ryel.zaja.entity;

import javax.persistence.*;
import java.io.Serializable;

/**投诉表
 * Created by billyu on 2017/1/9.
 */
@Entity
@Table(name = "complain")
public class Complain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "house_order_id")
    private HouseOrder houseOrder;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    public Complain(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HouseOrder getHouseOrder() {
        return houseOrder;
    }

    public void setHouseOrder(HouseOrder houseOrder) {
        this.houseOrder = houseOrder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

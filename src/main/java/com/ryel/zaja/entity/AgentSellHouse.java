package com.ryel.zaja.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "agent_sell_house")
public class AgentSellHouse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //经纪人id
    @ManyToOne
    @JoinColumn(name = "agent_id")
    private User agent;

    @ManyToOne
    @JoinColumn(name = "sell_house_id")
    private SellHouse sellHouse;

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

    public SellHouse getSellHouse() {
        return sellHouse;
    }

    public void setSellHouse(SellHouse sellHouse) {
        this.sellHouse = sellHouse;
    }
}

package com.ryel.zaja.dao;

import com.ryel.zaja.entity.AgentBuyHouse;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AgentBuyHouseDao extends JpaRepository<AgentBuyHouse, Integer>,JpaSpecificationExecutor<AgentBuyHouse> {
    @Query("select u.buyHouse from AgentBuyHouse u where u.agent.id = ?1")
    Page<BuyHouse> pageBuyHouseByAgentId(Integer agentId, Pageable pageable);

    @Query("select count(a.agent) from AgentBuyHouse a where a.agent.id = ?1")
    Long count(Integer agentId);

    @Query("select a.buyHouse.id from AgentBuyHouse a where a.agent.id = ?1 ")
    List<Integer> findBuyHouseByAgentId(Integer agentId);

    @Query("select a from AgentBuyHouse a where a.buyHouse = ?1 and a.agent = ?2")
    AgentBuyHouse findBuyHouseAndAgent(BuyHouse buyHouse, User agent);
}

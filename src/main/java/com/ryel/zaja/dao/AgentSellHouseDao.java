package com.ryel.zaja.dao;

import com.ryel.zaja.entity.AgentSellHouse;
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
public interface AgentSellHouseDao extends JpaRepository<AgentSellHouse, Integer>,JpaSpecificationExecutor<AgentSellHouse> {
    @Query("select u.sellHouse from AgentSellHouse u where u.agent.id = ?1")
    Page<SellHouse> pageSellHouseByAgentId(Integer agentId, Pageable pageable);

    @Query("select count(a.agent) from AgentSellHouse a where a.agent.id = ?1")
    Long count(Integer agentId);

    @Query("select a.sellHouse.id from AgentSellHouse a where a.agent.id = ?1 ")
    List<Integer> findSellHouseByAgentId(Integer agnetId);

    @Query("select a from AgentSellHouse a where a.sellHouse = ?1 and a.agent = ?2")
    AgentSellHouse findbySellHouseAndAgent(SellHouse sellHouse, User agent);
}

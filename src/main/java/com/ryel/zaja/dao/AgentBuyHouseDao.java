package com.ryel.zaja.dao;

import com.ryel.zaja.entity.AgentBuyHouse;
import com.ryel.zaja.entity.BuyHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AgentBuyHouseDao extends JpaRepository<AgentBuyHouse, Integer>,JpaSpecificationExecutor<AgentBuyHouse> {
    @Query("select u.buyHouse from AgentBuyHouse u where u.agent.id = ?1")
    Page<BuyHouse> pageBuyHouseByAgentId(Integer agentId, Pageable pageable);
}

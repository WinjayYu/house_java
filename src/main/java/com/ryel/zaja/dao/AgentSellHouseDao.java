package com.ryel.zaja.dao;

import com.ryel.zaja.entity.AgentSellHouse;
import com.ryel.zaja.entity.SellHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AgentSellHouseDao extends JpaRepository<AgentSellHouse, Integer>,JpaSpecificationExecutor<AgentSellHouse> {
    @Query("select u.sellHouse from AgentSellHouse u where u.agent.id = ?1")
    Page<SellHouse> pageSellHouseByAgentId(Integer agentId, Pageable pageable);
}

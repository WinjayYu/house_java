package com.ryel.zaja.service;

import com.ryel.zaja.entity.AgentBuyHouse;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.SellHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AgentBuyHouseService extends ICommonService<AgentBuyHouse> {
    Page<BuyHouse> pageBuyHouseByAgentId(Integer agentId, Pageable pageable);


    Long count(Integer agentId);

    List<Integer> findBuyHouseByAgentId(Integer agnetId);

    AgentBuyHouse create(AgentBuyHouse agentBuyHouse);
}

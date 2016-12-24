package com.ryel.zaja.service;

import com.ryel.zaja.entity.AgentBuyHouse;
import com.ryel.zaja.entity.BuyHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AgentBuyHouseService extends ICommonService<AgentBuyHouse> {
    Page<BuyHouse> pageBuyHouseByAgentId(Integer agentId, Pageable pageable);


    Long count(Integer agentId);

}

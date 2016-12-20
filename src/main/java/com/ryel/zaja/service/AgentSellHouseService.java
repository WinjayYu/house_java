package com.ryel.zaja.service;

import com.ryel.zaja.entity.AgentBuyHouse;
import com.ryel.zaja.entity.AgentSellHouse;
import com.ryel.zaja.entity.SellHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AgentSellHouseService extends ICommonService<AgentSellHouse> {
    Page<SellHouse> pageSellHouseByAgentId(Integer agentId, Pageable pageable);


}

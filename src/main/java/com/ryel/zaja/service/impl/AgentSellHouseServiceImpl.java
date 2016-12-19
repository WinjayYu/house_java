package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.AgentSellHouseDao;
import com.ryel.zaja.entity.AgentSellHouse;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.AgentSellHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AgentSellHouseServiceImpl extends AbsCommonService<AgentSellHouse> implements AgentSellHouseService {

    @Autowired
    private AgentSellHouseDao agentSellHouseDao;


    @Override
    public JpaRepository<AgentSellHouse, Integer> getDao() {
        return agentSellHouseDao;
    }


    @Override
    public Page<SellHouse> pageSellHouseByAgentId(Integer agentId, Pageable pageable) {
        return agentSellHouseDao.pageSellHouseByAgentId(agentId,pageable);
    }

}

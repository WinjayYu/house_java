package com.ryel.zaja.service.impl;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.AgentBuyHouseDao;
import com.ryel.zaja.entity.AgentBuyHouse;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.AgentBuyHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AgentBuyHouseServiceImpl extends AbsCommonService<AgentBuyHouse> implements AgentBuyHouseService {

    @Autowired
    private AgentBuyHouseDao agentBuyHouseDao;


    @Override
    public JpaRepository<AgentBuyHouse, Integer> getDao() {
        return agentBuyHouseDao;
    }


    @Override
    public Page<BuyHouse> pageBuyHouseByAgentId(Integer agentId, Pageable pageable) {
        return agentBuyHouseDao.pageBuyHouseByAgentId(agentId,pageable);
    }


    @Override
    public Long count(Integer agentId) {
        return agentBuyHouseDao.count(agentId);
    }

    @Override
    public List<Integer> findBuyHouseByAgentId(Integer agentId) {
        return agentBuyHouseDao.findBuyHouseByAgentId(agentId);
    }

    @Override
    @Transactional
    public AgentBuyHouse create(AgentBuyHouse agentBuyHouse) {
        if(null != agentBuyHouseDao.findBuyHouseAndAgent(agentBuyHouse.getBuyHouse(), agentBuyHouse.getAgent())){
            throw new BizException(Error_code.ERROR_CODE_0032);
        }
        return this.save(agentBuyHouse);
    }
}

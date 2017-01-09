package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.AgentMaterialDao;
import com.ryel.zaja.entity.AgentMaterial;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.AgentMaterialService;
import com.ryel.zaja.utils.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;

@Service
@Transactional(readOnly = true)
public class AgentMaterialImpl extends AbsCommonService<AgentMaterial> implements AgentMaterialService {

    @Autowired
    private AgentMaterialDao agentMaterialDao;

    @Autowired
    EntityManagerFactory emf;

    @Override
    public JpaRepository<AgentMaterial, Integer> getDao() {
        return agentMaterialDao;
    }

    @Override
    @Transactional
    public AgentMaterial create(AgentMaterial agentMaterial) {
        this.save(agentMaterial);
        return agentMaterial;
    }

    @Override
    @Transactional
    public AgentMaterial update(AgentMaterial agentMaterial) {
        AgentMaterial origAgent = agentMaterialDao.findByIdcard(agentMaterial.getIdcard());
        ClassUtil.copyProperties(origAgent, agentMaterial);
        return save(origAgent);
    }

    @Override
    public AgentMaterial findByAgentId(Integer agentId) {
        return agentMaterialDao.findByAgentId(agentId);
    }
}

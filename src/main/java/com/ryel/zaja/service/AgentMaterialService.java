package com.ryel.zaja.service;

import com.ryel.zaja.entity.AgentMaterial;


public interface AgentMaterialService extends ICommonService<AgentMaterial>{

    AgentMaterial create(AgentMaterial agentMaterial);

    AgentMaterial update(AgentMaterial agentMaterial);

    AgentMaterial findByAgentId(Integer userId);
}

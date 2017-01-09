package com.ryel.zaja.service;


import com.ryel.zaja.entity.AgentLocation;
import com.ryel.zaja.entity.User;

import java.util.List;

/**
 * Created by billyu on 2017/1/7.
 */
public interface AgentLocationService extends ICommonService<AgentLocation>{

    List<AgentLocation> findByDis(String district);

    List<AgentLocation> findByLoc(Double longitude, Double latitude, List<AgentLocation> listByDis);

    AgentLocation findByAgent(User agent);

    AgentLocation create(AgentLocation agentLocation);

    AgentLocation update(AgentLocation origAgentLocation, AgentLocation agentLocation);

    List<AgentLocation> findByCity(String city);
}

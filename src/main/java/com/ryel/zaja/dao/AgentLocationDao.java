package com.ryel.zaja.dao;

import com.ryel.zaja.entity.AgentLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by billyu on 2017/1/7.
 */
public interface AgentLocationDao extends JpaRepository<AgentLocation, Integer>,JpaSpecificationExecutor<AgentLocation> {

    List<AgentLocation> findByDistrict(String district);

    List<AgentLocation> findByCity(String city);

    @Query("select a from AgentLocation a where a.agent.id = ?1")
    AgentLocation findByAgentId(Integer id);
}

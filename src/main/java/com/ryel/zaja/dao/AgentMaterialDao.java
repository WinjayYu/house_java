package com.ryel.zaja.dao;

import com.ryel.zaja.entity.AgentMaterial;
import com.ryel.zaja.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AgentMaterialDao extends JpaRepository<AgentMaterial, Integer>,JpaSpecificationExecutor<AgentMaterial> {
    @Query("select u from AgentMaterial u where u.agent.id = ?1")
    AgentMaterial findByAgentId(Integer agentId);

    @Query("select u from AgentMaterial u where u.idcard = ?1")
    List<AgentMaterial> findByIdcard(String idcard);
}

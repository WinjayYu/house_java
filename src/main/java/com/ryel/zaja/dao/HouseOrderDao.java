package com.ryel.zaja.dao;

import com.ryel.zaja.entity.HouseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by burgl on 2016/8/16.
 */
@Repository
public interface HouseOrderDao extends JpaRepository<HouseOrder, Integer> ,JpaSpecificationExecutor<HouseOrder> {

    @Query("select h from HouseOrder h where h.agent.id = ?1 or h.seller.id = ?1 or h.buyer.id = ?1")
    List<HouseOrder> list(Integer id);

    @Query("select h from HouseOrder h where h.agent.id = ?1")
    Page<HouseOrder> pageByAgentId(Integer agentId, Pageable pageable);

    @Query("select h from HouseOrder h where h.buyer.id = ?1")
    Page<HouseOrder> pageByUserId(Integer agentId, Pageable pageable);

}

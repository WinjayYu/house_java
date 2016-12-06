package com.ryel.zaja.dao;

import com.ryel.zaja.entity.SendOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by billyu on 2016/12/2.
 */
@Repository
public interface SendOrderDao  extends JpaRepository<SendOrder, Integer>,JpaSpecificationExecutor<SendOrder> {
    @Query("select s from SendOrder s where s.agent.id = ?1")
    Page<SendOrder> findByAgentId(Integer id, Pageable pageable);
}

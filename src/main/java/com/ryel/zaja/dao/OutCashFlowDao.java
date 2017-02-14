package com.ryel.zaja.dao;

import com.ryel.zaja.entity.OutCashFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by Nathan on 2017/2/10.
 */
@Repository
public interface OutCashFlowDao extends JpaRepository<OutCashFlow, Integer>,JpaSpecificationExecutor<OutCashFlow> {
}

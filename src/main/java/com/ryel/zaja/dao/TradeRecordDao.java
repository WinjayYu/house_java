package com.ryel.zaja.dao;

import com.ryel.zaja.entity.TradeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRecordDao extends JpaRepository<TradeRecord, Integer> ,JpaSpecificationExecutor<TradeRecord> {
    @Query("select t from TradeRecord t where t.thirdHtId = ?1")
    TradeRecord findByThirdHtId(String thirdHtId);

    TradeRecord findByOrderId(Integer orderId);

    @Query("select t from TradeRecord t where t.inThirdCustId.id = ?1")
    Page<TradeRecord> findByInThirdCustId(Integer userId,Pageable pageable);
}

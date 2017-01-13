package com.ryel.zaja.dao;

import com.ryel.zaja.entity.TradeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRecordDao extends JpaRepository<TradeRecord, Integer> ,JpaSpecificationExecutor<TradeRecord> {
    List<TradeRecord> findByThirdHtId(String thirdHtId);
}

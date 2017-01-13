package com.ryel.zaja.dao;

import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.TradeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRecordDao extends JpaRepository<TradeRecord, Integer> ,JpaSpecificationExecutor<TradeRecord> {
    @Query("select t from TradeRecord t where t.ThirdHtId = ?1")
    List<TradeRecord> findByThirdHtId(String thirdHtId);
}

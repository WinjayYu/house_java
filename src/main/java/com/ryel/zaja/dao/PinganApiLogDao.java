package com.ryel.zaja.dao;

import com.ryel.zaja.entity.PinganApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PinganApiLogDao extends JpaRepository<PinganApiLog, Integer> ,JpaSpecificationExecutor<PinganApiLog> {


}

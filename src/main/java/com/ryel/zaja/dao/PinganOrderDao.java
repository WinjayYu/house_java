package com.ryel.zaja.dao;

import com.ryel.zaja.entity.PinganOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by Nathan on 2017/1/15.
 */
@Repository
public interface PinganOrderDao extends JpaRepository<PinganOrder, Integer>,JpaSpecificationExecutor<PinganOrder> {
    PinganOrder findByOrderId(Integer orderId);
}

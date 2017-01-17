package com.ryel.zaja.dao;

import com.ryel.zaja.entity.PinanOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by Nathan on 2017/1/15.
 */
@Repository
public interface PinanOrderDao extends JpaRepository<PinanOrder, Integer>,JpaSpecificationExecutor<PinanOrder> {
}

package com.ryel.zaja.dao;

import com.ryel.zaja.entity.HouseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by burgl on 2016/8/16.
 */
@Repository
public interface OrderDao extends JpaRepository<HouseOrder, Integer> ,JpaSpecificationExecutor<HouseOrder> {

    @Query("select h from HouseOrder h where h.code = 'qqqq'")
    public HouseOrder findByCode(String code);
}

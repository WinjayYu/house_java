package com.ryel.zaja.dao;

import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Created by burgl on 2016/8/16.
 */
@Repository
public interface HouseDao extends JpaRepository<House, Integer> ,JpaSpecificationExecutor<House> {

    /*@Query("select h from House h where h.sellPrice = ?1")
    public Page<House> filter(BigDecimal sellPrice, BigDecimal area, String decoration, String floor);*/
}

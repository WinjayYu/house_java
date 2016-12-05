package com.ryel.zaja.dao;

import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.entity.SendOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by burgl on 2016/8/16.
 */
@Repository
public interface BuyHouseDao extends JpaRepository<BuyHouse, Integer> ,JpaSpecificationExecutor<BuyHouse> {

    @Query("select b from BuyHouse b where b.user.id = ?1 order by b.addTime desc")
    public List<BuyHouse> findByUserId(Integer userId);

    @Query("select b from BuyHouse b where b.user.id = ?1")
    public Page<BuyHouse> findByUserId(Integer userId, Pageable pageable);

}

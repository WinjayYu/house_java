package com.ryel.zaja.dao;

import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.SellHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public interface SellHouseDao extends JpaRepository<SellHouse, Integer> ,JpaSpecificationExecutor<SellHouse> {

    @Query("select s from SellHouse s where s.user.id = ?1 order by s.addTime desc")
    Page<SellHouse> findByUserId(Integer id, Pageable pageable);

    @Query("select b from SellHouse b")
    Page<SellHouse> pageAll(Pageable pageable);

    @Query("select s from SellHouse s where s.community.uid in ?1 and s.id not in ?2 and s.num <= 60")
    Page<SellHouse> agentPage(List<String> uids, List<Integer> list, Pageable pageable);

    @Query("select s.id from SellHouse s where s.user.id = ?1")
    List<Integer> findByUserIdAsId(Integer userId);

}

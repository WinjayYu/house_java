package com.ryel.zaja.dao;

import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.Recommend;
import com.ryel.zaja.entity.SellHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by billyu on 2016/12/9.
 */
@Repository
public interface RecommendDao extends JpaRepository<Recommend,Integer>, JpaSpecificationExecutor<Recommend>{

    @Query("select r.house from Recommend r where r.status = ?1")
    List<House> findByStatus(String status);
}

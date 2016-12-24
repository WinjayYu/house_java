package com.ryel.zaja.dao;

import com.ryel.zaja.entity.Collect;
import com.ryel.zaja.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by billyu on 2016/12/7.
 */
@Repository
public interface CollectDao extends JpaRepository<Collect, Integer>,JpaSpecificationExecutor<Collect> {

    @Query("select c.house from Collect c where c.house.id = ?1")
    List<House> findByHouseId(Integer id);

    @Query("select count(c.house) from Collect c where c.house.id = ?1")
    Integer countByHouseId(Integer id);

    @Query("select co from Collect co where co.user.id = ?1 and co.house.id = ?2")
    Collect findByUserIdAndHouseId(Integer userId, Integer HouseId);

    @Query("select c.house from Collect c where c.user.id = ?1")
    Page<House> pageByUserId(Integer userId, Pageable pageable);
}

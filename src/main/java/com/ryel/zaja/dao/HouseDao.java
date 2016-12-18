package com.ryel.zaja.dao;

import com.ryel.zaja.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by burgl on 2016/8/16.
 */
@Repository
public interface HouseDao extends JpaRepository<House, Integer> ,JpaSpecificationExecutor<House> {

    @Query("select h from House h where h.layout = ?1 and h.status in ?2")
    List<House> findByLayout(String layout, List<String> status);

    List<House> findByCityname(String cityname);

    @Query("select h from House h where h.community.uid = ?1 and h.status in ?2")
    Page<House> findByUid(String uid, List<String> status, Pageable pageable);


    @Query("select h from House h where h.community.uid = ?1 and h.status in ?2")
    List<House> findByCommunityUid(String uid, List<String> status);

    @Query("select h from House h where h.community.uid = ?1 and h.area = ?2 and h.fitmentLevel = ?3 order by h.sellPrice asc")
    List<House> findByCommumityAndAreaAndFitmentLevel(String uid, BigDecimal area, String fitmentlevel);

    @Query("select b from House b where b.agent.id = ?1")
    Page<House> pageByAgentId(Integer agentId, Pageable pageable);

    @Query("select h from House h where h.community.uid in ?1")
    Page<House> findByCommunities(List<String> uidList, Pageable pageable);

    @Query("select h from House h order by h.addTime")
    Page<House> findByAddTime(Pageable pageable);
}

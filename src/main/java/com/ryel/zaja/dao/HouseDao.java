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
    List<House> findByHouseLayout(String layout, List<String> status);
    List<House> findByCommunityName(String communityName);
    List<House> findByLayout(String layout, List<String> status);

    List<House> findByCommunityAddress(String communityAddress);

    List<House> findByCity(String city);

    @Query("select h from House h where h.community.uid = ?1 and h.status in ?2")
    Page<House> findByUid(String uid, List<String> status, Pageable pageable);

    @Query("select h from House h where h.community.uid = ?1 and h.status in ?2")
    List<House> findByCommunityUid(String uid, List<String> status);

    @Query("select h from House h where h.agent.id = ?1 and h.sellHouse.id = ?2")
    List<House> findByAgentIdAndSellHouseId(Integer agentId, Integer sellHouseId);

    @Query("select h from House h where abs(h.price - ?1) < 100000 or (h.community.uid = ?2 or h.area = ?3 or h.renovation = ?4) order by h.price asc")
    List<House> findSimilar(BigDecimal price, String uid, BigDecimal area, String renovation);

    @Query("select h from House h where h.status in ?5 and( abs(h.price - ?1) < 100000 or (h.community.uid = ?2 or h.area = ?3 or h.renovation = ?4))  order by h.price asc")
    List<House> agentFindSimilar(BigDecimal price, String uid, BigDecimal area, String renovation,List<String> status);

    @Query("select b from House b where b.agent.id = ?1")
    Page<House> pageByAgentId(Integer agentId, Pageable pageable);

    @Query("select b from House b where b.community.uid = ?1 and b.status in ?2")
    Page<House> pageByCommunityUid(String uid,List<String> status, Pageable pageable);

    @Query("select b from House b where  b.status in ?1")
    Page<House> agentPage(List<String> status, Pageable pageable);

    @Query("select h from House h where h.community.uid in ?1")
    Page<House> findByCommunities(List<String> uidList, Pageable pageable);

    @Query("select h from House h order by h.addTime")
    Page<House> findByAddTime(Pageable pageable);

    @Query("select h from House h where h.sellHouse.id = ?1")
    Page<House> findBySellHouse(Integer userId, Pageable pageable);
}

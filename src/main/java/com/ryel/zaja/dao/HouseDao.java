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

    List<House> findByCity(String city);

    @Query("select h from House h where h.community.uid = ?1 and h.status in ?2")
    Page<House> findByUid(String uid, List<String> status, Pageable pageable);

    @Query("select h from House h where h.community.uid in ?1 and h.status in ?2")
    Page<House> findByUids(List<String> uid, List<String> status, Pageable pageable);

    @Query("select h from House h where h.community.uid = ?1 and h.status in ?2")
    List<House> findByCommunityUid(String uid, List<String> status);

    @Query("select h from House h where h.agent.id = ?1 and h.sellHouse.id = ?2")
    List<House> findByAgentIdAndSellHouseId(Integer agentId, Integer sellHouseId);

    @Query("select h from House h where h.status in ?5 and abs(h.price - ?1) < 100000 or (h.community.uid = ?2 or h.area = ?3 or h.renovation = ?4) order by h.price asc")
    List<House> findSimilar(BigDecimal price, String uid, BigDecimal area, String renovation,List<String> status);

    @Query("select h from House h where h.status in ?5 and( abs(h.price - ?1) < 100000 or (h.community.uid = ?2 or h.area = ?3 or h.renovation = ?4))  order by h.price asc")
    List<House> agentFindSimilar(BigDecimal price, String uid, BigDecimal area, String renovation,List<String> status);

    @Query("select b from House b where b.agent.id = ?1 and b.status in ?2")
    Page<House> pageByAgentId(Integer agentId,List<String> status, Pageable pageable);

    //用户端查看经纪人发布的房源
    @Query("select b from House b where b.agent.id = ?1 and b.status = ?2" )
    Page<House> pageByAgentId2(Integer agentId, String status, Pageable pageable);

    @Query("select b from House b where b.community.uid = ?1 and b.status in ?2")
    Page<House> pageByCommunityUid(String uid,List<String> status, Pageable pageable);

    @Query("select b from House b where b.status in ?1 ")
    Page<House> agentPage(List<String> status, Pageable pageable);

    @Query("select h from House h where h.status in ?1 order by h.addTime")
    Page<House> findByAddTime(List<String> list, Pageable pageable);

    @Query("select h from House h where h.sellHouse.user.id = ?1")
    Page<House> findBySellHouse(Integer userId, Pageable pageable);

    @Query("select b from House b where b.status in ?1 and b.community.uid in ?2")
    Page<House> findByCommunitiesStatus(List<String> status,List<String> uidList, Pageable pageable);

    //用sellHouseId查出所有的通过此sellHouse编辑而成的House
    @Query("select h from House h where h.sellHouse.id = ?1")
    List<House> listBySellHouse(Integer sellHouseId);

    @Query("select count(h) from House h where h.agent.id = ?1")
    Long countByAgentId(Integer agentId);

    //用sellHouseId查出所有的通过此sellHouse编辑而成的House
    @Query("select h from House h where h.sellHouse.id = ?1 and h.status = '30'")
    Page<House> PageBySellHouse(Integer sellHouseId, Pageable pageable);

    @Query("select h from House h where h.community.uid in ?1 and h.status in ?2")
    List<House> listByUids(List<String> uid, List<String> status);

}

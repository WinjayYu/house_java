package com.ryel.zaja.service;

import com.ryel.zaja.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by billyu on 2016/11/28.
 */
public interface HouseService extends ICommonService<House> {

    House create(House house);

    House update(House house);

    List<House> findByCommunityUid(String uid, String type);

    List<House> findByAgentIdAndSellHouseId(Integer agentId,Integer sellHouseId);

    List<House> findByCity(String city);

    Page<House> findByUid(String uid,String type, Pageable pageable);

    Page<House> findByUids(List<String> uids,String type, Pageable pageable);

    List<House> findByLayout(String houseType, String type);

    List<House> findSimilar(BigDecimal price, String uid, BigDecimal area, String renovation);

    List<House> agentFindSimilar(BigDecimal price, String uid, BigDecimal area, String renovation);

    Page<House> filter(int pageNum,
                       int pageSize,
                       final String price,
                       final String area,
                       final String layout,
                       final String renovation,
                       final String floor,
                       final String userType);

    Page<House> mgtPageHouse(int pageNum, int pageSize);

    Page<House> findByCommunitiesStatus(List<String> status,List<String> uidList, Pageable pageable);


    Page<House> findByAddTime(String type, Pageable pageable);

    /**
     * 获取所有房源列表
     *
     * @return
     */
    List<House> list();

    void agentDeleteHouse(Integer houseId, Integer agentId);

    Page<House> pageByAgentId(Integer agentId, Pageable pageable);

    Page<House> pageByCommunityUid(String uid,List<String> status, Pageable pageable);


    void agentPutawayHouse(Integer houseId);
    void agentSoldOutHouse(Integer houseId);

    House getHouseByCheck(Integer houseId);

    House findById(Integer id);

    Page<House> findBySellHouse(Integer userId, Pageable pageable);

    //用户端查看经纪人的房源列表
    Page<House> pageByAgentId2(int agentId, Pageable pageable);

    Long count(Integer agentId);
}

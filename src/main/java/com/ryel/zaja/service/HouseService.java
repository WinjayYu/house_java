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

    Page<House> mgtPageHouse(int pageNum, int pageSize,String title,String status);

    Page<House> findByCommunitiesStatus(List<String> status,List<String> uidList, Pageable pageable);


    Page<House> findByAddTime(String type, Pageable pageable);


    List<House> list();

    void agentDeleteHouse(Integer houseId, Integer agentId);

    Page<House> pageByAgentId(Integer agentId, Pageable pageable);

    Page<House> pageByCommunityUid(String uid,List<String> status, Pageable pageable);


    void agentPutawayHouse(Integer houseId);
    void agentSoldOutHouse(Integer houseId);

    House getHouseByCheck(Integer houseId);

    House findById(Integer id);

    Page<House> pageBySellHouse(Integer userId, Pageable pageable);

    //用户端查看经纪人的房源列表
    Page<House> pageByAgentId2(int agentId, Pageable pageable);

    Long count(Integer agentId);

    //home页面的zaja推荐与热门房源
    List<House> recommend(int userId, String type);
    List<House> hotHouse(double lon1, double lat1, String city, String type);
    //获取所有的房屋信息
    Page<House> backPageHouse(int pageNum, int pageSize,String status);
}

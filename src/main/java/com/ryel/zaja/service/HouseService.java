package com.ryel.zaja.service;

import com.ryel.zaja.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */
public interface HouseService extends ICommonService<House> {

    House create(House house);

    House update(House house);

    List<House> findByCommunityUid(String uid, String type);

    List<House> findByCity(String city);

    Page<House> findByUid(String uid,String type, Pageable pageable);

    List<House> findByLayout(String houseType, String type);

    List<House> findByCommumityAndAreaAndRenovation(String uid, BigDecimal area, String renovation);

    Page<House> filter(int pageNum,
                       int pageSize,
                       final String price,
                       final String area,
                       final String layout,
                       final String renovation,
                       final String floor,
                       final String userType);

    Page<House> findByCommunities(List<String> uids, Pageable pageable);

    List<House> findByCommunities(List<String> uids);

    Page<House> findByAddTime(Pageable pageable);

    /**
     * 获取所有房源列表
     *
     * @return
     */
    List<House> list();

    void agentDeleteHouse(int houseId);

    Page<House> pageByAgentId(int agentId, Pageable pageable);

    void agentPutawayHouse(Integer houseId);

    House getHouseByCheck(Integer houseId);

    House findById(Integer id);

}

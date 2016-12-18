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

    List<House> findByCommunityUid(String uid);

    List<House> findByCity(String city);

    Page<House> findByUid(String uid, Pageable pageable);

    List<House> findByLayout(String houseType);

    List<House> findByCommumityAndAreaAndRenovation(String uid, BigDecimal area, String fitmentlevel);

    Page<House> filter(int pageNum,
                       int pageSize,
                       final String sellPrice,
                       final String area,
                       final String houseType,
                       final String fitmentLevel,
                       final String floor);

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

}

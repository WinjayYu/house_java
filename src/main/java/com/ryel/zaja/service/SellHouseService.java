package com.ryel.zaja.service;

import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.SellHouse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SellHouseService extends ICommonService<SellHouse> {


    SellHouse create(SellHouse sellHouse);

    Page<SellHouse> pageByUserId(int userId, int pageNum, int pageSize);

    Page<SellHouse> pageAll(int pageNum, int pageSize);


    Page<SellHouse> agentPage(Integer pageNum, Integer pageSize, List<String> uids, List<Integer> list);

    List<Integer> findByUserIdAsId(Integer userId);

    SellHouse update(SellHouse sellHouse);
}

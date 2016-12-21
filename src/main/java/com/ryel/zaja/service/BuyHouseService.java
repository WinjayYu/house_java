package com.ryel.zaja.service;

import com.ryel.zaja.entity.BuyHouse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BuyHouseService extends ICommonService<BuyHouse> {

    BuyHouse create(BuyHouse buyHouse);

    List<BuyHouse> findByUserId(int userId);

    Page<BuyHouse> findByPage(Integer userId, int pageNum, int pageSize);

    Page<BuyHouse> pageAll(int pageNum, int pageSize);

}

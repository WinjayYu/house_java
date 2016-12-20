package com.ryel.zaja.service;

import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.SellHouse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SellHouseService extends ICommonService<SellHouse> {


    SellHouse create(SellHouse sellHouse);

    List<SellHouse> findByUserId(int userId);

    Page<SellHouse> pageAll(int pageNum, int pageSize);
}

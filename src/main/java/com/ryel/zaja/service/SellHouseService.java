package com.ryel.zaja.service;

import com.ryel.zaja.entity.SellHouse;

import java.util.List;

public interface SellHouseService extends ICommonService<SellHouse> {


    SellHouse create(SellHouse sellHouse);

    List<SellHouse> findByUserId(int userId);
}

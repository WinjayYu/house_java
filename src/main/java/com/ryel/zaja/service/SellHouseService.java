package com.ryel.zaja.service;

import com.ryel.zaja.entity.SellHouse;

import java.util.List;

public interface SellHouseService extends ICommonService<SellHouse> {

    public SellHouse create(SellHouse sellHouse);

    public List<SellHouse> findByUserId(int userId);
}

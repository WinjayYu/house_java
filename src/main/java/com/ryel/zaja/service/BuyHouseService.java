package com.ryel.zaja.service;

import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.SellHouse;

import java.util.List;

public interface BuyHouseService extends ICommonService<BuyHouse> {

    public BuyHouse create(BuyHouse buyHouse);

    public List<BuyHouse> findByUserId(int userId);
}

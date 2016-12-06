package com.ryel.zaja.service;

import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.entity.SendOrder;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BuyHouseService extends ICommonService<BuyHouse> {

    BuyHouse create(BuyHouse buyHouse);

    List<BuyHouse> findByUserId(int userId);

    Page<BuyHouse> findByPage(Integer userId, int pageNum, int pageSize);

}

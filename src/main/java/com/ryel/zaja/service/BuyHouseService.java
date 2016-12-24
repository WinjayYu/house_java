package com.ryel.zaja.service;

import com.ryel.zaja.entity.BuyHouse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BuyHouseService extends ICommonService<BuyHouse> {

    BuyHouse create(BuyHouse buyHouse);

    List<BuyHouse> findByUserId(int userId);

    Page<BuyHouse> findByPage(Integer userId, int pageNum, int pageSize);

    Page<BuyHouse> agentPage(Integer pageNum, Integer pageSize,List<String> uids);

}

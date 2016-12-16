package com.ryel.zaja.service;

import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.Recommend;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RecommendService extends ICommonService<Recommend> {

    List<House> findByStatus(String status);
}

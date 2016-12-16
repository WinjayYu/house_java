package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.HouseOrderDao;
import com.ryel.zaja.dao.RecommendDao;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.HouseOrder;
import com.ryel.zaja.entity.Recommend;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.HouseOrderService;
import com.ryel.zaja.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by burgl on 2016/8/27.
 */
@Service
public class RecommendServiceImpl extends AbsCommonService<Recommend> implements RecommendService {

    @Autowired
    RecommendDao recommendDao;


    @Override
    public List<House> findByStatus(String status) {
        return recommendDao.findByStatus(status);
    }

    @Override
    public JpaRepository<Recommend, Integer> getDao() {
        return getDao();
    }
}

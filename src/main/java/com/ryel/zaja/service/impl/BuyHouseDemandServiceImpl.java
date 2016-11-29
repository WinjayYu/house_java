package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.BuyHouseDemandDao;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.BuyHouseDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by burgl on 2016/8/27.
 */
@Service
public class BuyHouseDemandServiceImpl extends AbsCommonService<BuyHouse> implements BuyHouseDemandService {

    @Autowired
    private BuyHouseDemandDao buyHouseDemandDao;

    @Override
    @Transactional
    public BuyHouse create(BuyHouse buyHouse) {
        return this.save(buyHouse);
    }


    @Override
    public JpaRepository<BuyHouse, Integer> getDao() {
        return buyHouseDemandDao;
    }
}

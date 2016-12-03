package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.BuyHouseDao;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.BuyHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by burgl on 2016/8/27.
 */
@Service
public class BuyHouseServiceImpl extends AbsCommonService<BuyHouse> implements BuyHouseService {

    @Autowired
    private BuyHouseDao buyHouseDao;

    @Override
    @Transactional
    public BuyHouse create(BuyHouse buyHouse) {
        return this.save(buyHouse);
    }


    @Override
    public JpaRepository<BuyHouse, Integer> getDao() {
        return buyHouseDao;
    }

    @Override
    public List<BuyHouse> findByUserId(int userId) {
        List<BuyHouse> buyHouses = buyHouseDao.findByUserId(userId);
        return buyHouses;
    }
}

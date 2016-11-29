package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.SellHouseDao;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.SellHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by burgl on 2016/8/27.
 */
@Service
public class SellHouseServiceImpl extends AbsCommonService<SellHouse> implements SellHouseService {

    @Autowired
    private SellHouseDao sellHouseDao;

    @Override
    @Transactional
    public SellHouse create(SellHouse sellHouse) {
        return this.save(sellHouse);
    }


    @Override
    public JpaRepository<SellHouse, Integer> getDao() {
        return sellHouseDao;
    }
}

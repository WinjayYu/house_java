package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.HouseOrderDao;
import com.ryel.zaja.entity.HouseOrder;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.HouseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by burgl on 2016/8/27.
 */
@Service
public class HouseOrderServiceImpl extends AbsCommonService<HouseOrder> implements HouseOrderService {

    @Autowired
    private HouseOrderDao houseOrderDao;

    @Override
    @Transactional
    public HouseOrder create(HouseOrder houseOder) {
        return this.save(houseOder);
    }


    @Override
    public JpaRepository<HouseOrder, Integer> getDao() {
        return houseOrderDao;
    }
}

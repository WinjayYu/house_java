package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.PinanOrderDao;
import com.ryel.zaja.entity.PinanOrder;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.PinanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Nathan on 2017/1/15.
 */
@Service
public class PinanOrderServiceImpl extends AbsCommonService<PinanOrder> implements PinanOrderService {

    @Autowired
    private PinanOrderDao pinanOrderDao;

    @Override
    @Transactional
    public void create(PinanOrder order) {
        save(order);
    }

    @Override
    public JpaRepository<PinanOrder, Integer> getDao() {
        return pinanOrderDao;
    }
}

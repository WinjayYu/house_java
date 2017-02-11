package com.ryel.zaja.service.impl;


import com.ryel.zaja.dao.PinganOrderDao;
import com.ryel.zaja.entity.PinganOrder;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.PinganOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Nathan on 2017/1/15.
 */
@Service
public class PinganOrderServiceImpl extends AbsCommonService<PinganOrder> implements PinganOrderService {

    @Autowired
    private PinganOrderDao pinganOrderDao;

    @Override
    @Transactional
    public void create(PinganOrder order) {
        save(order);
    }

    @Override
    public PinganOrder findByOrderId(Integer orderId) {
        return pinganOrderDao.findByOrderId(orderId);
    }

    @Override
    public JpaRepository<PinganOrder, Integer> getDao() {
        return pinganOrderDao;
    }
}

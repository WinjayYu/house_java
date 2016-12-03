package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.SendOrderDao;
import com.ryel.zaja.entity.SendOrder;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.SendOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */
@Service
public class SendOrderServiceImpl extends AbsCommonService<SendOrder> implements SendOrderService{

    @Autowired
    private SendOrderDao sendOrderDao;

    @Override
    @Transactional
    public SendOrder create(SendOrder sendOrder) {
        sendOrder.setAddTime(new Date());
        sendOrderDao.save(sendOrder);
        return sendOrder;
    }

    @Override
    public List<SendOrder> list() {
        return null;
    }

    @Override
    public JpaRepository<SendOrder, Integer> getDao() {
        return null;
    }
}

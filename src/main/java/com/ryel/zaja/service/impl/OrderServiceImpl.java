package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.OrderDao;
import com.ryel.zaja.entity.Order;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by burgl on 2016/8/27.
 */
@Service
public class OrderServiceImpl extends AbsCommonService<Order> implements OrderService{

    @Autowired
    private OrderDao orderDao;

    @Override
    @Transactional
    public Order create(Order agenda) {
        return this.save(agenda);
    }

    @Override
    public JpaRepository<Order, Integer> getDao() {
        return orderDao;
    }
}

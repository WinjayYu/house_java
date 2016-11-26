package com.ryel.zaja.service;

import com.ryel.zaja.entity.Order;

public interface OrderService extends ICommonService<Order> {

    public Order create(Order order);


}

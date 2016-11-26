package com.ryel.zaja.service;

import com.ryel.zaja.entity.HouseOrder;

public interface OrderService extends ICommonService<HouseOrder> {

    public HouseOrder create(HouseOrder houseOrder);


}

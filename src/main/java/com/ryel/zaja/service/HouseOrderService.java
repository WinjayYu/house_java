package com.ryel.zaja.service;

import com.ryel.zaja.entity.HouseOrder;
import org.springframework.stereotype.Service;

public interface HouseOrderService extends ICommonService<HouseOrder> {

    public HouseOrder create(HouseOrder houseOrder);


}

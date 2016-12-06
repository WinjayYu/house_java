package com.ryel.zaja.service;

import com.ryel.zaja.entity.HouseOrder;
import org.springframework.jca.cci.core.InteractionCallback;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HouseOrderService extends ICommonService<HouseOrder> {

    HouseOrder create(HouseOrder houseOrder);

    List<HouseOrder> list(Integer id);
}

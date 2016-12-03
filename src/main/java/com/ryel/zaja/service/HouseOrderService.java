package com.ryel.zaja.service;

import com.ryel.zaja.entity.HouseOrder;
import org.springframework.jca.cci.core.InteractionCallback;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HouseOrderService extends ICommonService<HouseOrder> {

    public HouseOrder create(HouseOrder houseOrder);

    public List<HouseOrder> list(Integer id);
}

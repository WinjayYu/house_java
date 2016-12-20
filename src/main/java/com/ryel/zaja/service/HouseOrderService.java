package com.ryel.zaja.service;

import com.ryel.zaja.entity.HouseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jca.cci.core.InteractionCallback;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HouseOrderService extends ICommonService<HouseOrder> {

    HouseOrder create(HouseOrder houseOrder);

    List<HouseOrder> list(Integer id);
    Page<HouseOrder> pageByAgentId(Integer agentId, Pageable pageable);
}

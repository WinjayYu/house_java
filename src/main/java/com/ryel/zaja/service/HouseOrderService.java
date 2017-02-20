package com.ryel.zaja.service;

import com.ryel.zaja.entity.HouseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jca.cci.core.InteractionCallback;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HouseOrderService extends ICommonService<HouseOrder> {

    HouseOrder create(HouseOrder houseOrder);

    HouseOrder update(HouseOrder houseOrder);

    List<HouseOrder> list(Integer id);

    Page<HouseOrder> pageByAgentId(Integer agentId, Pageable pageable);

    Page<HouseOrder> pageByUserId(Integer agentId, Pageable pageable);

    HouseOrder confirm(Integer userId, Integer houseOrderId);

    List<HouseOrder> findPayedOrderByHouseId(Integer houseId);

    List<HouseOrder> findPayedOrderByAgentId(Integer agentId);

    HouseOrder findByBuyerIdAndOrderId(Integer buyerId, Integer houseOrderId);

    HouseOrder findByAgentIdAndOrderId(Integer agentId, Integer houseOrderId);

    HouseOrder payment(Integer userId, Integer houseOrderId);

    Long count(Integer agentId);

    HouseOrder findByHouseIdAndUserId(Integer houseId, Integer userId);
}

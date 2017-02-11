package com.ryel.zaja.service;

import com.ryel.zaja.entity.PinganOrder;

/**
 * Created by Nathan on 2017/1/15.
 */
public interface PinganOrderService extends ICommonService<PinganOrder> {
    void create(PinganOrder order);

    PinganOrder findByOrderId(Integer orderId);

}

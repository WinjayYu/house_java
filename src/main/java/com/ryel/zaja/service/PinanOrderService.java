package com.ryel.zaja.service;

import com.ryel.zaja.entity.PinanOrder;

/**
 * Created by Nathan on 2017/1/15.
 */
public interface PinanOrderService extends ICommonService<PinanOrder> {
    void create(PinanOrder order);
}

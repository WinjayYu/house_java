package com.ryel.zaja.service;

import com.ryel.zaja.entity.OutCashFlow;

/**
 * Created by Nathan on 2017/2/10.
 */
public interface OutCashFlowService extends ICommonService<OutCashFlow> {
    void create(OutCashFlow cash);
}

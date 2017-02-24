package com.ryel.zaja.service;

import com.ryel.zaja.entity.OutCashFlow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Nathan on 2017/2/10.
 */
public interface OutCashFlowService extends ICommonService<OutCashFlow> {
    void create(OutCashFlow cash);
    Page<OutCashFlow> pageByUserId(Integer userId);
}

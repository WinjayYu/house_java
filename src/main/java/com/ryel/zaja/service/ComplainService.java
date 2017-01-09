package com.ryel.zaja.service;

import com.ryel.zaja.entity.Complain;

/**
 * Created by billyu on 2017/1/9.
 */
public interface ComplainService extends ICommonService<Complain>{

    Complain create(Integer userId, Integer houseOrderId, String content);
}

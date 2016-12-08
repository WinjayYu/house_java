package com.ryel.zaja.service;

import com.ryel.zaja.entity.Click;

/**
 * Created by billyu on 2016/12/7.
 */
public interface ClickService extends ICommonService<Click>{

    Click add(Integer HouseId);
}

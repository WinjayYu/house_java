package com.ryel.zaja.service;

import com.ryel.zaja.entity.Collect;

/**
 * Created by billyu on 2016/12/14.
 */
public interface CollectService {

    Collect create(Integer userid, Integer houseId);

    Collect cancelCollect(Integer userId, Integer houseId);

    Integer countByHouseId(Integer id);
}

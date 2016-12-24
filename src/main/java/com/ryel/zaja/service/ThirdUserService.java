package com.ryel.zaja.service;

import com.ryel.zaja.entity.ThirdUser;

/**
 * Created by billyu on 2016/12/24.
 */
public interface ThirdUserService extends ICommonService<ThirdUser>{


    ThirdUser findByOpenid(Integer openid);
}

package com.ryel.zaja.service;

import com.ryel.zaja.entity.ThirdUser;
import org.apache.poi.ss.formula.functions.T;

/**
 * Created by billyu on 2016/12/24.
 */
public interface ThirdUserService extends ICommonService<ThirdUser>{


    ThirdUser findByOpenid(String openid);

    ThirdUser create(String type, String openid, String head, String nickname);

    ThirdUser bindMobile(String mobile, String openid);

    ThirdUser update(ThirdUser thirdUser);

    //一个账户只能绑定一个微信或者QQ
    ThirdUser check(Integer user, String type);
}

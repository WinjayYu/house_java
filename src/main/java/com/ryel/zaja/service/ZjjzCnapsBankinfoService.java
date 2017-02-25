package com.ryel.zaja.service;

import com.ryel.zaja.entity.ZjjzCnapsBankinfo;


import java.util.List;

/**
 * Created by billyu on 2017/2/8.
 */

public interface ZjjzCnapsBankinfoService extends ICommonService<ZjjzCnapsBankinfo>{
    List<ZjjzCnapsBankinfo> findByBankclscodeAndCitycode(String bankclscode, String citycode);
}

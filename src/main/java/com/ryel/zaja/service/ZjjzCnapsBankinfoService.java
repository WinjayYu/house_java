package com.ryel.zaja.service;

import com.ryel.zaja.entity.ZjjzCnapsBankinfo;

import java.util.List;
import java.util.Map;

/**
 * Created by billyu on 2017/2/8.
 */
public interface ZjjzCnapsBankinfoService extends ICommonService<ZjjzCnapsBankinfo> {
    List<Object> findByBankclscodeAndCitycode(String bankclscode, String citycode);

    List<Object> findByBankclscodeAndCitycodeAndBankname(String bankclscode, String citycode, String bankname);
}

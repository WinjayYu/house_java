package com.ryel.zaja.service;

import com.ryel.zaja.entity.ZjjzCnapsBankinfo;
import com.ryel.zaja.entity.vo.ZjjzCnapsBankinfoVo;

import java.util.List;
import java.util.Map;

/**
 * Created by billyu on 2017/2/8.
 */
public interface ZjjzCnapsBankinfoService extends ICommonService<ZjjzCnapsBankinfo> {
    List<ZjjzCnapsBankinfoVo> findByBankclscodeAndCitycode(String bankclscode, String citycode);

    List<ZjjzCnapsBankinfoVo> findByBankclscodeAndCitycodeAndBankname(String bankclscode, String citycode, String bankname);
}

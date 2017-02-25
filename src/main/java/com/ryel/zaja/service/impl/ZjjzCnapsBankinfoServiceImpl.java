package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.ZjjzCnapsBankinfoDao;
import com.ryel.zaja.entity.ZjjzCnapsBankinfo;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.ZjjzCnapsBankinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by billyu on 2017/2/8.
 */
@Service
public class ZjjzCnapsBankinfoServiceImpl extends AbsCommonService<ZjjzCnapsBankinfo> implements ZjjzCnapsBankinfoService {

    @Autowired
    private ZjjzCnapsBankinfoDao zjjzCnapsBankinfoDao;

    @Override
    public List<ZjjzCnapsBankinfo> findByBankclscodeAndCitycode(String bankclscode, String citycode) {
        return null;
    }

    @Override
    public JpaRepository<ZjjzCnapsBankinfo, Integer> getDao() {
        return zjjzCnapsBankinfoDao;
    }
}

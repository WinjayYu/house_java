package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.SuperBankInfoDao;
import com.ryel.zaja.entity.SuperBankInfo;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.SuperBankInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SuperBankInfoServiceImpl extends AbsCommonService<SuperBankInfo> implements SuperBankInfoService {
    protected final static Logger logger = LoggerFactory.getLogger(SuperBankInfoServiceImpl.class);

    @Autowired
    private SuperBankInfoDao superBankInfoDao;

    @Override
    public JpaRepository<SuperBankInfo, Integer> getDao() {
        return superBankInfoDao;
    }


}

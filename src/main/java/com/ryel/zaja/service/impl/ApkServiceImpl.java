package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.ApkDao;
import com.ryel.zaja.entity.Apk;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.ApkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by billyu on 2017/1/12.
 */
@Service
@Transactional(readOnly = true)
public class ApkServiceImpl extends AbsCommonService<Apk> implements ApkService {

    @Autowired
    private ApkDao apkDao;

    @Override
    public JpaRepository<Apk, Integer> getDao() {
        return apkDao;
    }

    @Override
    public Apk check(String version, String type) {
        return apkDao.findByVersionAndType(version, type);
    }

    @Override
    public Apk findLatestVersion(String type) {
        return apkDao.findLatestVersion(type);
    }

}

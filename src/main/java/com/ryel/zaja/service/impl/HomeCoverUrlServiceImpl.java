package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.HomeCoverUrlDao;
import com.ryel.zaja.entity.HomeCoverUrl;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.HomeCoverUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

/**
 * Created by billyu on 2016/12/15.
 */
@Service
public class HomeCoverUrlServiceImpl extends AbsCommonService<HomeCoverUrl> implements HomeCoverUrlService{


    @Autowired
    HomeCoverUrlDao homeCoverUrlDao;

    @Override
    public HomeCoverUrl find(int id) {
        return homeCoverUrlDao.findOne(id);
    }

    @Override
    public JpaRepository<HomeCoverUrl, Integer> getDao() {
        return getDao();
    }
}

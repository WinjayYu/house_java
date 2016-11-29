package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.AreaDao;
import com.ryel.zaja.entity.community;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */
@Service
public class AreaServiceImpl extends AbsCommonService<community> implements AreaService{

    @Autowired
    private AreaDao areaDao;

    @Override
    public community update(community community) {
        return community;
    }

    @Override
    public List<community> list() {
        return areaDao.findAll();
    }

    @Override
    public Page<community> findByPage(String name, Integer type, int pageNum, int pageSize) {
        return null;
    }

    @Override
    @Transactional
    public community create(community community) {
        community.setAddTime(new Date());
        community.setLastModifiedTime(new Date());
        areaDao.save(community);
        return community;
    }

    @Override
    public JpaRepository<community, Integer> getDao() {
        return null;
    }
}

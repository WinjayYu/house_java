package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.AreaDao;
import com.ryel.zaja.entity.Community;
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
public class AreaServiceImpl extends AbsCommonService<Community> implements AreaService{

    @Autowired
    private AreaDao areaDao;

    @Override
    public Community update(Community community) {
        return community;
    }

    @Override
    public List<Community> list() {
        return areaDao.findAll();
    }

    @Override
    public Page<Community> findByPage(String name, Integer type, int pageNum, int pageSize) {
        return null;
    }

    @Override
    @Transactional
    public Community create(Community community) {
        community.setAddTime(new Date());
        community.setLastModifiedTime(new Date());
        areaDao.save(community);
        return community;
    }

    @Override
    public JpaRepository<Community, Integer> getDao() {
        return null;
    }
}

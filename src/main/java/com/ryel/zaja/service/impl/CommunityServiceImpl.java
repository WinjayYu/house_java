package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.CommunityDao;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.CommunityService;
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
public class CommunityServiceImpl extends AbsCommonService<Community> implements CommunityService {

    @Autowired
    private CommunityDao communityDao;

    @Override
    public Community update(Community community) {
        return community;
    }

    @Override
    public List<Community> list() {
        return communityDao.findAll();
    }

    @Override
    public Page<Community> findByPage(String name, Integer type, int pageNum, int pageSize) {
        return null;
    }

    @Override
    @Transactional
    public Community create(Community community) {
        community.setAddTime(new Date());
        communityDao.save(community);
        return community;
    }

    @Override
    public JpaRepository<Community, Integer> getDao() {
        return null;
    }
}
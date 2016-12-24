package com.ryel.zaja.service.impl;

import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.CommunityDao;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.CommunityService;
import com.ryel.zaja.utils.ClassUtil;
import com.ryel.zaja.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
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
        Community dest  = findByUid(community.getUid());
        ClassUtil.copyProperties(dest, community);
        return save(dest);
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
    public List<Community> findByCity(String city) {
        return communityDao.findByCity(city);
    }

    @Override
    @Transactional
    public void create(Community community) {
        try {
            community.setAddTime(new Date());
            communityDao.save(community);
        }catch (Exception e){
            throw new BizException("小区数据有误");
        }
    }

    @Override
    public Community findByUid(String uid) {
        return communityDao.findByUid(uid);
    }

    @Override
    @Transactional
    public Community createOrUpdateByUid(Community community) {
        if(community == null || StringUtils.isBlank(community.getUid())){
            throw new BizException("","创建/更新小区时，小区信息错误,community:" + JsonUtil.obj2Json(community));
        }
        Community temp = findByUid(community.getUid());
        if(temp == null){
            create(community);
        }else {
            update(community);
        }
        return community;
    }

    @Override
    public JpaRepository<Community, Integer> getDao() {
        return null;
    }
}

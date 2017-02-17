package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.ImgWallDao;
import com.ryel.zaja.entity.ImgWall;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.ImgWallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by billyu on 2017/2/15.
 */
@Service
public class ImgWallServiceImpl extends AbsCommonService<ImgWall> implements ImgWallService {

    @Autowired
    private ImgWallDao imgWallDao;

    @Override
    public JpaRepository<ImgWall, Integer> getDao() {
        return imgWallDao;
    }

    @Override
    public List<ImgWall> findByAgentId(Integer agentId) {
        return imgWallDao.findByAgentId(agentId);
    }

    @Override
    public Long countImg(Integer agentId) {
        return imgWallDao.countImg(agentId);
    }
}

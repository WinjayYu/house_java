package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.ImgWallDao;
import com.ryel.zaja.entity.ImgWall;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.ImgWallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by billyu on 2017/2/15.
 */
@Service
@Transactional(readOnly = true)
public class ImgWallServiceImpl extends AbsCommonService<ImgWall> implements ImgWallService {

    @Autowired
    private ImgWallDao imgWallDao;

    @Override
    public JpaRepository<ImgWall, Integer> getDao() {
        return imgWallDao;
    }

    @Override
    public List<Object> findByAgentId(Integer agentId) {
        List<ImgWall> imgWalls =  imgWallDao.findByAgentId(agentId);
        List<Object> list = new ArrayList<>();
        for(ImgWall imgWall : imgWalls){
            list.add(imgWall.getImg());
        }
        return list;
    }

    @Override
    public Long countImg(Integer agentId) {
        return imgWallDao.countImg(agentId);
    }

    @Override
    @Transactional
    public void deleteByUrl(String url) {
        imgWallDao.deleteByUrl(url);
    }
}

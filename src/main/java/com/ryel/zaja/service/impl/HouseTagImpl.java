package com.ryel.zaja.service.impl;

import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.HouseTagDao;
import com.ryel.zaja.entity.HouseTag;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.HouseTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Nathan on 2016/12/29.
 */
@Service
public class HouseTagImpl extends AbsCommonService<HouseTag> implements HouseTagService {

    @Autowired
    private HouseTagDao tagDao;

    @Override
    public void create(HouseTag tag) {
        if(tagDao.findByTag(tag.getTag()) != null ){
            throw new BizException("房屋标签已存在！");
        }
        save(tag);
    }

    @Override
    public HouseTag findByTag(String tag) {
        return tagDao.findByTag(tag);
    }

    @Override
    public void upDateTagNum(String tag) {
        HouseTag houseTag = tagDao.findByTag(tag);
        if(null == houseTag)
        {
            throw new BizException("房屋标签不存在");
        }
        houseTag.setTagNum(houseTag.getTagNum()+1);
        save(houseTag);
    }

    @Override
    public List<HouseTag> list() {
        return tagDao.findAll();
    }

    @Override
    public JpaRepository<HouseTag, Integer> getDao() {
        return tagDao;
    }
}

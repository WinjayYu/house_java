package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.CollectDao;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.Collect;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.CollectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by billyu on 2016/12/14.
 */
@Service
public class CollectServiceImpl extends AbsCommonService<Collect> implements CollectService{
    protected final static Logger logger = LoggerFactory.getLogger(CollectServiceImpl.class);

    @Autowired
    HouseDao houseDao;

    @Autowired
    UserDao userDao;

    @Autowired
    CollectDao collectDao;

    @Override
    public Integer countByHouseId(Integer id) {
        return collectDao.countByHouseId(id);
    }

    private static final String COLLECT = "10"; // "10"为收藏
    private static final String CANCEL_COLLECT = "20"; // "20"为取消收藏

    @Override
    @Transactional(readOnly = false)
    public Collect create(Integer userId, Integer houseId) {

        Collect collect = collectDao.findByUserIdAndHouseId(userId, houseId);
        //如果已经收藏，抛出异常，如果status为"20"，则修改为"10"
        if(null != collect){
            if("10".equals(collect.getStatus())) {
                throw new RuntimeException("已收藏过！");
            }else{
                collect.setStatus(COLLECT);
                this.save(collect);
            }
        }else {

            collect = new Collect();

            House house = houseDao.findOne(houseId);
            collect.setHouse(house);

            User user = userDao.findOne(userId);
            collect.setUser(user);

            collect.setStatus(COLLECT);

            collectDao.save(collect);
            return collect;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public Collect cancelCollect(Integer userId, Integer houseId) {
        Collect collect = collectDao.findByUserIdAndHouseId(userId, houseId);
        if(null == collect){
            throw new RuntimeException("没有收藏过！");
        }else{
            collect.setStatus(CANCEL_COLLECT);
            collectDao.save(collect);
        }
        return null;
    }

    @Override
    public JpaRepository<Collect, Integer> getDao() {
        return null;
    }
}

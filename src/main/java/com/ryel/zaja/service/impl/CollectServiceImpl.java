package com.ryel.zaja.service.impl;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.CollectDao;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.Collect;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.CollectService;
import com.ryel.zaja.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by billyu on 2016/12/14.
 */
@Service
@Transactional(readOnly = true)
public class CollectServiceImpl extends AbsCommonService<Collect> implements CollectService{
    protected final static Logger logger = LoggerFactory.getLogger(CollectServiceImpl.class);

    @Autowired
    HouseDao houseDao;

    @Autowired
    UserDao userDao;

    @Autowired
    CollectDao collectDao;

    @Autowired
    private RedisService redisService;


    @Override
    public Integer countByHouseId(Integer id) {
        return collectDao.countByHouseId(id);
    }


    @Override
    @Transactional
    public Collect create(Integer userId, Integer houseId) {

        Collect collect = collectDao.findByUserIdAndHouseId(userId, houseId);
        if(null != collect){
            throw new BizException(Error_code.ERROR_CODE_0021);
        }

        House house = houseDao.findOne(houseId);

            collect = new Collect();
            collect.setHouse(house);

            User user = userDao.findOne(userId);
            collect.setUser(user);

            collectDao.save(collect);
            return collect;

    }

    @Override
    @Transactional
    public Collect cancelCollect(Integer userId, Integer houseId) {
        Collect collect = collectDao.findByUserIdAndHouseId(userId, houseId);
        if(null == collect){
            throw new BizException("没有收藏过！");
        }else{
            collectDao.delete(collect);
        }
        return null;
    }

    @Override
    public boolean check(Integer userId, Integer houseId) {
            Collect collect = collectDao.findByUserIdAndHouseId(userId, houseId);

            if(null == collect){
                return false;//未收藏
            }else{
                return true;//已收藏
            }

    }

    @Override
    public Page<House> pageByUserId(Integer userId, Integer pageNum, Integer pageSize) {
        return collectDao.pageByUserId(userId, new PageRequest(pageNum-1,pageSize, Sort.Direction.DESC, "id" ));
    }

    @Override
    public JpaRepository<Collect, Integer> getDao() {
        return null;
    }
}

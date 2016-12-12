package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.BuyHouseDao;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.entity.SendOrder;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.BuyHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by burgl on 2016/8/27.
 */
@Service
public class BuyHouseServiceImpl extends AbsCommonService<BuyHouse> implements BuyHouseService {

    @Autowired
    private BuyHouseDao buyHouseDao;

    @Override
    @Transactional
    public BuyHouse create(BuyHouse buyHouse) {
        buyHouse.setAddTime(new Date());
        buyHouse.setLastModifiedTime(new Date());
        return this.save(buyHouse);
    }


    @Override
    public JpaRepository<BuyHouse, Integer> getDao() {
        return buyHouseDao;
    }

    @Override
    public List<BuyHouse> findByUserId(int userId) {
        List<BuyHouse> buyHouses = buyHouseDao.findByUserId(userId);
        return buyHouses;
    }

    @Override
    public Page<BuyHouse> findByPage(Integer userId, int pageNum, int pageSize) {

        Page<BuyHouse> page = buyHouseDao.findByUserId(userId,new PageRequest(pageNum-1,pageSize, Sort.Direction.DESC, "id"));
        return page;
    }
}

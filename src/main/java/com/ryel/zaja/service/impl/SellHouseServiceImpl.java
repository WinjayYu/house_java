package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.SellHouseDao;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.SellHouseService;
import com.ryel.zaja.utils.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by burgl on 2016/8/27.
 */
@Service
public class SellHouseServiceImpl extends AbsCommonService<SellHouse> implements SellHouseService {

    @Autowired
    private SellHouseDao sellHouseDao;


    @Override
    @Transactional
    public SellHouse create(SellHouse sellHouse) {
        sellHouse.setAddTime(new Date());
        sellHouse.setLastModifiedTime(new Date());
        return this.save(sellHouse);
    }


    @Override
    public JpaRepository<SellHouse, Integer> getDao() {
        return sellHouseDao;
    }


    @Override
    public Page<SellHouse> pageByUserId(int userId, int pageNum, int pageSize) {
        return sellHouseDao.findByUserId(userId, new PageRequest(pageNum-1,pageSize));
    }

    @Override
    public Page<SellHouse> pageAll(int pageNum, int pageSize) {
        return sellHouseDao.pageAll(new PageRequest(pageNum-1,pageSize, Sort.Direction.DESC, "id"));
    }


    @Override
    public Page<SellHouse> agentPage(Integer pageNum, Integer pageSize, List<String> uids, List<Integer> list) {

        return sellHouseDao.agentPage(uids, list, new PageRequest(pageNum-1,pageSize, Sort.Direction.DESC, "id"));
    }

    @Override
    public List<Integer> findByUserIdAsId(Integer userId) {

        return sellHouseDao.findByUserIdAsId(userId);
    }

    @Override
    public Long count(Integer demandId) {
        return sellHouseDao.count(demandId);
    }

    @Override
    public SellHouse update(SellHouse sellHouse) {
        SellHouse origUser = findById(sellHouse.getId());
        ClassUtil.copyProperties(origUser, sellHouse);
        return sellHouseDao.save(origUser);
    }
}

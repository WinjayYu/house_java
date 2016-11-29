package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.HouseService;
import com.ryel.zaja.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */
@Service
public class HouseServiceImpl extends AbsCommonService<House> implements HouseService{

    @Autowired
    private HouseDao houseDao;

    @Override
    public House create(House house) {
        return null;
    }

    @Override
    public List<House> list() {
        return null;
    }

    @Override
    public House update(House house) {
        return null;
    }

    @Override
    public JpaRepository<House, Integer> getDao() {
        return null;
    }
}

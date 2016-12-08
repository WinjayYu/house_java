package com.ryel.zaja.service.impl;

import com.ryel.zaja.controller.api.SellHouseApi;
import com.ryel.zaja.entity.Click;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.ClickService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by billyu on 2016/12/7.
 */
@Service
public class ClickServiceImpl extends AbsCommonService<Click> implements ClickService{
    protected final static Logger logger = LoggerFactory.getLogger(ClickServiceImpl.class);

    @Override
    public Click add(Integer HouseId) {
        return null;
    }

    @Override
    public JpaRepository<Click, Integer> getDao() {
        return getDao();
    }
}

package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.OutCashFlowDao;
import com.ryel.zaja.entity.HouseTag;
import com.ryel.zaja.entity.OutCashFlow;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.HouseTagService;
import com.ryel.zaja.service.OutCashFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Nathan on 2017/2/10.
 */
@Service
@Transactional(readOnly = true)
public class OutCashFlowServiceImpl  extends AbsCommonService<OutCashFlow> implements OutCashFlowService {
    @Autowired
    private OutCashFlowDao outCashFlowDao;


    @Override
    @Transactional
    public void create(OutCashFlow cash) {
        cash.setAddTime(new Date());
        save(cash);
    }

    @Override
    public JpaRepository<OutCashFlow, Integer> getDao() {
        return outCashFlowDao;
    }
}

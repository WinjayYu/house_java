package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.AgentBuyHouseDao;
import com.ryel.zaja.dao.ThirdUserDao;
import com.ryel.zaja.entity.AgentBuyHouse;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.ThirdUser;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.AgentBuyHouseService;
import com.ryel.zaja.service.ThirdUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ThirdUserServiceImpl extends AbsCommonService<ThirdUser> implements ThirdUserService {

    @Autowired
    private ThirdUserDao thirdUserDao;


    @Override
    public JpaRepository<ThirdUser, Integer> getDao() {
        return thirdUserDao;
    }


    @Override
    public ThirdUser findByOpenid(Integer openid) {
        return null;
    }
}

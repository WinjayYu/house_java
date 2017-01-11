package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.ComplainDao;
import com.ryel.zaja.dao.HouseOrderDao;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.Complain;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.ComplainService;
import com.ryel.zaja.service.HouseOrderService;
import com.ryel.zaja.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by billyu on 2017/1/9.
 */
@Service
@Transactional(readOnly = true)
public class ComplainServiceImpl extends AbsCommonService<Complain> implements ComplainService {

    @Autowired
    UserService userService;

    @Autowired
    HouseOrderService houseOrderService;

    @Autowired
    ComplainDao complainDao;

    @Override
    public JpaRepository<Complain, Integer> getDao() {
        return complainDao;
    }


    @Override
    @Transactional
    public Complain create(Integer userId, Integer houseOrderId, String content) {
        Complain complain = new Complain();
        complain.setUser(userService.findById(userId));
        complain.setHouseOrder(houseOrderService.findById(houseOrderId));
        complain.setContent(content);

        return complainDao.save(complain);
    }
}

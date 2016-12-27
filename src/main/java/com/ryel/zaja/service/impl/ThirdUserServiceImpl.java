package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.ThirdUserDao;
import com.ryel.zaja.entity.ThirdUser;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.ThirdUserService;
import com.ryel.zaja.utils.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ThirdUser findByOpenid(String openid) {
        return thirdUserDao.findByOpenid(openid);
    }

    @Override
    @Transactional
    public ThirdUser create(String type, String openid, String head, String nickname) {

        ThirdUser thirdUser = new ThirdUser();
        thirdUser.setType(type);
        thirdUser.setNickname(nickname);
        thirdUser.setOpenid(openid);
        thirdUser.setHead(head);
        return this.save(thirdUser);
    }

    @Override
    @Transactional
    public ThirdUser bindMobile(String mobile, String openid) {
//        return thirdUserDao.bindMobile(mobile, openid);
        return null;
    }

    @Override
    @Transactional
    public ThirdUser update(ThirdUser thirdUser) {
/*        ThirdUser origThirdUser = thirdUserDao.findByOpenid(thirdUser.getOpenid());
        ClassUtil.copyProperties(origThirdUser, thirdUser);*/
        return save(thirdUser);
    }
}

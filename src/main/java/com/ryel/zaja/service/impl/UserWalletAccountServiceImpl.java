package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.UserWalletAccountDao;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.entity.UserWalletAccount;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.UserWalletAccountService;
import com.ryel.zaja.utils.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserWalletAccountServiceImpl extends AbsCommonService<UserWalletAccount> implements UserWalletAccountService {
    protected final static Logger logger = LoggerFactory.getLogger(UserWalletAccountServiceImpl.class);

    @Autowired
    private UserWalletAccountDao userWalletAccountDao;

    @Override
    public UserWalletAccount create(UserWalletAccount userWalletAccount) {
        return userWalletAccountDao.save(userWalletAccount);
    }

    @Override
    @Transactional
    public UserWalletAccount update(UserWalletAccount userWalletAccount) {
        UserWalletAccount dest  = findById(userWalletAccount.getId());
        ClassUtil.copyProperties(dest, userWalletAccount);
        return save(dest);
    }

    @Override
    public JpaRepository<UserWalletAccount, Integer> getDao() {
        return userWalletAccountDao;
    }


}

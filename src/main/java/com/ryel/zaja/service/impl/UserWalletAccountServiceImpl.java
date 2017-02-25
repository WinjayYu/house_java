package com.ryel.zaja.service.impl;

import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.UserWalletAccountDao;
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
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("UserWalletAccountServiceImpl")
@Transactional(readOnly = true)
public class UserWalletAccountServiceImpl extends AbsCommonService<UserWalletAccount> implements UserWalletAccountService {
    protected final static Logger logger = LoggerFactory.getLogger(UserWalletAccountServiceImpl.class);

    @Autowired
    private UserWalletAccountDao userWalletAccountDao;

    @Override
    public UserWalletAccount findByUserId(Integer userId) {
        List<UserWalletAccount> list = userWalletAccountDao.findByUserId(userId);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }else {
            if(list.size() > 1){
                throw new BizException("查询到多条UserWalletAccount,userId=" + userId);
            }else {
                return list.get(0);
            }
        }
    }

    @Override
    @Transactional
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

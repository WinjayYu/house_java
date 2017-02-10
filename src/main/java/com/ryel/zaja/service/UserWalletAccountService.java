package com.ryel.zaja.service;

import com.ryel.zaja.entity.UserWalletAccount;

import java.util.List;

public interface UserWalletAccountService extends ICommonService<UserWalletAccount> {
    List<UserWalletAccount> findByUserId(Integer userId);
    UserWalletAccount create(UserWalletAccount userWalletAccount);
    UserWalletAccount update(UserWalletAccount userWalletAccount);

    UserWalletAccount findByACcId(String aCcId);

}

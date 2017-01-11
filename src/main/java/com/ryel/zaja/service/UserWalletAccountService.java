package com.ryel.zaja.service;

import com.ryel.zaja.entity.UserWalletAccount;

public interface UserWalletAccountService extends ICommonService<UserWalletAccount> {
    UserWalletAccount create(UserWalletAccount userWalletAccount);
    UserWalletAccount update(UserWalletAccount userWalletAccount);

}

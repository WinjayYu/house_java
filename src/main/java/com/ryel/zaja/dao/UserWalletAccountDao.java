package com.ryel.zaja.dao;

import com.ryel.zaja.entity.UserWalletAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWalletAccountDao extends JpaRepository<UserWalletAccount, Integer> ,JpaSpecificationExecutor<UserWalletAccount> {


}

package com.ryel.zaja.dao;

import com.ryel.zaja.entity.UserWalletAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWalletAccountDao extends JpaRepository<UserWalletAccount, Integer> ,JpaSpecificationExecutor<UserWalletAccount> {
    List<UserWalletAccount> findByUserId(Integer userId);
}

package com.ryel.zaja.dao;

import com.ryel.zaja.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Created by burgl on 2016/8/16.
 */
@Repository
public interface UserDao extends JpaRepository<User, Integer>,JpaSpecificationExecutor<User> {

    @Query("select u from User u where u.mobile = ?1")
    User findByMobile(String mobile);

    User findByMobileAndPassword(String mobile, String password);


}

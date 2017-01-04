package com.ryel.zaja.dao;

import com.ryel.zaja.entity.User;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Created by burgl on 2016/8/16.
 */
@Repository
public interface UserDao extends JpaRepository<User, Integer>,JpaSpecificationExecutor<User> {

    @Cacheable
    User findByMobile(String mobile);

    User findByMobileAndPassword(String mobile, String password);

    User findByMobileAndPasswordAndType(String mobile, String password,String type);


}

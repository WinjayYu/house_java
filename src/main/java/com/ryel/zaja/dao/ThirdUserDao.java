package com.ryel.zaja.dao;

import com.ryel.zaja.entity.ThirdUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Created by billyu on 2016/12/7.
 */
@Repository
public interface ThirdUserDao extends JpaRepository<ThirdUser, Integer>,JpaSpecificationExecutor<ThirdUser> {


    ThirdUser findByOpenid(String openid);

    ThirdUser findByUserAndType(Integer userId, String type);
}

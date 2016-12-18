package com.ryel.zaja.service;

import com.ryel.zaja.entity.AgentMaterial;
import com.ryel.zaja.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */

public interface UserService extends ICommonService<User>{

    User create(User user);

    User login(String mobile, String password);

    User agentLogin(String mobile, String password);

    User update(User user);

    User findByMobile(String mobile);


    /**
     * 获取所有用户列表
     * @return
     */
    List<User> list();



    Page<User> findByPage(String name, int pageNum, int pageSize);

    void agentRegister(User user, AgentMaterial agentMaterial, String verifyCode);
}

package com.ryel.zaja.service;

import com.ryel.zaja.entity.AgentMaterial;
import com.ryel.zaja.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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



    Page<User> findByPage(int pageNum, int pageSize, String name);

    Page<User> mgtPageAgent(int pageNum, int pageSize,String name);

    User agentRegister(User user, AgentMaterial agentMaterial, String verifyCode,
                       MultipartFile positive, MultipartFile negative, MultipartFile companyPic);

    void applyAgent(Integer userId, AgentMaterial agentMaterial,
                    MultipartFile positive, MultipartFile negative, MultipartFile companyPic);

    //获取未审核的经纪人信息
    Page<User> unCheckAgent(int pageNum, int pageSize);
    //获取所有经纪人/或者用户
    Page<User> userList(int pageNum, int pageSize, String usertype);
}

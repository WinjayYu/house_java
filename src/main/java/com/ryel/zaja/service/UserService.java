package com.ryel.zaja.service;

import com.ryel.zaja.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */
public interface UserService extends ICommonService<User>{

    public User create(User user);

    public User login(String mobile, String password);

    public User update(User user);


    /**
     * 获取所有用户列表
     * @return
     */
    public List<User> list();



    public Page<User> findByPage(String name, final Integer type, int pageNum, int pageSize);
}

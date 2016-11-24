package com.ryel.springBootJpa.service;

import com.ryel.springBootJpa.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by burgl on 2016/8/16.
 */
public interface UserService extends ICommonService<User>{

    public User create(User user);

    public User login(String username, String password);

    public User update(User user);

    /**
     * 关注用户
     * @param userId
     * @param fllowUserId
     */
    public void watch(Integer userId, Integer fllowUserId);

    /**
     * 取消关注用户
     * @param userId
     * @param fllowUserId
     */
    public void unwatch(Integer userId, Integer fllowUserId);

    /**
     * 获取所有用户列表
     * @return
     */
    public List<User> list();

    /**
     * 获取普通注册用户列表(非主席和讲师)
     * @return
     */
    public List<User> registUser();

    /**
     * 获取关注的用户列表
     * @param userId
     * @return
     */
    public List<User> watchlist(Integer userId);

    /**
     * 获取关注我的用户列表
     * @param userId
     * @return
     */
    public List<User> fllowlist(Integer userId);



    //TODO: 导出接机信息




    public Page<User> findByPage(String name, final Integer type, int pageNum, int pageSize);
}

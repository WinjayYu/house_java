package com.ryel.springBootJpa.pojo;

import com.ryel.springBootJpa.entity.User;

/**
 * Author: koabs
 * 9/4/16.
 */
public class UserVo extends User{

    //用户关注的用户
    private Boolean watch;

    //关注用户的用户
    private Boolean fllow;

}

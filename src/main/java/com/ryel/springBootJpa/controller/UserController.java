package com.ryel.springBootJpa.controller;


import com.ryel.springBootJpa.config.bean.Result;
import com.ryel.springBootJpa.entity.User;
import com.ryel.springBootJpa.service.UserService;
import com.ryel.springBootJpa.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by burgl on 2016/8/28.
 */
@Controller
@RequestMapping(value = "user")
public class UserController  extends BaseController{

    @Autowired
    private UserService userService;


    @RequestMapping("/index")
    public String index(HttpServletRequest request,
                        HttpServletResponse response){
        return "用户列表";
    }





    /**
     * 删除管理员帐号
     * @param request
     * @param response
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(HttpServletRequest request,
                         HttpServletResponse response,
                         String ids){
        int[] arrayId = JsonUtil.json2Obj(ids, int[].class);
        userService.deleteByIds(arrayId);
        return Result.success().msg("用户删除成功!");
    }



    @RequestMapping("/save")
    @ResponseBody
    public Result save(HttpServletRequest request,
                       HttpServletResponse response,
                       User user){
        user.setType(9);
        if(user.getId()==null){
            userService.create(user);
        }else{
            userService.update(user);
        }
        return Result.success().msg("用户操作成功!");
    }









}

package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by billyu on 2016/11/28.
 */
@RestController
@RequestMapping(value = "/api/area/")
public class CommunityApi {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserDao userDao;

    /**
     * 创建小区
     * @param
     * @return
     */
    @RequestMapping(value = "createarea")
    public Result createArea(@RequestBody Community community){
        try{
            communityService.create(community);
        }catch (Exception e){
            return Result.error().msg("error_6");
        }
        return Result.success().msg("");
    }

    @RequestMapping(value = "arealist")
    public Result areaList(){
        return Result.success().msg("").data(communityService.list());
    }

}

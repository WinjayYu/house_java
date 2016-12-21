package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
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
@RequestMapping(value = "/api/community/")
public class CommunityApi {

    @Autowired
    private CommunityService communityService;


    /**
     * 创建小区
     * @param
     * @return
     */
    @RequestMapping(value = "createCommunity", method = RequestMethod.POST)
    public Result createArea(Community community){
        try{
            communityService.create(community);
        }catch (Exception e){
            return Result.error().msg(Error_code.ERROR_CODE_0025);//操作失败
        }
        return Result.success().msg("");
    }

    @RequestMapping(value = "communitylist")
    public Result areaList(){
        return Result.success().msg("").data(communityService.list());
    }

}

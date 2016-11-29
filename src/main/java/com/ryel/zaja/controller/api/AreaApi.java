package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.community;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by billyu on 2016/11/28.
 */
@RestController
@RequestMapping(value = "/api/area/")
public class AreaApi {

    @Autowired
    private AreaService areaService;

    @Autowired
    private UserDao userDao;

    /**
     * 创建小区
     * @param
     * @return
     */
    @RequestMapping(value = "createarea")
    public Result createArea(@RequestBody community community){
        try{

            User user = new User(1001);
            community.setAddBy(user);
            areaService.create(community);
        }catch (Exception e){
            return Result.error().msg("error_6");
        }
        return Result.success().msg("");
    }

    @RequestMapping(value = "arealist")
    public Result areaList(){
        return Result.success().msg("").data(areaService.list());
    }

}

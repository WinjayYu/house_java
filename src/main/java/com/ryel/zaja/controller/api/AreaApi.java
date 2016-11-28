package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.entity.Area;
import com.ryel.zaja.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by billyu on 2016/11/28.
 */
@RestController
@RequestMapping(value = "/api/area/")
public class AreaApi {

    @Autowired
    private AreaService areaService;

    /**
     * 创建小区
     * @param area
     * @return
     */
    @RequestMapping(value = "createarea")
    public Result createArea(@RequestBody Area area){
        try{
            areaService.create(area);
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

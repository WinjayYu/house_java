package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billyu on 2016/12/14.
 */
@RestController
@RequestMapping(value = "api/collect")
public class CollectApi {
    protected final static Logger logger = LoggerFactory.getLogger(CollectApi.class);

    @Autowired
    CollectService collectService;

    @RequestMapping(value = "collect",method = RequestMethod.POST)
    public Result collect(Integer userId, Integer houseId){

        try{
            collectService.create(userId, houseId);
        }catch (RuntimeException e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0021).data(new HashMap<>());
        }
        return Result.success().msg("").data(new HashMap<>());
    }

    @RequestMapping(value = "cancelCollect")
    public Result cancelCollect(Integer userId, Integer houseId){
        try{
            collectService.cancelCollect(userId, houseId);
        }catch (RuntimeException e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0016).data(new HashMap<>());
        }
        return null;
    }

    //点击房源详情的时候调用此接口，检查是否收藏
    @RequestMapping(value = "check", method = RequestMethod.POST)
    public Result check(Integer userId, Integer houseId){
        Map<String, String> map = new HashMap<String,String>();
        String status = "0";
        if(collectService.check(userId, houseId)){
            status = "0";//已收藏
        }else{
            status = "1";//未收藏
        }
        map.put("status", status);
        return Result.success().msg("").data(map);
    }
}

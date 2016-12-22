package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.entity.Collect;
import com.ryel.zaja.service.CollectService;
import com.ryel.zaja.utils.APIFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    /**
     * 收藏
     * @param userId
     * @param houseId
     * @return
     */
    @RequestMapping(value = "collect",method = RequestMethod.POST)
    public Result collect(Integer userId, Integer houseId){

        try{
            collectService.create(userId, houseId);
        }catch (BizException be){
            return Result.error().msg(Error_code.ERROR_CODE_0021).data(new HashMap<>());
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
        return Result.success().msg("").data(new HashMap<>());
    }

    /**
     * 取消收藏
     * @param userId
     * @param houseId
     * @return
     */
    @RequestMapping(value = "cancelcollect")
    public Result cancelCollect(Integer userId, Integer houseId){
        try{
            collectService.cancelCollect(userId, houseId);
            return Result.success().data(new HashMap<>());
        }catch (BizException be){
            logger.error(be.getMessage(), be);
            return Result.error().msg(Error_code.ERROR_CODE_0016).data(new HashMap<>());
        }
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

    /**
     * 我的收藏列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "listcollect", method = RequestMethod.POST)
    public Result listCollect(Integer userId, Integer pageNum, Integer pageSize){

        try{
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<Collect> page = collectService.pageByUserId(userId, pageNum, pageSize);
            if(null != page) {
                Map<String, Object> dataMap = APIFactory.fitting(page);
                return Result.success().data(dataMap);
            }else{
                return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
    }
}

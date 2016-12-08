package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.entity.House;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by billyu on 2016/12/3.
 */
@RestController
@RequestMapping(value = "/api/home")
public class HomeApi {

    protected final static Logger logger = LoggerFactory.getLogger(SellHouseApi.class);


    @RequestMapping(value = "home", method = RequestMethod.POST)
    public Result home(@RequestParam(value = "userId", required = false) Integer userId,
                       @RequestParam(value = "longitude", required = false) String longitude,
                       @RequestParam(value = "latitude", required = false) String latitude){

        if(null != userId){
            recommend(userId);
        }
        return null;
    }

    public List<House> recommend(int userId) {
    return null;
    }
}

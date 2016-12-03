package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.service.BuyHouseService;
import com.ryel.zaja.service.SellHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by billyu on 2016/11/30.
 * 买房
 */
@RestController(value = "UserCenterApi")
@RequestMapping("/api/buyhouse/")
public class BuyHouseApi {


    @Autowired
    private BuyHouseService buyHouseService;

    @RequestMapping(value = "listbuyhouses", method = RequestMethod.POST)
    public Result listBuyHouses(@RequestParam("userId") Integer userId) {
        if (userId == null) {
            return Result.error().msg("id为空！");
        }
        List<BuyHouse> list;
        Result result;
        Map<String, Object> map = new HashMap<>();
        try {
            list = buyHouseService.findByUserId(userId);
            map.put("list",list);
            result = Result.success().data(map);
        } catch (Exception e) {
            return Result.error().msg("error_4");
        }
        return result;
    }
}

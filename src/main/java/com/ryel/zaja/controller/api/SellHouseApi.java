package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.service.SellHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by billyu on 2016/11/30.
 *
 * 卖房
 */
@RestController(value = "sellHouse")
@RequestMapping("/api/sellhouse/")
public class SellHouseApi {


    @Autowired
    private SellHouseService sellHouseService;

    @RequestMapping(value = "listsellhouses", method = RequestMethod.POST)
    public Result allSellHouses(@RequestBody SellHouse sellHouse) {

        List<SellHouse> list;
        Result result;
        Map<String, Object> map = new HashMap<>();
        try {
            list = sellHouseService.findByUserId(sellHouse.getId());
            map.put("list",list);
            result = Result.success().data(map);
        } catch (Exception e) {
            return Result.error().msg("error_4");
        }
        return result;
    }

}

package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.service.SellHouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestController()
@RequestMapping("/api/sellhouse/")
public class SellHouseApi {
    protected final static Logger logger = LoggerFactory.getLogger(SellHouseApi.class);

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
            logger.error(e.getMessage(),e);
            return Result.error().msg(Error_code.ERROR_CODE_0014);
        }
        return result;
    }

}

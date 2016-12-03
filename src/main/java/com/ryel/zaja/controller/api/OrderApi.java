package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.CommunityDao;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.HouseOrderDao;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.exception.HouseException;
import com.ryel.zaja.exception.UserException;
import com.ryel.zaja.service.HouseOrderService;
import com.ryel.zaja.service.HouseService;
import com.ryel.zaja.service.SendOrderService;
import com.ryel.zaja.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order/")
public class OrderApi {

    @Value("${pro.upload.url}")
    private String uploadUrl;

    @Autowired
    private HouseOrderService houseOrderService;

    @Autowired
    private HouseOrderDao houseOrderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseDao houseDao;

    @Autowired
    private CommunityDao communityDao;

    //创建订单
    @RequestMapping(value = "createorder")
    public Result createOrder(@RequestBody HouseOrder houseOrder) {
        try {
            houseOrder.setAddTime(new Date());
            houseOrderService.create(houseOrder);
        } catch (Exception e) {
            return Result.error().msg("error_6");
        }
        return Result.success().msg("");
    }

    //获取某一个订单的详情
    @RequestMapping(value = "oneorder")
    public Result oneOrder(@RequestBody HouseOrder houseOrder) {

        HouseOrder origHouseOrder = houseOrderService.findById(houseOrder.getId());
        if (origHouseOrder == null) {
            return Result.error().msg("error_5");
        }

        return Result.success().msg("").data(origHouseOrder);

    }

    @RequestMapping(value = "listorder", method = RequestMethod.POST)
    public Result listOrder(@RequestBody HouseOrder houseOrder) {

        List<HouseOrder> list;
        Result result;
        Map<String, Object> map = new HashMap<>();

        list = houseOrderService.list(houseOrder.getId());
        map.put("list", list);
        result = Result.success().data(map);
        if (list == null) {
            return Result.error().msg("error_4");
        }
        return result;
    }




}

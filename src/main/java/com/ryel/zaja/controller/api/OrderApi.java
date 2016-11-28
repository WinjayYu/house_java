package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.HouseOrderDao;
import com.ryel.zaja.entity.HouseOrder;
import com.ryel.zaja.service.HouseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/order/")
public class OrderApi {

    @Value("${pro.upload.url}")
    private String uploadUrl;

    @Autowired
    private HouseOrderService houseOrderService;

    @Autowired
    private HouseOrderDao houseOrderDao;

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
    @RequestMapping(value = "getorder")
    public Result getOrder(@RequestBody HouseOrder houseOrder){
        HouseOrder houseOrder1;
        try{
            houseOrder1 = houseOrderService.findById(houseOrder.getId());
        } catch(Exception e){
            return Result.error().msg("error_4");
        }
        return Result.success().msg("").data(houseOrder1);

    }


}

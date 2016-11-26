package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.entity.HouseOrder;
import com.ryel.zaja.exception.UserException;
import com.ryel.zaja.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/order/")
public class OrderApi {

    @Value("${pro.upload.url}")
    private String uploadUrl;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "create")
    public Result create(@RequestBody HouseOrder houseOrder) {
        try {
            houseOrder.setAddTime(new Date());
            houseOrder.setStatus("10");
            orderService.create(houseOrder);
        } catch (UserException e) {
            return Result.error().msg("邮箱或者手机已经存在!");
        }
        return Result.success().msg("successs!");
    }



}

package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.entity.Order;
import com.ryel.zaja.exception.UserException;
import com.ryel.zaja.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: koabs
 * 8/22/16.
 * app 客户端用户登入,注册等功能.
 */
@RestController
@RequestMapping("/api/order/")
public class OrderApi {

    @Value("${pro.upload.url}")
    private String uploadUrl;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "create")
    public Result create(Order order) {
        try {
            orderService.create(order);
        } catch (UserException e) {
            return Result.error().msg("邮箱或者手机已经存在!");
        }
        return Result.success().msg("successs!");
    }



}

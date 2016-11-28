package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.OrderDao;
import com.ryel.zaja.entity.HouseOrder;
import com.ryel.zaja.exception.UserException;
import com.ryel.zaja.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/api/order/")
public class OrderApi {

    @Value("${pro.upload.url}")
    private String uploadUrl;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    //创建订单
    @RequestMapping(value = "createorder")
    public Result createOrder(@RequestBody HouseOrder houseOrder) {
        try {
            houseOrder.setAddTime(new Date());
            orderService.create(houseOrder);
        } catch (Exception e) {
            return Result.error().msg("");
        }
        return Result.success().msg("successs!");
    }

    //获取某一个订单的详情
    @RequestMapping(value = "getorder")
    public Result getOrder(@RequestBody HouseOrder houseOrder){
        HouseOrder houseOrder1;
        try{
            houseOrder1 = orderService.findById(houseOrder.getId());
        } catch(Exception e){
            return Result.error().msg("error_4");
        }
        return Result.success().msg("").data(houseOrder1);

    }


}

package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
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
    private HouseService houseService;

    //创建订单
    @RequestMapping(value = "createorder")
    public Result createOrder(HouseOrder houseOrder) {
        try {
            houseOrder.setAddTime(new Date());
            houseOrderService.create(houseOrder);
        } catch (Exception e) {
            return Result.error().msg(Error_code.ERROR_CODE_0019);//操作失败
        }
        return Result.success().msg("");
    }

    //获取某一个订单的详情
    @RequestMapping(value = "oneorder")
    public Result oneOrder(HouseOrder houseOrder) {

        HouseOrder origHouseOrder = houseOrderService.findById(houseOrder.getId());
        if (origHouseOrder == null) {
            return Result.error().msg(Error_code.ERROR_CODE_0006);//手机号被占用
        }

        return Result.success().msg("").data(origHouseOrder);

    }

    @RequestMapping(value = "listorder", method = RequestMethod.POST)
    public Result listOrder(Integer id) {

        List<HouseOrder> list;
        Result result;
        Map<String, Object> map = new HashMap<>();

        list = houseOrderService.list(id);
        map.put("list", list);
        result = Result.success().data(map);
        if (list == null) {
            return Result.error().msg(Error_code.ERROR_CODE_0014);
        }
        return result;
    }




}

package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.CommunityDao;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.HouseOrderDao;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.service.HouseOrderService;
import com.ryel.zaja.service.HouseService;
import com.ryel.zaja.utils.APIFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order/")
public class OrderApi {
    protected final static Logger logger = LoggerFactory.getLogger(OrderApi.class);


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
            return Result.error().msg(Error_code.ERROR_CODE_0025);
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
    public Result listOrder(Integer userId, Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }

            Map<String, Object> map = new HashMap<>();
            Page<HouseOrder> page = houseOrderService.pageByUserId(userId, new PageRequest(pageNum-1, pageSize, Sort.Direction.DESC, "id"));

            if (page == null) {
                return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
            }

            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        }catch (Exception e){
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
    }

    /**
     * 确认房屋交接
     * @param userId
     * @param houseOrderId
     * @return
     */
    public Result confirm(Integer userId, Integer houseOrderId){
        try{
            houseOrderService.confirm(userId, houseOrderId);
            return Result.success().msg("").data(new HashMap<>());
        }catch (BizException be){
            logger.error(be.getMessage(), be);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }


}

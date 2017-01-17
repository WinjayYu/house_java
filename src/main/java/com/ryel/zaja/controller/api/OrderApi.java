package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.HouseOrderStatus;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.CommunityDao;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.HouseOrderDao;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.service.ComplainService;
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

    @Autowired
    private ComplainService complainService;

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

    /**
     * 订单列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
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


            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        }catch (Exception e){
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
    }

    /**
     * 确认房屋交接，状态值从"40"变成"50"
     * @param userId
     * @param houseOrderId
     * @return
     */
    @RequestMapping(value = "confirm", method = RequestMethod.POST)
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


    /**
     * 申请退款,状态值从"30"变成"31"
     * @param buyerId
     * @param houseOrderId
     * @return
     */
    @RequestMapping(value = "rebate", method = RequestMethod.POST)
    public Result rebate(@RequestParam(value = "userId") Integer buyerId, Integer houseOrderId){
        return changeStatus(buyerId, houseOrderId,HouseOrderStatus.APPLY_REBATE.getCode());
    }

    /**
     * 用户确认接单,状态值从"10"变成"20"
     * @param userId
     * @param houseOrderId
     * @return
     */
    @RequestMapping(value = "receiveorder", method = RequestMethod.POST)
    public Result receiveOrder(Integer userId, Integer houseOrderId){
        return changeStatus(userId, houseOrderId,HouseOrderStatus.WAIT_PAYMENT.getCode());
    }


    /**
     * 用户支付,状态值从"20"变成"30"
     * @param userId
     * @param houseOrderId
     * @return
     */
    @RequestMapping(value = "payment", method = RequestMethod.POST)
    public Result payment(Integer userId, Integer houseOrderId){

        try{
            houseOrderService.payment(userId, houseOrderId);
            return Result.success().msg("").data(new HashMap<>());
        }catch (BizException be){
            logger.error(be.getMessage(), be);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }

    }

    /**
     * 用户拒绝接收订单,状态值从"10"变成"11"
     * @param userId
     * @param houseOrderId
     * @return
     */
    @RequestMapping(value = "rejectorder", method = RequestMethod.POST)
    public Result reject(Integer userId, Integer houseOrderId){

        return changeStatus(userId, houseOrderId,HouseOrderStatus.REJECT.getCode());

    }



    /**
     * 用户投诉,订单状态值从"10"变成"12"，投诉内容写进数据库
     * @param userId
     * @param houseOrderId
     * @return
     */
    @RequestMapping(value = "complain", method = RequestMethod.POST)
    public Result complain(Integer userId, Integer houseOrderId, String content){
        try{
            complainService.create(userId, houseOrderId, content);
            return changeStatus(userId, houseOrderId,HouseOrderStatus.COMPLAIN.getCode());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }

    }




    /**
     * 更改房屋的状态值
     * @param userId
     * @param houseOrderId
     * @param status
     * @return
     */
    public Result changeStatus(Integer userId, Integer houseOrderId, String status){
        try{
            HouseOrder houseOrder = houseOrderService.findByBuyerIdAndOrderId(userId, houseOrderId);

            houseOrder.setStatus(status);
            houseOrderService.update(houseOrder);
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

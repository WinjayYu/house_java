package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.HouseOrderStatus;
import com.ryel.zaja.config.enums.HouseOrderType;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.CommunityDao;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.HouseOrderDao;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.push.Push;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.BizUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @Autowired
    private UserService userService;

    @Autowired
    private PushDeviceService pushService;

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
    public Result receiveOrder(Integer userId, Integer houseOrderId, String idcard, String floor, String username){
        try{
            HouseOrder houseOrder = houseOrderService.findByBuyerIdAndOrderId(userId, houseOrderId);

            houseOrder.setStatus(HouseOrderStatus.WAIT_PAYMENT.getCode());
            houseOrder.setIdcard(idcard);
            houseOrder.setFloor(floor);
            houseOrder.setUsername(username);
            houseOrderService.update(houseOrder);
            //更新用户信息
            User user = new User();
            user.setId(userId);
            user.setUsername(username);
            userService.update(user);
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


    /**
     * 用户发起订单
     */
    @RequestMapping(value = "userpublishorder")
    public Result publishorder(Integer agentId, Integer houseId, BigDecimal area, BigDecimal price,
                               String toMobile, String idcard, String floor, String username) {
        try {
            HouseOrder houseOrder = new HouseOrder();
            // 查经济人
            User agent = userService.findById(agentId);
            if (agent == null) {
                throw new BizException(Error_code.ERROR_CODE_0023, "查询用户信息为空");
            }
            // 查买房人
            User user = userService.findByMobile(toMobile);
            if (user == null) {
                throw new BizException(Error_code.ERROR_CODE_0023, "根据toMobile查询user为空");
            }
            House house = houseService.findById(houseId);
            if (house == null) {
                throw new BizException(Error_code.ERROR_CODE_0025, "查询到house is null");
            }
            List<HouseOrder> list = houseOrderService.findPayedOrderByHouseId(houseId);
            if (!CollectionUtils.isEmpty(list)) {
                throw new BizException(Error_code.ERROR_CODE_0026, "房源已经存在已支付的订单");
            }
            houseOrder.setHouse(house);
            houseOrder.setCommunity(house.getCommunity());
            if (null != house.getSellHouse()) {
                houseOrder.setSeller(house.getSellHouse().getUser());
            } else {
                houseOrder.setSeller(userService.findById(agentId));
            }

            // 生产订单号
            String code = BizUtil.getOrderCode();
            houseOrder.setAgent(agent);
            houseOrder.setBuyer(user);
            houseOrder.setBuyerMobile(toMobile);
            houseOrder.setStatus(HouseOrderStatus.NO_ORDER.getCode());
            houseOrder.setType(HouseOrderType.FROM_HOUSE.getCode());
            houseOrder.setAddTime(new Date());
            houseOrder.setCode(code);
            houseOrder.setArea(area);
            houseOrder.setPrice(price);
            houseOrder.setCommission(price.multiply(BigDecimal.valueOf(250)));
            houseOrder.setAuthor(user);
            houseOrder.setUsername(username);
            houseOrder.setFloor(floor);
            houseOrder.setIdcard(idcard);

            houseOrderService.save(houseOrder);

            //发送推送信息
            PushDevice pushDevice = pushService.findByUser(agent);
            if (null != pushDevice) {
                if (pushDevice.getDevice().equals("Android")) {
                    Push push = new Push();
                    push.sendAndroidOrder(user.getId());
                } else {
                    Push push = new Push();
                    push.sendIOSOrder(user.getId());
                }
            }
            return Result.success().msg("").data(new HashMap<>());
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(e.getCode()).data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }



}

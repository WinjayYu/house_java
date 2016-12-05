package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.CommunityDao;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.SendOrderDao;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.SendOrder;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.entity.vo.SendOrderVo;
import com.ryel.zaja.service.HouseService;
import com.ryel.zaja.service.SendOrderService;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.DataTableFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by billyu on 2016/12/2.
 */
@RestController
@RequestMapping("api/sendorder")
public class SendOrderApi {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SendOrderService sendOrderService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseDao houseDao;

    @Autowired
    private CommunityDao communityDao;

    private SendOrderDao sendOrderDao;

    /**
     * 由已有的房源发送订单
     * @param sendOrderVo
     * @return
     */
    @RequestMapping(value = "sendorder", method = RequestMethod.POST)
    public Result sendOrder(@RequestBody SendOrderVo sendOrderVo) {
        User user = userDao.findByMobile(sendOrderVo.getMobile());
        if(user == null){
            return Result.error().msg(Error_code.ERROR_CODE_0012);//手机号未注册用户
        }
        House house = houseDao.findOne(sendOrderVo.getHouseId());

        final String type = "10";//由房源发送的订单
        final String status = "10";//已派送

        SendOrder sendOrder = new SendOrder();
        sendOrder.setType(type);
        sendOrder.setStatus(status);
        sendOrder.setHouse(house);

        sendOrder.setArea(new BigDecimal(0.00));
        sendOrder.setCommission(new BigDecimal(0.00));
        sendOrder.setCommunity(new Community(0));
        sendOrder.setSellPrice(new BigDecimal(0.00));
        try {
            sendOrder.setAgent(userDao.findOne(sendOrderVo.getAgentId()));
            sendOrder.setUser(user);
            sendOrderService.create(sendOrder);
        } catch (Exception e) {
            return Result.error().msg(Error_code.ERROR_CODE_0019);//操作失败
        }
        return Result.success().msg("");
    }

    /**
     * 经纪人自定义房源
     * @param sendOrderVo
     * @return
     */
    @RequestMapping(value = "customsendorder", method = RequestMethod.POST)
    public Result customSendOrder(@RequestBody SendOrderVo sendOrderVo) {
        User user = userDao.findByMobile(sendOrderVo.getMobile());
        if(user == null){
            return Result.error().msg(Error_code.ERROR_CODE_0012);//手机号未注册用户
        }
        final String type = "20";//由房源发送的订单
        final String status = "10";//已派送

        SendOrder sendOrder = new SendOrder();
        sendOrder.setType(type);
        sendOrder.setStatus(status);
        sendOrder.setHouse(new House(0));

        sendOrder.setArea(sendOrderVo.getArea());
        sendOrder.setCommission(sendOrderVo.getCommission());
        sendOrder.setCommunity(communityDao.findOne(sendOrderVo.getCommunityUid()));
        sendOrder.setSellPrice(sendOrderVo.getSellPrice());

        try {
            sendOrder.setAgent(userDao.findOne(sendOrderVo.getAgentId()));
            sendOrder.setUser(user);
            sendOrderService.create(sendOrder);
        } catch (Exception e) {
            return Result.error().msg(Error_code.ERROR_CODE_0019);//操作失败
        }
        return Result.success().msg("").data("");
    }

    /**
     * 根据经纪人id查询发送订单列表
     * @param sendOrderVo
     * @return
     */
    @RequestMapping(value = "listsendorder" ,method = RequestMethod.POST)
    public Result listSendOrder(@RequestBody SendOrderVo sendOrderVo){

        Page<SendOrder> page = sendOrderService.findByPage(sendOrderVo.getAgentId(),
                                                            sendOrderVo.getPageNum(),
                                                            sendOrderVo.getPageSize());
        Map<String,Object> dataMap = APIFactory.fitting(page);
        return Result.success().msg("").data(dataMap);
    }


    public Integer getPageNum(Integer start,Integer length){
        if(start==null){
            start = 0;
        }
        if(length == null){
            length = 10;
        }

        int pageNum = (start/length)+1;
        return pageNum;
    }
}

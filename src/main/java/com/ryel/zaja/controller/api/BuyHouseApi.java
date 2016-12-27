package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.HouseOrderStatus;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.entity.HouseOrder;
import com.ryel.zaja.service.BuyHouseService;
import com.ryel.zaja.service.CommunityService;
import com.ryel.zaja.service.HouseOrderService;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.management.AttributeList;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by billyu on 2016/11/30.
 * 买房
 */
@RestController
@RequestMapping(value = "/api/buyhouse/", produces = "application/json; charset=UTF-8")
public class BuyHouseApi {
    protected final static Logger logger = LoggerFactory.getLogger(BuyHouseApi.class);


    @Autowired
    private BuyHouseService buyHouseService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserService userService;

    @Autowired
    private HouseOrderService houseOrderService;

    @RequestMapping(value = "listbuyhouses", method = RequestMethod.POST)
    public Result listBuyHouses(Integer userId, Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }

            Page<BuyHouse> page = buyHouseService.findByPage(userId, pageNum, pageSize);
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
//            return JsonUtil.obj2ApiJson(result);
        }

    }

    /**
     * 已废弃
     * @param community
     * @param userId
     * @param price
     * @param layout
     * @param renovation
     * @param area
     * @return
     */
   /* @RequestMapping(value = "onebuyhouse", method = RequestMethod.POST)
    public Result oneBuyHouse(Integer id) {
        BuyHouse origBuyHouse = buyHouseService.findById(id);
        if (null == origBuyHouse) {
            return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
        }
        return Result.success().msg("").data(origBuyHouse);
    }*/

    /**
     * 发布买房需求
     *
     * @param community
     * @param userId
     * @param price
     * @param layout
     * @param renovation
     * @param area
     * @return
     */
    @RequestMapping(value = "buyhouse", method = RequestMethod.POST)
    public Result buyHouse(Community community, Integer userId, String price,
                           String layout, String renovation, String area) {
        try {
            Community origComm = communityService.findByUid(community.getUid());
            if (null == origComm) {
                communityService.create(community);
            }

            BuyHouse buyHouse = new BuyHouse();
            buyHouse.setCommunity(community);
            buyHouse.setUser(userService.findById(userId));

            if(null == price){
                buyHouse.setMinPrice(null);
                buyHouse.setMaxPrice(null);
            }else {
                String[] pri = price.split("\\|");
                if (1 == pri.length) {
                    buyHouse.setMinPrice(new BigDecimal(pri[0]));
                } else if (!StringUtils.isNotBlank(pri[0])) {
                    buyHouse.setMinPrice(new BigDecimal(pri[1]));
                } else {
                    BigDecimal bg1 = new BigDecimal(pri[0]);
                    BigDecimal bg2 = new BigDecimal(pri[1]);
                    buyHouse.setMinPrice(bg1);
                    buyHouse.setMaxPrice(bg2);
                }
            }

            buyHouse.setLayout(layout);
            buyHouse.setRenovation(renovation);

            if(null == area){
                buyHouse.setMinArea(null);
                buyHouse.setMaxArea(null);
            }else {
                String[] are = area.split("\\|");
                if (1 == are.length) {
                    buyHouse.setMinArea(new BigDecimal(are[0]));
                } else if (!StringUtils.isNotBlank(are[0])) {
                    buyHouse.setMaxArea(new BigDecimal(are[1]));
                } else {
                    BigDecimal bg1 = new BigDecimal(are[0]);
                    BigDecimal bg2 = new BigDecimal(are[1]);
                    buyHouse.setMinArea(bg1);
                    buyHouse.setMaxArea(bg2);
                }
            }

            buyHouseService.create(buyHouse);
            return Result.success().msg("").data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
    }


}

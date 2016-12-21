package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.SellHouseStatus;
import com.ryel.zaja.dao.CommunityDao;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.service.CommunityService;
import com.ryel.zaja.service.SellHouseService;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.utils.APIFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by billyu on 2016/11/30.
 * <p>
 * 卖房
 */
@RestController()
@RequestMapping("/api/sellhouse/")
public class SellHouseApi {
    protected final static Logger logger = LoggerFactory.getLogger(SellHouseApi.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private SellHouseService sellHouseService;

    @RequestMapping(value = "listsellhouses", method = RequestMethod.POST)
    public Result allSellHouses(Integer userId, Integer pageNum, Integer pageSize) {
        try {
            Map<String, Object> map = new HashMap<>();
            Page<SellHouse> page = sellHouseService.pageByUserId(userId, pageNum, pageSize);

            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
    }


    @RequestMapping(value = "sellhouse", method = RequestMethod.POST)
    public Result sellHouse(Community community, Integer id, BigDecimal price,
                            String layout, String renovation, double area) {
        Community origComm = communityService.findByUid(community.getUid());
        if (null == origComm) {
            try {
                communityService.create(community);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
            }
        }
        try {
            Community OrigCom = communityService.findByUid(community.getUid());

            SellHouse sellHouse = new SellHouse();
            sellHouse.setCommunity(community);
            sellHouse.setUser(userService.findById(id));
            sellHouse.setPrice(price);
            sellHouse.setLayout(layout);
            sellHouse.setRenovation(renovation);
            sellHouse.setArea(area);
            sellHouse.setStatus(SellHouseStatus.PUBLISHED.getCode());
            sellHouseService.create(sellHouse);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
        return Result.success().msg("").data(new HashMap<>());
    }

}

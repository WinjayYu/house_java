package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.APIFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by billyu on 2017/1/4.
 */
@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=UTF-8")
public class HouseApi {
    protected final static Logger logger = LoggerFactory.getLogger(HouseApi.class);

    @Autowired
    private HomeCoverUrlService homeCoverUrlService;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private BuyHouseService buyHouseService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserService userService;

    /**
     * home页面
     *
     * @param userId
     * @param lon1
     * @param lat1
     * @param city
     * @return
     */
    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public Result home(@RequestParam(value = "userId", required = false) Integer userId,
                       @RequestParam(value = "longitude", required = false) Double lon1,
                       @RequestParam(value = "latitude", required = false) Double lat1,
                       @RequestParam(value = "city", required = false) String city) {
        try {

            Map<String, Object> home = new HashMap<String, Object>();
            List recomHouses;
            List hotHouses;

            HomeCoverUrl homeCoverUrl = homeCoverUrlService.find(1);
            home.put("homeCoverUrl", homeCoverUrl);

            String type = UserType.USER.getCode();//用户类型,默认为用户

            //如果有userId，则调用recommend方法，zaja推荐
            if (null != userId) {
                User user = userService.findById(userId);
                type = user.getType();
                recomHouses = APIFactory.listFilterHouse(houseService.recommend(userId, type));

                if (!CollectionUtils.isEmpty(recomHouses)) {
                    home.put("recomHouses", recomHouses);
                } else {
                    List<House> recoHouses = recommendService.findByStatus("10");
                    home.put("recomHouses", recoHouses);
                }
            } else {
                List<House> recoHouses = recommendService.findByStatus("10");
                home.put("recomHouses", recoHouses);
            }

            //如果有经纬度和城市名字，则调用hotHouse方法，热门推荐
            if (null != lon1 && null != lat1 && null != city) {
                hotHouses = APIFactory.listFilterHouse(houseService.hotHouse(lon1, lat1, city, type));
            } else {
                lon1 = 114.323705;
                lat1 = 30.468666;
                city = "武汉";
                hotHouses = APIFactory.listFilterHouse(houseService.hotHouse(lon1, lat1, city, type));
            }
            if (!hotHouses.isEmpty() && null != hotHouses) {
                home.put("hotHouses", hotHouses);
            } else {
                List recoHouses = APIFactory.listFilterHouse((recommendService.findByStatus("10")));
                home.put("hotHouses", recoHouses);
            }

            return Result.success().msg("").data(home);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
    }

    /**
     * 用户买房需求列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/house/listbuyhouses", method = RequestMethod.POST)
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
    @RequestMapping(value = "/house/buyhouse", method = RequestMethod.POST)
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

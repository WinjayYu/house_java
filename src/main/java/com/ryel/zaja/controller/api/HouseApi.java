
package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.SellHouseStatus;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.GetDistanceUtil;
import com.ryel.zaja.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;


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

    @Autowired
    private SellHouseService sellHouseService;


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

    @RequestMapping(value = "/buyhouse/listbuyhouses", method = RequestMethod.POST)
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

    @RequestMapping(value = "/buyhouse/buyhouse", method = RequestMethod.POST)
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
            buyHouse.setNum(0);

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


    /**
     * 卖房列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/sellhouse/listsellhouses", method = RequestMethod.POST)
    public Result allSellHouses(Integer userId, Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }

            Page<SellHouse> page = sellHouseService.pageByUserId(userId, pageNum, pageSize);


            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
    }


    /**
     * 发布卖房需求
     * @param community
     * @param userId
     * @param price
     * @param layout
     * @param renovation
     * @param area
     * @return
     */
    @RequestMapping(value = "/sellhouse/sellhouse", method = RequestMethod.POST)
    public Result sellHouse(Community community, Integer userId, BigDecimal price,
                            String layout, String renovation, Double area) {
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
            sellHouse.setUser(userService.findById(userId));
            sellHouse.setPrice(price);
            sellHouse.setLayout(layout);
            sellHouse.setRenovation(renovation);
            sellHouse.setArea(area);
            sellHouse.setStatus(SellHouseStatus.PUBLISHED.getCode());
            sellHouse.setHouseNum(0);
            sellHouse.setNum(0);
            sellHouseService.create(sellHouse);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
        return Result.success().msg("").data(new HashMap<>());
    }

    /**
     * 通过用户发布的卖房需求查房源
     * @param sellHouseId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/sellhouse/mysellhouse", method = RequestMethod.POST)
    public Result mySellHouse(Integer sellHouseId, Integer pageNum, Integer pageSize){
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<House> page = houseService.pageBySellHouse(sellHouseId,new PageRequest(pageNum - 1, pageSize, Sort.Direction.ASC, "id") );

            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
    }

    /**
     * 相似房源
     * @param houseId
     * @return
     */
    @RequestMapping(value = "/similar", method = RequestMethod.POST)
    public String similarHouse(Integer houseId) {
        House house = houseService.findById(houseId);

        Map<String, Object> similarHouse = new HashMap<String, Object>();
        List<House> resultHouses = new ArrayList<House>();

        List<House> houses = houseService.findSimilar(house.getPrice(),
                house.getCommunity().getUid(),
                house.getArea(),
                house.getRenovation());

        houses.remove(houseService.findById(houseId));
        if (!CollectionUtils.isEmpty(houses)) {
            if(houses.size() > 5){
                for(int i=houses.size()-1; i>4; i--){
                    houses.remove(i);
                }
            }
            similarHouse.put("list", houses);
            return JsonUtil.obj2ApiJson(Result.success().msg("").data(similarHouse));

        } else {
            return JsonUtil.obj2ApiJson(Result.success().msg("").data(similarHouse.put("list", recommendService.findByStatus("10"))));

        }
    }

    /**
     * 筛选房源
     * @param pageNum
     * @param pageSize
     * @param price
     * @param area
     * @param layout
     * @param renovation
     * @param floor
     * @return
     */
    @RequestMapping(value = {"/discovery/filter", "/discovery/filterbycity"}, method = RequestMethod.POST)
    public Result filters(Integer pageNum,
                          Integer pageSize,
                          String price,
                          String area,
                          String layout,
                          String renovation,
                          String floor,
                          String city,
                          String district,
                          @RequestParam(value = "longitude", required = false) Double lon1,
                          @RequestParam(value = "latitude", required = false) Double lat1) {

        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 1;
        }
        //如果经纬度不为空，则为筛选附近接口，否则筛选城市接口
        if(null != lon1 && null != lat1){

            List<String> uids = nearbyCommunity(lon1, lat1, city);
            if(null != uids && !uids.isEmpty()) {
                Iterator<String> it = uids.iterator();
                while (it.hasNext()) {
                    String uid = it.next();
                    Community community = communityService.findByUid(uid);
                    if (!community.getDistrict().equals(district)) {
                        it.remove();
                    }
                }

                Page<House> page = houseService.pageByNearbyHouse(pageNum, pageSize, uids, price, area, layout, renovation, floor);
                Map<String, Object> dataMap = APIFactory.fitting(page);
                return Result.success().msg("").data(dataMap);
            }

            Page<House> page = houseService.findByAddTime(UserType.USER.getCode(), new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC));
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);

        }else {
            Page<House> houses = houseService.filter(pageNum, pageSize, price, area, layout, renovation, floor, city, UserType.USER.getCode());
            Map<String, Object> dataMap = APIFactory.fitting(houses);
            return Result.success().msg("").data(dataMap);
        }
    }



    /**
     * 搜索房源
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/discovery/search", method = RequestMethod.POST)
    public Result search(String uid, Integer pageNum, Integer pageSize) {
        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 1;
        }
        Page<House> houses = houseService.findByUid(uid, UserType.USER.getCode(), new PageRequest(pageNum - 1, pageSize, Sort.Direction.ASC, "id"));


        Map<String, Object> dataMap = APIFactory.fitting(houses);
        return Result.success().msg("").data(dataMap);
    }

    /**
     * 附近房子
     * @param lon1
     * @param lat1
     * @param city
     * @param type 用户类型
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/discovery/dis", method = RequestMethod.POST)
    public Result discovery(@RequestParam(value = "longitude", required = false) Double lon1,
                            @RequestParam(value = "latitude", required = false) Double lat1,
                            String city,
                            String type,
                            Integer pageNum,
                            Integer pageSize) {
        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 1;
        }
        type = type == null ? UserType.USER.getCode() : type;

        if (null != lon1 && null != lat1 && null != city) {
            List<String> uids = nearbyCommunity(lon1, lat1, city);
            if (!uids.isEmpty() && null != uids) {
                Page<House> page = houseService.findByUids(uids, type, new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
                Map<String, Object> dataMap = APIFactory.fitting(page);
                return Result.success().msg("").data(dataMap);
            }
        }
        Page<House> page = houseService.findByAddTime(type, new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC));
        Map<String, Object> dataMap = APIFactory.fitting(page);
        return Result.success().msg("").data(dataMap);


    }

    public List<String> nearbyCommunity(double lon1, double lat1, String city) {
        List<Community> communities5 = new ArrayList<Community>();
        List<Community> communities10 = new ArrayList<Community>();
        List<Community> communities20 = new ArrayList<Community>();

        List<Community> communityBycity = communityService.findByCity(city);
        List<String> uids = new ArrayList<>();
        for (Community community : communityBycity) {
            double lon2 = community.getLongitude().doubleValue();
            double lat2 = community.getLatitude().doubleValue();
            //计算两个点之间的距离
            double distance = GetDistanceUtil.GetDistance(lon1, lat1, lon2, lat2);
            if (distance <= 5000) {
                communities5.add(community);
            }else if (distance > 5000 && distance <= 10000)
            {
                communities10.add(community);
            }else
            {
                communities20.add(community);
            }
            //if(6 == houses.size()) break;
        }
        for(Community community : communities5){
            uids.add(community.getUid());
        }
        return uids;
    }
}


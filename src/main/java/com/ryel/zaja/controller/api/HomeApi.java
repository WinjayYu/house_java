package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.GetDistanceUtil;
import com.ryel.zaja.utils.MapSortUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by billyu on 2016/12/3.
 */
@RestController
@RequestMapping(value = "/api/")
public class HomeApi {

    protected final static Logger logger = LoggerFactory.getLogger(SellHouseApi.class);

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

//    private final static String MANAGER = "10";
//    private final static String USER = "20";
//    private final static String AGENT = "30";


    @RequestMapping(value = "home", method = RequestMethod.POST)
    public Result home(@RequestParam(value = "userId", required = false) Integer userId,
                       @RequestParam(value = "longitude", required = false) Double lon1,
                       @RequestParam(value = "latitude", required = false) Double lat1,
                       @RequestParam(value = "city", required = false) String city) {

        Map<String, Object> home = new HashMap<String, Object>();
        List<House> recomHouses = new ArrayList<House>();
        List<House> hotHouses = new ArrayList<House>();

        HomeCoverUrl homeCoverUrl = homeCoverUrlService.find(1);
        home.put("homeCoverUrl", homeCoverUrl);

        String type = "10";//用户类型,默认为用户

        //如果有userId，则调用recommend方法，zaja推荐
        if (null != userId) {
            User user = userService.findById(userId);
            type = user.getType();
            recomHouses = recommend(userId, type);
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
            hotHouses = hotHouse(lon1, lat1, city, type);
        } else {
            lon1 = 114.323705; lat1 = 30.468666; city = "武汉";
            hotHouses = hotHouse(lon1, lat1, city, type);
        }
        if (!hotHouses.isEmpty() && null != hotHouses) {
            home.put("hotHouses", hotHouses);
        } else {
            List<House> recoHouses = recommendService.findByStatus("10");
            home.put("hotHouses", recoHouses);
        }

        return Result.success().msg("").data(home);
    }


    public List<House> hotHouse(double lon1, double lat1, String city, String type) {

        List<Community> communities = new ArrayList<Community>();
        List<Community> communityBycity = communityService.findByCity(city);
        List<String> uids = new ArrayList<>();
        List<House> houses = new ArrayList<>();

        for (Community community : communityBycity) {
            double lon2 = community.getLongitude().doubleValue();
            double lat2 = community.getLatitude().doubleValue();
            //计算两个点之间的距离
            double distance = GetDistanceUtil.GetDistance(lon1, lat1, lon2, lat2);
            if (distance <= 100000) {//100公里之内的
                communities.add(community);
            }
        }
        for (Community community : communities) {
            uids.add(community.getUid());
        }
        //return uids;
        for (int i = 0; i < uids.size(); i++) {
            houses.addAll(houseService.findByCommunityUid(uids.get(i), type));
            if (6 == houses.size()) break;
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < houses.size(); i++) {

            int clickNum = houses.get(i).getViewNum();
            int collect = collectService.countByHouseId(houses.get(i).getId());
            //热度值，clickNum*1  + collect*2
            int hotNum = clickNum + collect * 2;
            map.put(i, hotNum);
        }

        Map sortMap = MapSortUtils.sortByValue(map);
        List<Integer> list = new ArrayList<Integer>(sortMap.keySet());

        List<House> sortHouses = new ArrayList<>();
        for (int b = list.size() - 1; b >= 0; b--) {
            sortHouses.add(houses.get(list.get(b)));
        }
        return sortHouses;

    }


    //zaja推荐房源
    public List<House> recommend(int userId, String type) {
        List<House> result = new ArrayList<>();
        List<BuyHouse> buyHouses = new ArrayList<>();
        if (UserType.USER.getCode().equals(type)) {
            buyHouses = buyHouseService.findByUserId(userId);
        }
        if (buyHouses.isEmpty()) {
            return recommendService.findByStatus("10");
        } else {
            //随机获取一条数据
            BuyHouse bh = buyHouses.get(new Random().nextInt(buyHouses.size()));

            List<House> houses = new ArrayList<House>();

            //加上小区一样的房源
            if (-1 != bh.getCommunity().indexOf("|")) {
                String[] str = bh.getCommunity().split("\\|");
//                int j = new Random().nextInt(str.length);
                for (int i = 0; i < str.length; i++) {
                    houses.addAll(houseService.findByCommunityUid(str[i], type));
                }
            } else {
                houses = houseService.findByCommunityUid(bh.getCommunity(), type);
            }
            //加上户型一样的房源
            houses.addAll(houseService.findByLayout(bh.getLayout(), type));

            //如果小于5条数据则返回结果
            if (houses.size() <= 5) {
                List<House> recoHouses = recommendService.findByStatus("10");
                for (int i = 0; houses.size() < 5; ) {
                    houses.add(recoHouses.get(i));
                    i++;
                }
                return houses;
            }
        }

        return null;
    }
}



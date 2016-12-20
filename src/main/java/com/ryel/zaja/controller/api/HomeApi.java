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
                       @RequestParam(value = "cityname", required = false) String cityname) {

        Map<String, Object> home = new HashMap<String, Object>();
        List<House> recomHouses = new ArrayList<House>();
        List<House> hotHouses = new ArrayList<House>();

        HomeCoverUrl homeCoverUrl = homeCoverUrlService.find(1);
        home.put("homeCoverUrl", homeCoverUrl);

        User user = userService.findById(userId);
        String type = user.getType();//用户类型


        //如果有userId，则调用recommend方法，zaja推荐
        if (null != userId) {
            recomHouses = recommend(userId,type);
            if (!recomHouses.isEmpty() && null != recomHouses) {
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
        if (null != lon1 && null != lat1 && null != cityname) {

            hotHouses = hotHouse(lon1, lat1, cityname, type);
            if (!hotHouses.isEmpty() && null != hotHouses) {
                home.put("hotHouses", hotHouses);
            } else {
                List<House> recoHouses = recommendService.findByStatus("10");
                home.put("hotHouses", recoHouses);
            }
        } else {
            List<House> recoHouses = recommendService.findByStatus("10");
            home.put("hotHouses", recoHouses);
        }

        return Result.success().msg("").data(home);
    }


    public List<House> hotHouse(double lon1, double lat1, String cityname ,String type) {

        List<Community> communities = new ArrayList<Community>();
        List<Community> communityBycity = communityService.findByCityname(cityname);
        List<String> uids = new ArrayList<>();
        List<House> houses = new ArrayList<>();

        for (Community community : communityBycity) {
            double lon2 = community.getLongitude().doubleValue();
            double lat2 = community.getLatitude().doubleValue();
            //计算两个点之间的距离
            double distance = GetDistanceUtil.GetDistance(lon1, lat1, lon2, lat2);
            if (distance <= 10000) {//10公里之内的
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
        if (UserType.USER.getType().equals(type)) {
            buyHouses = buyHouseService.findByUserId(userId);
        }
        if (buyHouses.isEmpty()) {
            return recommendService.findByStatus("10");
        } else {
            //随机获取一条数据
            BuyHouse bh = buyHouses.get(new Random().nextInt(buyHouses.size()));

            List<House> houses = new ArrayList<House>();

            if (-1 != bh.getCommunity().indexOf("|")) {
                String[] str = bh.getCommunity().split("\\|");
                int j = new Random().nextInt(str.length);
                houses = houseService.findByCommunityUid(str[j], type);
            } else {
                houses = houseService.findByCommunityUid(bh.getCommunity(), type);
            }
          /*  if (null == houses) {
                houses = houseDao.findByCommunityAddress();
            }*/

            if (CollectionUtils.isEmpty(houses)) {
                houses = houseService.findByLayout(bh.getLayout(), type);
            if (houses.isEmpty()) {
                houses = houseService.findByLayout(bh.getLayout(),type);
                return houses;
            } else {
                //如果小于5条数据则返回结果
                if (houses.size() <= 5) {
                    List<House> recoHouses = recommendService.findByStatus("10");
                    for (int i = 0; houses.size() < 5; ) {
                        houses.add(recoHouses.get(i));
                        i++;
                    }
                    return houses;
                } else {
                    for (House house : houses) {
                        if (bh.getLayout().equals(house.getLayout())) {
                            if (bh.getLayout().equals(house.getLayout())) {
                                result.add(house);
                            }
                        }

                        if (result.size() <= 5) {
                            List<House> recoHouses = recommendService.findByStatus("10");
                            for (int i = 0; houses.size() <= 5; ) {
                                houses.add(recoHouses.get(i));
                                i++;
                            }
                            return houses;
                        }
                    }
                }
            }
            return null;
        }
    }
}

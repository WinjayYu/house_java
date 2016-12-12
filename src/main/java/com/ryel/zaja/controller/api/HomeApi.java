package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.*;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.HomeCoverUrl;
import com.ryel.zaja.utils.GetDistanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    SellHouseDao sellHouseDao;

    @Autowired
    BuyHouseDao buyHouseDao;

    @Autowired
    RecommendDao recommendDao;

    @Autowired
    HouseDao houseDao;

    @Autowired
    CommunityDao communityDao;

    @Autowired
    HomeCoverUrlDao homeCoverUrlDao;

    @RequestMapping(value = "home", method = RequestMethod.POST)
    public Result home(@RequestParam(value = "userId", required = false) Integer userId,
                       @RequestParam(value = "longitude", required = false) Double lon1,
                       @RequestParam(value = "latitude", required = false) Double lat1,
                       @RequestParam(value = "cityname", required = false) String cityname) {

        Map<String, Object> home = new HashMap<String, Object>();
        List<House> recomHouses = new ArrayList<House>();
        List<House> hotHouses = new ArrayList<House>();

        HomeCoverUrl homeCoverUrl = homeCoverUrlDao.findOne(1);
        home.put("homeCoverUrl", homeCoverUrl);

        //如果有userId，则调用recommend方法，zaja推荐
        if (null != userId) {
            recomHouses = recommend(userId);
            if (!recomHouses.isEmpty() && null != recomHouses) {
                home.put("recommend", recomHouses);
            }
        }

        //如果有经纬度和城市名字，则调用hotHouse方法，热门推荐
        if (null != lon1 && null != lat1 && null != cityname) {
            hotHouses = hotHouse(lon1, lat1, cityname);
            if (!hotHouses.isEmpty() && null != hotHouses) {
                home.put("hotHouses", hotHouses);
            }
        }

        Map homePage = new HashMap();
        homePage.put("home",home);
        return Result.success().msg("").data(homePage);
    }

    public List<House> hotHouse(double lon1, double lat1, String cityname) {
        List<House> houses = new ArrayList<House>();
        List<House> housesByCity = houseDao.findByCityname(cityname);
        for (House house : housesByCity) {
            double lon2 = house.getLongitude().doubleValue();
            double lat2 = house.getLatitude().doubleValue();
            //计算两个点之间的距离
            double distance =  GetDistanceUtil.GetDistance(lon1, lat1, lon2, lat2);
            if(distance <= 5000){
                houses.add(house);
            }
            //if(6 == houses.size()) break;
        }

        for(House house : houses){

        }
        return houses;
    }

   /* public static double getDistatce(BigDecimal lon1, BigDecimal lon2, BigDecimal lat1, BigDecimal lat2) {
        BigDecimal R = BigDecimal.valueOf(6378.137);
        BigDecimal distance = BigDecimal.ZERO;
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1 * Math.PI / 180)
                * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R;
        return distance;
    }*/


    //zaja推荐房源
    public List<House> recommend(int userId) {
        List<House> result = new ArrayList<>();
        List<BuyHouse> buyHouses = buyHouseDao.findByUserId(userId);

        if (0 == buyHouses.size()) {
            return recommendDao.findByStatus("10");
        } else {
            //随机获取一条数据
            BuyHouse bh = buyHouses.get(new Random().nextInt(buyHouses.size()));

            List<House> houses = new ArrayList<House>();

            if(-1 != bh.getCommunity().indexOf("|")) {
                String[] str = bh.getCommunity().split("\\|");
                int j = new Random().nextInt(str.length);
                houses = houseDao.findByCommunityUid(str[j]);
            }else{
                houses = houseDao.findByCommunityUid(bh.getCommunity());
            }
          /*  if (null == houses) {
                houses = houseDao.findByCommunityAddress();
            }*/

            if (null == houses) {
                houses = houseDao.findByHouseType(bh.getHouseType());
                return houses;
            } else {
                //如果小于5条数据则返回结果
                if (houses.size() <= 5) {
                    List<House> recoHouses = recommendDao.findByStatus("10");
                    for (int i = 0; houses.size() < 5; ) {
                        houses.add(recoHouses.get(i));
                        i++;
                    }
                    return houses;
                } else {
                    for (House house : houses) {
                        if (bh.getHouseType().equals(house.getHouseType())) {
                            result.add(house);
                        }
                    }

                    if (result.size() <= 5) {
                        List<House> recoHouses = recommendDao.findByStatus("10");
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

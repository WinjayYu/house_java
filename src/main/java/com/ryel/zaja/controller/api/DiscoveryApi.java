package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.CommunityDao;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.RecommendDao;
import com.ryel.zaja.dao.custom.FilterDao;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.vo.FilterVo;
import com.ryel.zaja.service.HouseService;
import com.ryel.zaja.service.RecommendService;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.GetDistanceUtil;
import com.ryel.zaja.utils.MapSortUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by billyu on 2016/12/6.
 */

@RestController
@RequestMapping(value = "/api/discovery/")
public class DiscoveryApi {
    protected final static Logger logger = LoggerFactory.getLogger(SellHouseApi.class);


    @Autowired
    private HouseService houseService;

    @Autowired
    private RecommendService recommendService;

/*    @RequestMapping(value = "filter", method = RequestMethod.POST)
    public Result filter(String sellPrice,
                         String area,
                         String houseType,
                         String fitmentLevel,
                         String floor) {

        List<House> houses = filterDao.findByFilter(sellPrice, area, houseType, fitmentLevel, floor);
        return Result.success().msg("").data(houses);
    }*/

    @RequestMapping(value = "filter", method = RequestMethod.POST)
    public Result filters(Integer pageNum,
                          Integer pageSize,
                          String sellPrice,
                          String area,
                          String houseType,
                          String fitmentLevel,
                          String floor) {

        Page<House> houses = houseService.filter(pageNum, pageSize, sellPrice, area, houseType, fitmentLevel, floor);
        Map<String,Object> dataMap = APIFactory.fitting(houses);
        return Result.success().msg("").data(dataMap);
    }


    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Result search(String uid, Integer pageNum, Integer pageSize) {
        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 1;
        }
        Page<House> houses = houseService.findByUid(uid, new PageRequest(pageNum - 1, pageSize, Sort.Direction.ASC, "id"));
        if (null == houses) {
            return Result.error().msg(Error_code.ERROR_CODE_0020);
        }
        Map<String, Object> dataMap = APIFactory.fitting(houses);
        return Result.success().msg("").data(dataMap);
    }

    @RequestMapping(value = "dis", method = RequestMethod.POST)
    public Result discovery(@RequestParam(value = "longitude",required = false) Double lon1,
                            @RequestParam(value = "latitude", required = false)Double lat1,
                            String cityname,
                            Integer pageNum,
                            Integer pageSize){
        List<House> nearbyHouses = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        //如果有经纬度和城市名字，则调用hotHouse方法，热门推荐
        if (null != lon1 && null != lat1 && null != cityname) {
            nearbyHouses = hotHouse(lon1, lat1, cityname);
            if (!nearbyHouses.isEmpty() && null != nearbyHouses) {
                Page<House> page = new PageImpl<House>(nearbyHouses,
                        new PageRequest(pageNum-1,pageSize, Sort.Direction.DESC, "id"), nearbyHouses.size());
                Map<String, Object> dataMap = APIFactory.fitting(page);
//                result.put("nearbyHouses", dataMap);
                return Result.success().msg("").data(dataMap);
            }else {
                List<House> recoHouses = recommendService.findByStatus("10");
                result.put("nearbyHouses", recoHouses);
            }
        } else {
            List<House> recoHouses = recommendService.findByStatus("10");
            result.put("nearbyHouses", recoHouses);
        }
        return Result.success().msg("").data(result);
    }

    public List<House> hotHouse(double lon1, double lat1, String cityname) {
        List<House> houses = new ArrayList<House>();
        List<House> housesByCity = houseService.findByCity(cityname);
        for (House house : housesByCity) {
            double lon2 = house.getLongitude().doubleValue();
            double lat2 = house.getLatitude().doubleValue();
            //计算两个点之间的距离
            double distance = GetDistanceUtil.GetDistance(lon1, lat1, lon2, lat2);
            if (distance <= 5000) {
                houses.add(house);
            }
            //if(6 == houses.size()) break;
        }
        return houses;
    }
}

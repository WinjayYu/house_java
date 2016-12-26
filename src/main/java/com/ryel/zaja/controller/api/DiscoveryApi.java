package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.dao.CommunityDao;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.RecommendDao;
import com.ryel.zaja.dao.custom.FilterDao;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.vo.FilterVo;
import com.ryel.zaja.service.CommunityService;
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

    @Autowired
    private CommunityService communityService;

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
                          String price,
                          String area,
                          String layout,
                          String renovation,
                          String floor) {

        Page<House> houses = houseService.filter(pageNum, pageSize, price, area, layout, renovation, floor, UserType.USER.getCode());
        Map<String, Object> dataMap = APIFactory.fitting(houses);
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
        Page<House> houses = houseService.findByUid(uid, UserType.USER.getCode(), new PageRequest(pageNum - 1, pageSize, Sort.Direction.ASC, "id"));

//        Page<House> houses = houseService.findByUid(uid, UserType.USER.getType(), new PageRequest(pageNum - 1, pageSize, Sort.Direction.ASC, "id"));
        if (null == houses) {
            return Result.error().msg(Error_code.ERROR_CODE_0020);
        }
        Map<String, Object> dataMap = APIFactory.fitting(houses);
        return Result.success().msg("").data(dataMap);
    }

    /**
     *
     * @param lon1
     * @param lat1
     * @param city
     * @param type 用户类型
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "dis", method = RequestMethod.POST)
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


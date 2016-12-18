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
                          String sellPrice,
                          String area,
                          String houseType,
                          String fitmentLevel,
                          String floor) {

        Page<House> houses = houseService.filter(pageNum, pageSize, sellPrice, area, houseType, fitmentLevel, floor);
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
        Page<House> houses = houseService.findByUid(uid, UserType.AGENT.getType(), new PageRequest(pageNum - 1, pageSize, Sort.Direction.ASC, "id"));
        if (null == houses) {
            return Result.error().msg(Error_code.ERROR_CODE_0020);
        }
        Map<String, Object> dataMap = APIFactory.fitting(houses);
        return Result.success().msg("").data(dataMap);
    }

    @RequestMapping(value = "dis", method = RequestMethod.POST)
    public Result discovery(@RequestParam(value = "longitude", required = false) Double lon1,
                            @RequestParam(value = "latitude", required = false) Double lat1,
                            String cityname,
                            Integer pageNum,
                            Integer pageSize) {
        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 1;
        }

        if (null != lon1 && null != lat1 && null != cityname) {
            List<String> uids = nearbyCommunity(lon1,lat1,cityname);
            if(!uids.isEmpty() && null!= uids){
                Page<House> page =  houseService.findByCommunities(uids, new PageRequest(pageNum-1,pageSize, Sort.Direction.DESC, "id"));
                Map<String, Object> dataMap = APIFactory.fitting(page);
                return Result.success().msg("").data(dataMap);
            }else{
                Page<House> page = houseService.findByAddTime(new PageRequest(pageNum-1, pageSize, Sort.Direction.DESC));
                Map<String, Object> dataMap = APIFactory.fitting(page);
                return Result.success().msg("").data(dataMap);
            }
        }else{
            Page<House> page = houseService.findByAddTime(new PageRequest(pageNum-1, pageSize, Sort.Direction.DESC));
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        }

    }

    public List<String> nearbyCommunity(double lon1, double lat1, String cityname) {
        List<Community> communities = new ArrayList<Community>();
        List<Community> communityBycity = communityService.findByCityname(cityname);
        List<String> uids = new ArrayList<>();
        for (Community community : communityBycity) {
            double lon2 = community.getLongitude().doubleValue();
            double lat2 = community.getLatitude().doubleValue();
            //计算两个点之间的距离
            double distance = GetDistanceUtil.GetDistance(lon1, lat1, lon2, lat2);
            if (distance <= 5000) {
                communities.add(community);
            }
            //if(6 == houses.size()) break;
        }
        for(Community community : communities){
            uids.add(community.getUid());
        }
        return uids;
    }
}


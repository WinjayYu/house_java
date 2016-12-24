package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.RecommendDao;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.service.HouseService;
import com.ryel.zaja.service.RecommendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.data.redis.core.RedisCommand;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by billyu on 2016/12/11.
 */
@RestController
@RequestMapping("api/")
public class SimilarHouseApi {
    protected final static Logger logger = LoggerFactory.getLogger(SimilarHouseApi.class);


    @Autowired
    private RecommendService recommendService;

    @Autowired
    private HouseService houseService;

    @RequestMapping(value = "similar", method = RequestMethod.POST)
    public Result similarHouse(Integer houseId) {
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
            return Result.success().msg("").data(similarHouse);

        } else {
            return Result.success().msg("").data(similarHouse.put("list", recommendService.findByStatus("10")));
        }
    }
}

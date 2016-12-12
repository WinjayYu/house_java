package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.RecommendDao;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.House;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    HouseDao houseDao;

    @Autowired
    RecommendDao recommendDao;

    @RequestMapping(value = "similar", method = RequestMethod.POST)
    public Result similarHouse(Integer id){
        House house = houseDao.findOne(id);

        Map<String,Object> similarHouse = new HashMap<String,Object>();
        List<House> resultHouses = new ArrayList<House>();

        List<House> houses = houseDao.findByCommumityAndAreaAndFitmentLevel(house.getCommunity().getUid(),
                                                                            house.getArea(),
                                                                            house.getFitmentLevel());
        if(null == house){
            return Result.success().msg("").data(recommendDao.findByStatus("10"));
        }

        if(houses.size() <= 1){
            return Result.success().msg("").data(houses);
        }else{
            double sellPrice = house.getSellPrice().doubleValue();
            double interval = Math.abs(houses.get(0).getSellPrice().doubleValue() - sellPrice);
            int index = 0;
            for(int i=0; i<=houses.size(); i++){
                double subtract = Math.abs(houses.get(i).getSellPrice().doubleValue() - sellPrice);
                //将售价相差最小的值赋给interval
                if(interval >= subtract){
                    interval = subtract;
                    index = i;
                }
            }
          if(index <= 1){
              for(int i=0; i<5; i++){
                  resultHouses.add(houses.get(i));
              }
          }else if(index >= houses.size()-1){
              int size = houses.size();
              for(int i=size-6; i<=size-1; i++){
                  resultHouses.add(houses.get(i));
              }
          }else{
              for(int i=index-2; i<=index+2; i++){
                  resultHouses.add(houses.get(i));
              }
          }
        }
        return Result.success().msg("").data(similarHouse.put("similarHouse", resultHouses));
    }
}

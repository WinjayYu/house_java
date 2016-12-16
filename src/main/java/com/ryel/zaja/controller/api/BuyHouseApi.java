package com.ryel.zaja.controller.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.BuyHouseDao;
import com.ryel.zaja.dao.CommunityDao;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.entity.vo.BuyHouseVo;
import com.ryel.zaja.service.BuyHouseService;
import com.ryel.zaja.service.CommunityService;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.utils.APIFactory;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by billyu on 2016/11/30.
 * 买房
 */
@RestController
@RequestMapping("/api/buyhouse/")
public class BuyHouseApi {
    protected final static Logger logger = LoggerFactory.getLogger(BuyHouseApi.class);


    @Autowired
    private BuyHouseService buyHouseService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "listbuyhouses", method = RequestMethod.POST)
    public Result listBuyHouses(Integer userId, Integer pageNum, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        Page<BuyHouse> page = buyHouseService.findByPage(userId, pageNum, pageSize);

        Map<String, Object> dataMap = APIFactory.fitting(page);
        return Result.success().msg("").data(dataMap);

//        map.put("list", list);
//        return Result.success().data(map);
    }

    @RequestMapping(value = "onebuyhouse", method = RequestMethod.POST)
    public Result oneBuyHouse(Integer id) {
        BuyHouse origBuyHouse = buyHouseService.findById(id);
        if (null == origBuyHouse) {
            return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
        }
        return Result.success().msg("").data(origBuyHouse);
    }

    @RequestMapping(value = "buyhouse", method = RequestMethod.POST)
    public Result buyHouse(String community, Integer userId, String buyPrice,
                           String houseType, String fitmentLevel, String area) {
//        String cutComm = community.substring(1,community.length()-1);

        String spliceString = "{\"community\":" + community + "}";
        JSONObject obj = new JSONObject(spliceString);
        JSONArray arr = (JSONArray) obj.get("community");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject object3 = arr.getJSONObject(i);
            String uid = object3.getString("uid");
            String name = object3.getString("name");
            String adcode = object3.getString("adcode");
            BigDecimal longitude = object3.getBigDecimal("longitude");
            BigDecimal latitude = object3.getBigDecimal("latitude");
            String cityname = object3.getString("cityname");
            String address = object3.getString("address");

            Community origComm = communityService.findByUid(uid);
            if (null == origComm) {
                try {
                    Community community1 = new Community();
                    community1.setUid(uid);
                    community1.setName(name);
                    community1.setAdcode(adcode);
                    community1.setLongitude(longitude);
                    community1.setLatitude(latitude);
                    community1.setCityname(cityname);
                    community1.setAddress(address);
                    communityService.create(community1);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return Result.error().msg(Error_code.ERROR_CODE_0019).data(new HashMap<>());
                }
            }

            sb.append(uid + "|");
        }

        String commUids = sb.toString().substring(0, sb.toString().length() - 1);
        BuyHouse buyHouse = new BuyHouse();

        buyHouse.setCommunity(commUids);
        buyHouse.setUser(userService.findById(userId));

        String[] pri = buyPrice.split("\\|");
        if(1 == pri.length){
            buyHouse.setMinPrice(new BigDecimal(pri[0]));
        }else if(!StringUtils.isNotBlank(pri[0])){
            buyHouse.setMinPrice(new BigDecimal(pri[1]));
        }else{
            BigDecimal bg1 = new BigDecimal(pri[0]);
            BigDecimal bg2 = new BigDecimal(pri[1]);
            buyHouse.setMinPrice(bg1);
            buyHouse.setMaxPrice(bg2);
        }
//        buyHouse.setMinPrice(new BigDecimal(pri[0]));
//        buyHouse.setMaxPrice(new BigDecimal(pri[1]));

        buyHouse.setHouseType(houseType);
        buyHouse.setFitmentLevel(fitmentLevel);

        String[] are = area.split("\\|");
        if(1 == are.length){
            buyHouse.setMinArea(new BigDecimal(are[0]));
        }else if(!StringUtils.isNotBlank(are[0])){
            buyHouse.setMaxArea(new BigDecimal(are[1]));
        }else{
            BigDecimal bg1 = new BigDecimal(are[0]);
            BigDecimal bg2 = new BigDecimal(are[1]);
            buyHouse.setMinArea(bg1);
            buyHouse.setMaxArea(bg2);
        }

        try {
            buyHouseService.create(buyHouse);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return Result.error().msg(Error_code.ERROR_CODE_0019).data(new HashMap<>());
        }

        return Result.success().msg("").data(new HashMap<>());

       /* Iterator iterator = arr.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }*/

    }

}

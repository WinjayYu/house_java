package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.service.BuyHouseService;
import com.ryel.zaja.service.CommunityService;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.management.AttributeList;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by billyu on 2016/11/30.
 * 买房
 */
@RestController
@RequestMapping(value = "/api/buyhouse/", produces = "application/json; charset=UTF-8")
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
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }

//            Map<String, Object> comm = new HashMap<>();
            List<Community> CommunityList = new ArrayList<>();
            List<Object> list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            Page<BuyHouse> page = buyHouseService.findByPage(userId, pageNum, pageSize);

            if(0 == page.getContent().size()){
                return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
//                return JsonUtil.obj2Json(result);
            }
            for(int i=0; i<page.getContent().size(); i++){

                String [] uids = page.getContent().get(i).getCommunity().split("\\|");

                for(int j=0; j<uids.length; j++) {
                    CommunityList.add(communityService.findByUid(uids[j]));
                }

                String str = JsonUtil.obj2Json(page.getContent().get(i));
                JSONObject obj = new JSONObject(str);
                obj.append("CommunityList", CommunityList);
                System.out.println(obj);
                list.add(obj.toString());
            }


//            comm.put("community", list);

                Map<String, Object> dataMap = new HashMap<>();
            Map<String, Object> pageMap = new HashMap<String, Object>();

            pageMap.put("totalNum", page.getTotalElements());
            pageMap.put("totalPage", page.getTotalPages());
            pageMap.put("currentPage", page.getNumber() + 1);

            dataMap.put("list", list);
            dataMap.put("page", pageMap);
            return Result.success().msg("").data(dataMap);
            //return JsonUtil.obj2Json(result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
//            return JsonUtil.obj2ApiJson(result);
        }

    }

    /**
     * 已废弃
     * @param community
     * @param userId
     * @param price
     * @param layout
     * @param renovation
     * @param area
     * @return
     */
   /* @RequestMapping(value = "onebuyhouse", method = RequestMethod.POST)
    public Result oneBuyHouse(Integer id) {
        BuyHouse origBuyHouse = buyHouseService.findById(id);
        if (null == origBuyHouse) {
            return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
        }
        return Result.success().msg("").data(origBuyHouse);
    }*/

    /**
     * 发布买房需求
     * @param community
     * @param userId
     * @param price
     * @param layout
     * @param renovation
     * @param area
     * @return
     */
    @RequestMapping(value = "buyhouse", method = RequestMethod.POST)
    public Result buyHouse(String community, Integer userId, String price,
                           String layout, String renovation, String area) {
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
            String city = object3.getString("city");
            String address = object3.getString("address");
            String district = object3.getString("district");

            Community origComm = communityService.findByUid(uid);
            if (null == origComm) {
                try {
                    Community community1 = new Community();
                    community1.setUid(uid);
                    community1.setName(name);
                    community1.setAdcode(adcode);
                    community1.setLongitude(longitude);
                    community1.setLatitude(latitude);
                    community1.setCity(city);
                    community1.setAddress(address);
                    community1.setDistrict(district);
                    communityService.create(community1);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
                }
            }

            sb.append(uid + "|");
        }

        String commUids = sb.toString().substring(0, sb.toString().length() - 1);
        BuyHouse buyHouse = new BuyHouse();

        buyHouse.setCommunity(commUids);
        buyHouse.setUser(userService.findById(userId));

        String[] pri = price.split("\\|");
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

        buyHouse.setLayout(layout);
        buyHouse.setRenovation(renovation);

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
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }

        return Result.success().msg("").data(new HashMap<>());

       /* Iterator iterator = arr.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }*/

    }

}

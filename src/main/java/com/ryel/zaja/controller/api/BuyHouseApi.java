package com.ryel.zaja.controller.api;

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
import com.ryel.zaja.utils.APIFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by billyu on 2016/11/30.
 * 买房
 */
@RestController()
@RequestMapping("/api/buyhouse/")
public class BuyHouseApi {
    protected final static Logger logger = LoggerFactory.getLogger(BuyHouseApi.class);


    @Autowired
    private BuyHouseService buyHouseService;

    @Autowired
    private CommunityDao communityDao;

    private CommunityService communityService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BuyHouseDao buyHouseDao;

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
        BuyHouse origBuyHouse = buyHouseDao.findOne(id);
        if (null == origBuyHouse) {
            return Result.error().msg(Error_code.ERROR_CODE_0014).data("");
        }
        return Result.success().msg("").data(origBuyHouse);
    }

    @RequestMapping(value = "buyhouse", method = RequestMethod.POST)
    public Result buyHouse(Map<String,Community> map, Integer userId, String price,
                           String houseType, String fitmentLevel, String area){
//遍历小区map并写进数据库
        for(int i=0; i<map.size(); i++) {
            Community origComm = communityDao.findByUid(map.get(i).getUid());
            if (null == origComm) {
                try {
                    communityService.create(map.get(i));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return Result.error().msg(Error_code.ERROR_CODE_0019).data("");
                }
            }
        }
        try{
            //buyhouse表community存字符串，以"|"隔开
            StringBuffer sb = new StringBuffer();
            for(int i=0; i<map.size(); i++){
                sb.append(map.get(i).getUid() + "|");
            }
            sb.deleteCharAt(sb.length()-1);

            BuyHouse buyHouse = new BuyHouse();

            buyHouse.setCommunity(sb.toString());
            buyHouse.setUser(userDao.findOne(userId));

            String[] pri = price.split("\\|");
            buyHouse.setMinPrice(new BigDecimal(pri[0]));
            buyHouse.setMaxPrice(new BigDecimal(pri[1]));

            buyHouse.setHouseType(houseType);
            buyHouse.setFitmentLevel(fitmentLevel);

            String[] are = area.split("\\|");
            buyHouse.setMinArea(new BigDecimal(are[0]));
            buyHouse.setMaxArea(new BigDecimal(are[1]));

            buyHouseService.create(buyHouse);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return Result.error().msg(Error_code.ERROR_CODE_0019).data("");
        }
        return Result.success().msg("").data("");
    }

}

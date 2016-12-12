package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.CommunityDao;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.custom.FilterDao;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.vo.FilterVo;
import com.ryel.zaja.utils.APIFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private FilterDao filterDao;

    @Autowired
    private CommunityDao communityDao;

    @Autowired
    private HouseDao houseDao;

    @RequestMapping(value = "filter", method = RequestMethod.POST)
    public Result filter(String sellPrice,
                         String area,
                         String type,
                         String decoration,
                         String floor) {

        List<House> houses = filterDao.findByFilter(sellPrice, area, type, decoration, floor);
        return Result.success().msg("").data(houses);
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Result search(String uid, Integer pageNum, Integer pageSize) {
        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 1;
        }
        Page<House> houses = houseDao.findByCommunityUid(uid, new PageRequest(pageNum - 1, pageSize, Sort.Direction.ASC, "id"));
        if(null == houses){
            return Result.error().msg(Error_code.ERROR_CODE_0020);
        }
        Map<String,Object> dataMap = APIFactory.fitting(houses);
        return Result.success().msg("").data(dataMap);
    }
}

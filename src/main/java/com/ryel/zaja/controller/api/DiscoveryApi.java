package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.custom.FilterDao;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.vo.FilterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by billyu on 2016/12/6.
 */
@RestController
@RequestMapping(value = "/api/discovery/")
public class DiscoveryApi {
    protected final static Logger logger = LoggerFactory.getLogger(SellHouseApi.class);

    @Autowired
    FilterDao filterDao;

    @RequestMapping(value = "filter", method = RequestMethod.POST)
    public Result filter(FilterVo  filterVo){

        List<House> houses = filterDao.findByFilter(filterVo);
        return Result.success().msg("").data(houses);
    }

}

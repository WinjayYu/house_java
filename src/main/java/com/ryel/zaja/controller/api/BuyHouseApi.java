package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.entity.vo.BuyHouseVo;
import com.ryel.zaja.service.BuyHouseService;
import com.ryel.zaja.service.SellHouseService;
import com.ryel.zaja.utils.APIFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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


    @Autowired
    private BuyHouseService buyHouseService;

    @RequestMapping(value = "listbuyhouses", method = RequestMethod.POST)
    public Result listBuyHouses(BuyHouseVo buyHouseVo) {
        Map<String, Object> map = new HashMap<>();
        Page<BuyHouse> page = buyHouseService.findByPage(buyHouseVo.getUserId(),
                                                         buyHouseVo.getPageNum(),
                                                         buyHouseVo.getPageSize());

        Map<String, Object> dataMap = APIFactory.fitting(page);
        return Result.success().msg("").data(dataMap);

//        map.put("list", list);
//        return Result.success().data(map);
    }

    @RequestMapping(value = "onebuyhouse", method = RequestMethod.POST)
    public Result oneBuyHouse(BuyHouse buyHouse) {
        BuyHouse origBuyHouse = buyHouseService.findById(buyHouse.getId());
        if (null == origBuyHouse) {
            return Result.error().msg(Error_code.ERROR_CODE_0014);
        }
        return Result.success().data(origBuyHouse);
    }
}

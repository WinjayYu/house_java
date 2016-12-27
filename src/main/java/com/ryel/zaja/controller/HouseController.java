package com.ryel.zaja.controller;


import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.AgentRegisterStatus;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.entity.AgentMaterial;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.AgentMaterialService;
import com.ryel.zaja.service.HouseService;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.utils.DataTableFactory;
import com.ryel.zaja.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "/mgt/house")
public class HouseController extends BaseController{
    @Autowired
    private HouseService houseService;

    @RequestMapping("/index")
    public String index(HttpServletRequest request,
                        HttpServletResponse response){
        return "房源列表";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Map list(Integer draw, Integer start, Integer length) {
        int pageNum = getPageNum(start, length);
        Page<House> page = houseService.mgtPageHouse(pageNum, length);
        Map<String, Object> result = DataTableFactory.fitting(draw, page);
        return result;
    }

    @RequestMapping("/detail")
    public ModelAndView detail(Integer id) {
        ModelAndView modelAndView = new ModelAndView("经济人详情");
        House house = houseService.findById(id);
        modelAndView.addObject("house",house);
        return modelAndView;
    }

    @RequestMapping("/approve")
    @ResponseBody
    public Result approve(Integer id,String type) {
        House house = houseService.findById(id);
        if("0".equals(type)){
            house.setStatus(HouseStatus.PUTAWAY_YET.getCode());
        }else {
            house.setStatus(HouseStatus.REJECT.getCode());
        }
        houseService.update(house);
        return Result.success();
    }

}

package com.ryel.zaja.controller;


import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.AgentRegisterStatus;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.controller.api.BuyHouseApi;
import com.ryel.zaja.entity.AgentMaterial;
import com.ryel.zaja.entity.Feature;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.AgentMaterialService;
import com.ryel.zaja.service.BizUploadFile;
import com.ryel.zaja.service.HouseService;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.utils.DataTableFactory;
import com.ryel.zaja.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/mgt/house")
public class HouseController extends BaseController{
    protected final static Logger logger = LoggerFactory.getLogger(HouseController.class);
    @Value("${pro.upload.path}")
    private String path;
    @Value("${pro.upload.url}")
    private String url;
    @Autowired
    private BizUploadFile bizUploadFile;
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
        ModelAndView modelAndView = new ModelAndView("房源详情");
        House house = houseService.findById(id);
        if(StringUtils.isNotBlank(house.getFeature())){
            Feature feature = JsonUtil.json2Obj(house.getFeature(),Feature.class);
            modelAndView.addObject("feature",feature);
        }
        modelAndView.addObject("house",house);
        return modelAndView;
    }

    @RequestMapping("/approve")
    @ResponseBody
    public Result approve(Integer id,String type) {
        House house = houseService.findById(id);
        if("0".equals(type)){
            // 设置为上架状态
            house.setStatus(HouseStatus.PUTAWAY_YET.getCode());
            // 本地图片上传到七牛并更新imgs字段
            String imgs = house.getImgs();
            if (StringUtils.isNotBlank(imgs)) {
                List<String> imageList = JsonUtil.json2Obj(imgs, List.class);
                List<String> newList = new ArrayList<String>();
                if (!CollectionUtils.isEmpty(imageList)) {
                    for (String imageUrl : imageList) {
                        String newUrl = localUrlToQiniuUrl(imageUrl,house.getId().toString());
                        newList.add(newUrl);
                    }
                }
                String newImgs = JsonUtil.obj2Json(newList);
                house.setImgs(newImgs);
            }
        }else {
            house.setStatus(HouseStatus.REJECT.getCode());
        }
        houseService.update(house);
        return Result.success();
    }

    private String localUrlToQiniuUrl(String localUrl,String houseId){
        String newUrl = localUrl;
        if (StringUtils.isNotBlank(localUrl)) {
            try {
                String imagePath = localUrl.replace(url, path);
                File file = new File(imagePath);
                if (file.exists()) {
                    newUrl = bizUploadFile.uploadHouseImageToQiniu(file, houseId);
                }else {
                    logger.error("文件不存在：" + file.getPath());
                }
            } catch (Exception e) {
                logger.error("上传文件异常");
                e.printStackTrace();
            }
        }
        return newUrl;
    }

}

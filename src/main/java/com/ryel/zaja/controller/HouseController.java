package com.ryel.zaja.controller;


import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.AgentRegisterStatus;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.service.*;
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
public class HouseController extends BaseController {
    protected final static Logger logger = LoggerFactory.getLogger(HouseController.class);
    @Value("${pro.upload.path}")
    private String path;
    @Value("${pro.upload.url}")
    private String url;
    @Autowired
    private BizUploadFile bizUploadFile;
    @Autowired
    private HouseService houseService;
    @Autowired
    private SellHouseService sellHouseService;
    @Autowired
    private QiNiuService qiNiuService;

    private final String remoteUrlHead = "http://oi0y2qwer.bkt.clouddn.com/";

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("房源列表");
        modelAndView.addObject("statusList",HouseStatus.getAllStatusOptionHtml(true));
        return modelAndView;
    }

    @RequestMapping("/list")
    @ResponseBody
    public Map list(Integer draw, Integer start, Integer length,String title,String status) {
        int pageNum = getPageNum(start, length);
        Page<House> page = houseService.mgtPageHouse(pageNum, length,title,status);
        Map<String, Object> result = DataTableFactory.fitting(draw, page);
        return result;
    }

    @RequestMapping("/detail")
    public ModelAndView detail(Integer id) {
        ModelAndView modelAndView = new ModelAndView("房源详情");
        House house = houseService.findById(id);
        if (StringUtils.isNotBlank(house.getFeature())) {
            Feature feature = JsonUtil.json2Obj(house.getFeature(), Feature.class);
            modelAndView.addObject("feature", feature);
        }
        modelAndView.addObject("house", house);
        if(StringUtils.isNotBlank(house.getImgs())){
            List<String> imageList = JsonUtil.json2Obj(house.getImgs(),List.class);
            if(!CollectionUtils.isEmpty(imageList)){
                modelAndView.addObject("imageList",imageList);
            }
        }
        modelAndView.addObject("house",house);
        return modelAndView;
    }

    @RequestMapping("/approve")
    @ResponseBody
    public Result approve(Integer id, String type) {
        House house = houseService.findById(id);
        if ("0".equals(type)) {
            // 设置为上架状态
            house.setStatus(HouseStatus.PUTAWAY_YET.getCode());
            if (null != house.getSellHouse()) {
                SellHouse sellHouse = house.getSellHouse();
                sellHouse.setHouseNum(sellHouse.getHouseNum() + 1);
                sellHouseService.save(sellHouse);
            }
            // 本地图片上传到七牛并更新imgs字段
            String imgs = house.getImgs();
            if (StringUtils.isNotBlank(imgs)) {
                List<String> imageList = JsonUtil.json2Obj(imgs, List.class);

                //删除七牛图片
                try {
                    qiNiuService.deleteMultipleFile(remoteUrlHead + house.getId());
                }catch (Exception e){
                    logger.error("上传文件异常");
                    e.printStackTrace();
                }

                List<String> newList = new ArrayList<String>();
                if (!CollectionUtils.isEmpty(imageList)) {
                    for (String imageUrl : imageList) {
                        String newUrl = localUrlToQiniuUrl(imageUrl, house.getId().toString());
                        newList.add(newUrl);
                    }
                }
                String newImgs = JsonUtil.obj2Json(newList);
                house.setImgs(newImgs);
                house.setCover(newList.get(0));

                try {
                    //删除本地图片
                    for (String str : imageList) {
                        String newStr = str.replace(url, path);
                        File file = new File(newStr);
                        if(file.exists()){
                            file.delete();
                        }else {
                            logger.error("文件不存在：" + file.getPath());
                        }
                    }
                }catch (Exception e){
                    logger.error("删除本地图片失败！");
                }

            }
        }else {
            house.setStatus(HouseStatus.REJECT.getCode());
        }
        houseService.update(house);
        return Result.success();
    }

    private String localUrlToQiniuUrl(String localUrl, String houseId) {
        String newUrl = localUrl;
        if (StringUtils.isNotBlank(localUrl)) {
            try {
                String imagePath = localUrl.replace(url, path);
                File file = new File(imagePath);
                if (file.exists()) {
                    newUrl = bizUploadFile.uploadHouseImageToQiniu(file, houseId);
                } else {
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

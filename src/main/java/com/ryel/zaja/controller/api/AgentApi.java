package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.HouseOrderStatus;
import com.ryel.zaja.config.enums.HouseOrderType;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.BizUtil;
import com.ryel.zaja.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 经济人相关功能
 */
@RestController()
@RequestMapping(value = "/api/agent/",produces = "application/json; charset=UTF-8")
public class AgentApi {
    protected final static Logger logger = LoggerFactory.getLogger(BuyHouseApi.class);
    @Autowired
    private HouseService houseService;
    @Autowired
    private BuyHouseService buyHouseService;
    @Autowired
    private SellHouseService sellHouseService;
    @Autowired
    private UserService userService;
    @Autowired
    private AgentBuyHouseService agentBuyHouseService;
    @Autowired
    private AgentSellHouseService agentSellHouseService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private AgentMaterialService agentMaterialService;
    @Autowired
    private HouseOrderService houseOrderService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private BizUploadFile bizUploadFile;
    @Resource
    private CommunityService communityService;
    @Autowired
    private RecommendService recommendService;

    /**
     * 登录
     * 测试ok
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(String mobile, String password) {
        try {
            if (StringUtils.isBlank(mobile) || StringUtils.isBlank(password)) {
                return Result.error().msg(Error_code.ERROR_CODE_0022).data(new HashMap<>());
            }
            User user = userService.agentLogin(mobile, password);
            if (user == null) {
                return Result.error().msg(Error_code.ERROR_CODE_0004).data(new HashMap<>());
            }
            AgentMaterial agentMaterial = agentMaterialService.findByAgentId(user.getId());
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("user",user);
            data.put("agentMaterial",agentMaterial);
            return Result.success().msg("").data(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 注册
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Result register(User user, AgentMaterial agentMaterial,String verifycode,
                           @RequestParam(required = false) MultipartFile positiveFile,
                           @RequestParam(required = false) MultipartFile negativeFile,
                           @RequestParam(required = false) MultipartFile companyPicFile) {
        try {
            // 校验验证码
            Object origVerCode = redisTemplate.opsForValue().get(user.getMobile());
            if (origVerCode == null || StringUtils.isBlank(verifycode) || !origVerCode.equals(verifycode)) {
                return Result.error().msg(Error_code.ERROR_CODE_0010);
            }
            userService.agentRegister(user,agentMaterial,verifycode,positiveFile,negativeFile,companyPicFile);
            return Result.success().msg("").data(new HashMap<>());
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(e.getCode()).data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 删除房源
     *
     * @param houseId 房源id
     */
    @RequestMapping(value = "deletehouse", method = RequestMethod.POST)
    public Result deletehouse(int houseId) {
        try {
            houseService.agentDeleteHouse(houseId);
            return Result.success().msg("").data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 经纪人编辑房源
     *
     * @param house 房源信息
     */
    @RequestMapping(value = "modifyhouse", method = RequestMethod.POST)
    public Result modifyhouse(House house) {
        try {
            houseService.update(house);
            return Result.success().msg("").data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 获取发布的房源列表
     * @param agentId 经济人id
     */
    @RequestMapping(value = "querymypublishlist", method = RequestMethod.POST)
    public Result querymypublishlist(Integer agentId, Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<House> houses = houseService.pageByAgentId(agentId,
                    new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
            if (null == houses) {
                return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
            }
            Map<String, Object> dataMap = APIFactory.fitting(houses);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 根据小区uid查询访问信息
     * @param uid 小区uid
     */
    @RequestMapping(value = "houselistbycommunityuid", method = RequestMethod.POST)
    public Result houselistbycommunityuid(String uid, Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            List<String> status = new ArrayList<String>();
            status.add(HouseStatus.PUTAWAY_YET.getCode());
            status.add(HouseStatus.IN_CONNECT.getCode());
            Page<House> houses = houseService.pageByCommunityUid(uid,status,
                    new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
            if (null == houses) {
                return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
            }
            Map<String, Object> dataMap = APIFactory.fitting(houses);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Result search(String uid, Integer pageNum, Integer pageSize) {
        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 1;
        }
        Page<House> houses = houseService.findByUid(uid, UserType.AGENT.getCode(), new PageRequest(pageNum - 1, pageSize, Sort.Direction.ASC, "id"));

//        Page<House> houses = houseService.findByUid(uid, UserType.USER.getType(), new PageRequest(pageNum - 1, pageSize, Sort.Direction.ASC, "id"));
        if (0 == houses.getSize()) {
            return Result.error().msg(Error_code.ERROR_CODE_0020);
        }
        Map<String, Object> dataMap = APIFactory.fitting(houses);
        return Result.success().msg("").data(dataMap);
    }



    /**
     * 查房
     * （可以查询到房屋交接中的房屋）
     */
    @RequestMapping(value = "houselist", method = RequestMethod.POST)
    public Result houselist(Integer pageNum, Integer pageSize,BigDecimal longitude,BigDecimal latitude,String cityName) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Map<String, Object> dataMap = houseService.agentPage(pageNum,pageSize,longitude,latitude,cityName);
            return Result.success().msg("").data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }


    /**
     * 我的订单列表
     * @param agentId 经济人id
     */
    @RequestMapping(value = "myorderlist", method = RequestMethod.POST)
    public Result myorderlist(Integer agentId, Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<HouseOrder> page = houseOrderService.pageByAgentId(agentId,
                    new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
            if (null == page) {
                return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
            }
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 我的买房需求列表
     * @param agentId 经济人id
     */
    @RequestMapping(value = "buyhouselist", method = RequestMethod.POST)
    public Result buyhouselist(Integer agentId, Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<BuyHouse> page = agentBuyHouseService.pageBuyHouseByAgentId(agentId,
                    new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
            if (null == page) {
                return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
            }
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 我的卖房需求列表
     * @param agentId 经济人id
     */
    @RequestMapping(value = "sellhouselist", method = RequestMethod.POST)
    public Result sellhouselist(Integer agentId, Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<SellHouse> page = agentSellHouseService.pageSellHouseByAgentId(agentId,
                    new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
            if (null == page) {
                return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
            }
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 全部买房需求列表
     */
    @RequestMapping(value = "allbuyhouselist", method = RequestMethod.POST)
    public Result allbuyhouselist(Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<BuyHouse> page = buyHouseService.pageAll(pageNum,pageSize);
            if (null == page) {
                return Result.error().msg(Error_code.ERROR_CODE_0014);
            }
            for(BuyHouse buyHouse : page.getContent()){
                List<Community> list = communityService.listByUids(buyHouse.getCommunity());
                buyHouse.setCommunityList(list);
            }
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 全部卖房需求列表
     */
    @RequestMapping(value = "allsellhouselist", method = RequestMethod.POST)
    public Result allsellhouselist(Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<SellHouse> page = sellHouseService.pageAll(pageNum,pageSize);
            if (null == page) {
                return Result.error().msg(Error_code.ERROR_CODE_0014);
            }
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 创建订单
     */
    @RequestMapping(value = "createorder", method = RequestMethod.POST)
    public Result createorder(Integer agentId, Integer houseId, String uid, BigDecimal area,BigDecimal price,
                              String toMobile,Integer buyerId) {
        try {

            User agent = new User();
            agent.setId(agentId);
            House house = new House();
            house.setId(houseId);
            User user = new User();
            user.setId(buyerId);
            HouseOrder houseOrder = new HouseOrder();
            houseOrder.setAgent(agent);
            houseOrder.setBuyer(user);
            houseOrder.setCommunity(communityService.findByUid(uid));
            houseOrder.setArea(area);
            houseOrder.setPrice(price);
            houseOrder.setBuyerMobile(toMobile);

            houseService.agentPutawayHouse(houseId);
            return Result.success().msg("").data(new HashMap<>());
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(e.getMessage()).data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 经济人上架房源
     * @param houseId 房源id
     */
    @RequestMapping(value = "putawayhouse", method = RequestMethod.POST)
    public Result putawayhouse(Integer houseId) {
        try {
            houseService.agentPutawayHouse(houseId);
            return Result.success().msg("").data(new HashMap<>());
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(e.getMessage()).data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 下架房源
     * @param houseId 房源id
     */
    @RequestMapping(value = "soldouthouse", method = RequestMethod.POST)
    public Result soldouthouse(Integer houseId) {
        try {
            houseService.agentSoldOutHouse(houseId);
            return Result.success().msg("").data(new HashMap<>());
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(e.getMessage()).data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 接单
     */
    @RequestMapping(value = "receiveorder", method = RequestMethod.POST)
    public Result receiveorder(Integer demandId,Integer agentId,String type) {
        try {
            if(demandId == null || ("10".equals(type) && "20".equals(type))){
                return Result.error().msg(Error_code.ERROR_CODE_0023).data(new HashMap<>());
            }
            User user = new User();
            user.setId(agentId);
            if("10".equals(type)){       // 接买房单
                AgentBuyHouse agentBuyHouse = new AgentBuyHouse();
                agentBuyHouse.setAgent(user);
                BuyHouse buyHouse = new BuyHouse();
                buyHouse.setId(demandId);
                agentBuyHouse.setBuyHouse(buyHouse);
                agentBuyHouseService.save(agentBuyHouse);
            }else{                      // 接卖房单
                AgentSellHouse agentSellHouse = new AgentSellHouse();
                agentSellHouse.setAgent(user);
                SellHouse sellHouse = new SellHouse();
                sellHouse.setId(demandId);
                agentSellHouse.setSellHouse(sellHouse);
                agentSellHouseService.save(agentSellHouse);
            }
            return Result.success().msg("").data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 我的评论
     */
    @RequestMapping(value = "mycomment", method = RequestMethod.POST)
    public Result mycomment(Integer agentId, Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<Comment> page = commentService.pageByAgentId(agentId,
                    new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
            if (null == page) {
                return Result.error().msg(Error_code.ERROR_CODE_0014);
            }
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 我的佣金 前20条
     */
    @RequestMapping(value = "mycommission", method = RequestMethod.POST)
    public Result mycommission(Integer agentId) {
        try {
            List<HouseOrder> list = houseOrderService.findPayedOrderByAgentId(agentId);
            return Result.success().data(list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 发布房源
     */
    @RequestMapping(value = "publishhouse", method = RequestMethod.POST)
    public String publishhouse(@RequestParam(required = false) MultipartFile image1,@RequestParam(required = false) MultipartFile image2,
                               @RequestParam(required = false) MultipartFile image3,@RequestParam(required = false) MultipartFile image4,
                               @RequestParam(required = false) MultipartFile image5,
                               Integer agentId,Integer sellhouseId,String title,BigDecimal price,String tags,Community community,
                               String layout,BigDecimal area,String floor,String renovation,String orientation,String purpose,
                               String features) {
        try {
            // 参数校验
            if(agentId == null || community == null){
                return JsonUtil.obj2ApiJson(Result.error().msg(Error_code.ERROR_CODE_0023).data("agentId或sellhouseId或community为空"));
            }
            // 小区校验
            Community origComm = communityService.createOrUpdateByUid(community);
            // 用户校验
            User agent = userService.findById(agentId);
            if(agent == null){
                throw new BizException("查询到用户为空userId:"+agentId);
            }
            House house = new House();
            // 判断sellhouseId是否存在
            if(sellhouseId != null){
                SellHouse sellHouse = sellHouseService.findById(sellhouseId);
                if(sellHouse == null){
                    throw new BizException("查询到sellHouse为空sellhouseId:"+sellhouseId);
                }
                // 判断经济人是否发布过房源
                List<House> list = houseService.findByAgentIdAndSellHouseId(agent.getId(),sellhouseId);
                if(!CollectionUtils.isEmpty(list)){
                    return JsonUtil.obj2ApiJson(Result.error().msg(Error_code.ERROR_CODE_0028));
                }
                house.setSellHouse(sellHouse);
            }

            house.setCity(community.getCity());
            house.setViewNum(0);
            house.setPrice(price);
            house.setCommission(new BigDecimal(2.5));
            house.setAgent(agent);
            house.setCommunity(community);
            house.setStatus(HouseStatus.SAVED.getCode());
            house.setAddTime(new Date());
            house.setArea(area);
            house.setFeature(features);
            house.setFloor(floor);
            house.setRenovation(renovation);
            house.setOrientation(orientation);
            house.setLayout(layout);
            house.setTitle(title);
            house.setTags(tags);
            house.setPurpose(purpose);
            house.setPublishTime(new Date());
            house.setLastModifiedTime(new Date());
            house.setYear(new SimpleDateFormat("yyyy").format(new Date()));
            if(sellhouseId != null){
                house.setType("10");
            }else{
                house.setType("20");
            }
            houseService.create(house);

            // 把图片更新进去
            Integer houseId = house.getId();
            List<String> imagePathList = new ArrayList<String>();
            if(image1 != null){
                String path = bizUploadFile.uploadHouseImageToQiniu(image1,houseId.toString());
                if(StringUtils.isNotBlank(path)){
                    imagePathList.add(path);
                }
                house.setCover(path);
            }
            if(image2 != null){
                String path = bizUploadFile.uploadHouseImageToQiniu(image2,houseId.toString());
                if(StringUtils.isNotBlank(path)){
                    imagePathList.add(path);
                }
            }
            if(image3 != null){
                String path = bizUploadFile.uploadHouseImageToQiniu(image3,houseId.toString());
                if(StringUtils.isNotBlank(path)){
                    imagePathList.add(path);
                }
            }
            if(image4 != null){
                String path = bizUploadFile.uploadHouseImageToQiniu(image4,houseId.toString());
                if(StringUtils.isNotBlank(path)){
                    imagePathList.add(path);
                }
            }
            if(image5 != null){
                String path = bizUploadFile.uploadHouseImageToQiniu(image5,houseId.toString());
                if(StringUtils.isNotBlank(path)){
                    imagePathList.add(path);
                }
            }
            house.setImgs(JsonUtil.obj2Json(imagePathList));
            houseService.update(house);
            return JsonUtil.obj2ApiJson(Result.success());
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return JsonUtil.obj2ApiJson(Result.error().msg(Error_code.ERROR_CODE_0001).data(e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonUtil.obj2ApiJson(Result.error().msg(Error_code.ERROR_CODE_0001));
        }
    }

    /**
     * 筛选
     * @param pageNum
     * @param pageSize
     * @param price
     * @param area
     * @param layout
     * @param renovation
     * @param floor
     * @return
     */
    @RequestMapping(value = "filter", method = RequestMethod.POST)
    public Result filters(Integer pageNum,
                          Integer pageSize,
                          String price,
                          String area,
                          String layout,
                          String renovation,
                          String floor) {

        Page<House> houses = houseService.filter(pageNum, pageSize, price, area, layout, renovation, floor, UserType.AGENT.getCode());
        Map<String, Object> dataMap = APIFactory.fitting(houses);
        return Result.success().msg("").data(dataMap);
    }


    private Map<String, Object> user2map(User user) {
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        return result;
    }

    /**
     * 相似房源
     */
    @RequestMapping(value = "similar", method = RequestMethod.POST)
    public String similarHouse(Integer houseId) {
        House house = houseService.findById(houseId);
        Map<String, Object> similarHouse = new HashMap<String, Object>();
        List<House> houses = houseService.agentFindSimilar(house.getPrice(),
                house.getCommunity().getUid(),
                house.getArea(),
                house.getRenovation());
        if (!CollectionUtils.isEmpty(houses)) {
            if (houses.size() > 5) {
                for (int i = houses.size() - 1; i > 4; i--) {
                    houses.remove(i);
                }
            }
            similarHouse.put("list", houses);
            return JsonUtil.obj2ApiJson(Result.success().msg("").data(similarHouse));
        } else {
            return JsonUtil.obj2ApiJson(Result.success().msg("").data(similarHouse.put("list", recommendService.findByStatus("10"))));
        }
    }

    /**
     * 修改头像
     */
    @RequestMapping(value = "modifyhead")
    public Result modifyhead(Integer agentId, @RequestParam(required = true) MultipartFile image){
        try {
            String path = bizUploadFile.uploadUserImageToQiniu(image,agentId);
            if(StringUtils.isNotBlank(path)){
                Map<String ,String> dataMap = new HashMap<>();
                dataMap.put("remotePath",path);
                User user = userService.findById(agentId);
                user.setHead(path);
                userService.update(user);
                return Result.success().msg("").data(dataMap);
            }else {
                throw new BizException("修改头像异常");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return Result.error().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 经济人发布订单
     */
    @RequestMapping(value = "agentpublishorder")
    public Result agentpublishorder(Integer agentId, Integer houseId, Community community, BigDecimal area, BigDecimal price,
                                    String toMobile) {
        try {
            HouseOrder houseOrder = new HouseOrder();
            // 查经济人
            User agent = userService.findById(agentId);
            if (agent == null) {
                throw new BizException(Error_code.ERROR_CODE_0023, "查询用户信息为空");
            }
            // 查买房人
            User user = userService.findByMobile(toMobile);
            if (user == null) {
                throw new BizException(Error_code.ERROR_CODE_0023, "根据toMobile查询user为空");
            }
            // 订单类型
            String type;
            if (houseId != null) {
                type = HouseOrderType.FROM_HOUSE.getCode();
                House house = houseService.findById(houseId);
                if(house == null){
                    throw new BizException(Error_code.ERROR_CODE_0025, "查询到house is null");
                }
                List<HouseOrder> list = houseOrderService.findPayedOrderByHouseId(houseId);
                if(!CollectionUtils.isEmpty(list)){
                    throw new BizException(Error_code.ERROR_CODE_0026, "房源已经存在已支付的订单");
                }
                houseOrder.setHouse(house);
                houseOrder.setCommunity(house.getCommunity());
                houseOrder.setSeller(house.getSellHouse().getUser());
            } else {
                type = HouseOrderType.FROM_CUSTOM.getCode();
                if (area == null || price == null || community == null
                        || StringUtils.isBlank(community.getUid()) || StringUtils.isBlank(community.getName())) {
                    throw new BizException(Error_code.ERROR_CODE_0023, "houseId为空，且其他信息也为空");
                }
                Community origComm = communityService.findByUid(community.getUid());
                if (null == origComm) {
                    communityService.create(community);
                }
                houseOrder.setCommunity(community);
                houseOrder.setArea(area);
                houseOrder.setPrice(price);
            }
            // 生产订单号
            String code = BizUtil.getOrderCode();
            houseOrder.setAgent(agent);
            houseOrder.setBuyer(user);
            houseOrder.setBuyerMobile(toMobile);
            houseOrder.setStatus(HouseOrderStatus.NO_ORDER.getCode());
            houseOrder.setType(type);
            houseOrder.setAddTime(new Date());
            houseOrder.setCode(code);
            houseOrderService.save(houseOrder);
            return Result.success();
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(e.getCode());
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 经济人确定订单
     */
    @RequestMapping(value = "agentconfirmorder")
    public Result agentconfirmorder(Integer agentId, Integer orderId){
        try {
            if (agentId == null || orderId == null) {
                return Result.error().msg(Error_code.ERROR_CODE_0023);
            }
            HouseOrder order = houseOrderService.findById(orderId);
            if(order == null){
                return Result.error().msg(Error_code.ERROR_CODE_0025);
            }
            if(!HouseOrderStatus.WAIT_AGENT_COMFIRM.getCode().equals(order.getStatus())){
                return Result.error().msg(Error_code.ERROR_CODE_0025);
            }
            order.setStatus(HouseOrderStatus.WAIT_USER_COMFIRM.getCode());
            houseOrderService.update(order);
            return Result.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001);
        }
    }

}


package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.*;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.push.Push;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.BizUtil;
import com.ryel.zaja.utils.GetDistanceUtil;
import com.ryel.zaja.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 经济人相关功能
 */
@RestController()
@RequestMapping(value = "/api/agent/", produces = "application/json; charset=UTF-8")
public class AgentApi {
    protected final static Logger logger = LoggerFactory.getLogger(AgentApi.class);
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
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private BizUploadFile bizUploadFile;
    @Resource
    private CommunityService communityService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private HouseTagService tagService;
    @Autowired
    private PushDeviceService pushService;
    @Autowired
    private AgentLocationService agentLocationService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private TradeRecordService tradeRecordService;
    @Autowired
    private ImgWallService imgWallService;
    @Autowired
    private QiNiuService qiNiuService;
    @Autowired
    private ComplainService complainService;

    /**
     * 登录
     * 测试ok
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(String mobile, String password) {
            try {
            if (StringUtils.isBlank(mobile) || StringUtils.isBlank(password)) {
                return Result.error().msg(Error_code.ERROR_CODE_0023).data(new HashMap<>());
            }
            User user = userService.agentLogin(mobile, password);
            if (user == null) {
                return Result.error().msg(Error_code.ERROR_CODE_0004).data(new HashMap<>());
            } else {
                //判断是否为用户
                if (UserType.USER.getCode().equals(user.getType())) {
                    return Result.error().msg(Error_code.ERROR_CODE_0029).data(new HashMap<>());
                }
                //没有审核通过
                if(!AgentRegisterStatus.APPROVE_PASS.getCode().equals(user.getAgentStatus()))
                {
                    return Result.error().msg(Error_code.ERROR_CODE_0036).data(new HashMap<>());
                }
            }
            AgentMaterial agentMaterial = agentMaterialService.findByAgentId(user.getId());
            Map<String, Object> data = new HashMap<String, Object>();

            Map userMap = APIFactory.filterUser(user);
            userMap.put("idcard",agentMaterial.getIdcard());
            data.put("user", userMap);
//            data.put("agentMaterial", agentMaterial);
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
    public Result register(User user, AgentMaterial agentMaterial, String verifycode,
                           @RequestParam(required = false) MultipartFile positiveFile,
                           @RequestParam(required = false) MultipartFile negativeFile,
                           @RequestParam(required = false) MultipartFile companyPicFile) {
        try {
            // 校验验证码
            Object origVerCode = stringRedisTemplate.opsForValue().get(user.getMobile());
            if (null == origVerCode) {
                return Result.error().msg(Error_code.ERROR_CODE_0010).data(new HashMap<>());
            }
            if (!origVerCode.equals(verifycode)) {
                return Result.error().msg(Error_code.ERROR_CODE_0009).data(new HashMap<>());
            }

            userService.agentRegister(user, agentMaterial, verifycode, positiveFile, negativeFile, companyPicFile);
            return Result.success().msg("").data(new HashMap<>());
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(e.getCode()).data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    @RequestMapping(value = "changeaccount", method = RequestMethod.POST)
    public Result changeAccount(Integer agentId, String mobile, String password, String verCode) {
        try {
            User user = userService.findById(agentId);

            if (null == user) {
                return Result.error().msg(Error_code.ERROR_CODE_0012).data(new HashMap<>());//手机号未注册用户
            }

            ValueOperations<String, String> valueops = stringRedisTemplate.opsForValue();
            String origVerCode = valueops.get(mobile);

            if (null == origVerCode) {
                return Result.error().msg(Error_code.ERROR_CODE_0010).data(new HashMap<>());
            }
            if (!origVerCode.equals(verCode)) {
                return Result.error().msg(Error_code.ERROR_CODE_0009).data(new HashMap<>());
            }

            User mobileuser = userService.findByMobile(mobile);
            if(mobileuser != null)
            {
                return Result.error().msg(Error_code.ERROR_CODE_0034).data(new HashMap<>());
            }

            user.setMobile(mobile);
            user.setPassword(password);
            userService.update(user);

            return Result.success().msg("").data(new HashMap<>());
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
    public Result deletehouse(Integer agentId, Integer houseId) {
        try {
            houseService.agentDeleteHouse(houseId,agentId);
            return Result.success().msg("").data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 获取发布的房源列表
     *
     * @param agentId 经济人id
     */
    @RequestMapping(value = "mypublishlist", method = RequestMethod.POST)
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

            Map<String, Object> dataMap = APIFactory.fitting(houses);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 根据小区uid查询访问信息
     *
     * @param uid 小区uid
     */
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Result searchbycommunityuid(String uid, Integer pageNum, Integer pageSize) {
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
            Page<House> houses = houseService.pageByCommunityUid(uid, status,
                    new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));

            Map<String, Object> dataMap = APIFactory.fitting(houses);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }


    /**
     * 查房
     * （可以查询到房屋交接中的房屋）
     */
    @RequestMapping(value = "houselist", method = RequestMethod.POST)
    public Result houselist(Integer pageNum, Integer pageSize, Double longitude, Double latitude, String city) {
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

            if (null != longitude && null != latitude && null != city) {

                List<Community> communityBycity = communityService.findByCity(city);
                List<String> uids = GetDistanceUtil.nearbyCommunity(longitude, latitude, communityBycity);
                if (!uids.isEmpty() && null != uids) {
                    Page<House> page = houseService.findByCommunitiesStatus(status, uids, new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
                    Map<String, Object> dataMap = APIFactory.fitting(page);
                    return Result.success().msg("").data(dataMap);
                } else {
                    Page<House> page = houseService.findByAddTime(UserType.AGENT.getCode(), new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC));
                    Map<String, Object> dataMap = APIFactory.fitting(page);
                    return Result.success().msg("").data(dataMap);
                }
            } else {
                Page<House> page = houseService.findByAddTime(UserType.AGENT.getCode(), new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "addTime"));
                Map<String, Object> dataMap = APIFactory.fitting(page);
                return Result.success().msg("").data(dataMap);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }


    /**
     * 我的订单列表
     *
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

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            for (HouseOrder order : page.getContent()) {
                Map neworder = new HashMap<String, Object>();
                //说明是二次编辑的房源信息
                if (HouseType.FIRST.getCode().equals(order.getType())) {
                    neworder.put("house", order.getHouse());
                }

                neworder.put("community", order.getCommunity());
                neworder.put("area", order.getArea());
                neworder.put("price", order.getPrice());
                neworder.put("commission", order.getCommission());
                neworder.put("discount",order.getDiscount());

                neworder.put("id", order.getId());
                neworder.put("code", order.getCode());
                neworder.put("buyer", order.getBuyer());
                neworder.put("status", order.getStatus());
                neworder.put("type", order.getType());
                neworder.put("username", order.getUsername());
                neworder.put("idcard", order.getIdcard());
                neworder.put("authorId", order.getAuthorId());
                neworder.put("floor", order.getFloor());
                neworder.put("addTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(order.getAddTime()));

                list.add(neworder);
            }

            Map<String, Object> dataMap = APIFactory.fitting(page, list);
            return Result.success().msg("").data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 我的买房需求列表
     *
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

            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 我的卖房需求列表
     *
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
    @RequestMapping(value = "allbuyhouselist")
    public Result allbuyhouselist(Integer agentId, Integer pageNum, Integer pageSize, Double longitude, Double latitude, String city) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<BuyHouse> page = null;

            //List的作用：去掉经纪人自己发的需求和已经接单过的需求
            List<Integer> list = agentBuyHouseService.findBuyHouseByAgentId(agentId);
            list.addAll(buyHouseService.findByUserIdAsId(agentId));
            //给一个默认值，防止list为null的时候sql报错
            list.add(-1);

            if (null != longitude && null != latitude && null != city) {
                List<String> uids = BizUtil.nearbyCommunity(longitude, latitude, city, communityService);
                page = buyHouseService.agentPage(agentId, pageNum, pageSize, uids, list);
            } else {
                page = buyHouseService.agentPage(agentId, pageNum, pageSize, null, list);
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
    public Result allsellhouselist(Integer agentId, Integer pageNum, Integer pageSize, Double longitude, Double latitude, String city) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }

            Page<SellHouse> page;
            List<Integer> list = agentSellHouseService.findSellHouseByAgentId(agentId);
            list.addAll(sellHouseService.findByUserIdAsId(agentId));
            //给一个默认值，防止list为null的时候sql报错
            list.add(-1);

            if (null != longitude && null != latitude && null != city) {
                List<String> uids = BizUtil.nearbyCommunity(longitude, latitude, city, communityService);
                page = sellHouseService.agentPage(pageNum, pageSize, uids, list);
            } else {
                page = sellHouseService.agentPage(pageNum, pageSize, null, list);
            }

            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 经济人上架房源
     *
     * @param houseId 房源id
     */
    @RequestMapping(value = "putuphouse", method = RequestMethod.POST)
    public Result putuphouse(Integer agentId, Integer houseId) {
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
     *
     * @param houseId 房源id
     */
    @RequestMapping(value = "putdownhouse", method = RequestMethod.POST)
    public Result putdownhouse(Integer agentId, Integer houseId) {
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
    @RequestMapping(value = "receivedemand", method = RequestMethod.POST)
    public Result receiveorder(Integer demandId, Integer agentId, String type) {
        try {
            if (demandId == null) {
                return Result.error().msg(Error_code.ERROR_CODE_0023).data(new HashMap<>());
            }
            User user = new User();
            user.setId(agentId);
            if ("10".equals(type)) {       // 接买房单

                AgentBuyHouse agentBuyHouse = new AgentBuyHouse();
                agentBuyHouse.setAgent(user);
                BuyHouse buyHouse = buyHouseService.findById(demandId);
                if (buyHouse.getUser().getId().equals(agentId)) {
                    return Result.error().msg(Error_code.ERROR_CODE_0033).data(new HashMap<>());
                }
                agentBuyHouse.setBuyHouse(buyHouse);
                agentBuyHouseService.create(agentBuyHouse);
            } else {                      // 接卖房单

                AgentSellHouse agentSellHouse = new AgentSellHouse();
                agentSellHouse.setAgent(user);
                SellHouse sellHouse = sellHouseService.findById(demandId);
                if (sellHouse.getUser().getId().equals(agentId)) {
                    return Result.error().msg(Error_code.ERROR_CODE_0033).data(new HashMap<>());
                }
                agentSellHouse.setSellHouse(sellHouse);
                agentSellHouseService.create(agentSellHouse);
            }
            return Result.success().msg("").data(new HashMap<>());
        } catch (BizException be) {
            logger.error(be.getMessage(), be);
            return Result.error().msg(be.getMessage()).data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
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


            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            for (Comment comment : page.getContent()) {
                Map newcomment = new HashMap<String, Object>();

                newcomment.put("content", comment.getContent());
                newcomment.put("addTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getAddTime()));
                newcomment.put("user", comment.getUser());
                newcomment.put("id", comment.getId());
                newcomment.put("star", comment.getStar());
                newcomment.put("orderId", comment.getHouseOrder().getId());
                list.add(newcomment);
            }
            Map<String, Object> dataMap = APIFactory.fitting(page,list);
            return Result.success().msg("").data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }


    /**
     * 更新房源
     */
    @RequestMapping(value = "updatehouse", method = RequestMethod.POST)
    public String updatehouse(@RequestParam(required = false) MultipartFile image1, @RequestParam(required = false) MultipartFile image2,
                               @RequestParam(required = false) MultipartFile image3, @RequestParam(required = false) MultipartFile image4,
                               @RequestParam(required = false) MultipartFile image5, @RequestParam(required = false) MultipartFile image6,
                              @RequestParam(required = false) MultipartFile image7, @RequestParam(required = false) MultipartFile image8,
                              @RequestParam(required = false) MultipartFile image9,
                               Integer agentId, Integer houseId, String title, BigDecimal price, String tags, Community community,
                               String layout, BigDecimal area, String floor, String renovation, String orientation, String purpose,
                               String features) {
        try {
            // 参数校验
            if (agentId == null || community == null) {
                return JsonUtil.obj2ApiJson(Result.error().msg(Error_code.ERROR_CODE_0023).data("agentId或sellhouseId或community为空"));
            }
            // 小区校验
            Community origComm = communityService.createOrUpdateByUid(community);
            // 用户校验
            User agent = userService.findById(agentId);
            if (agent == null) {
                throw new BizException("查询到用户为空userId:" + agentId);
            }
            House house = houseService.findById(houseId);

            if (house == null)
            {
                throw new BizException("查询到用户为空houseId:" + houseId);
            }

            house.setCity(community.getCity());
            house.setPrice(price);
            house.setCommission(price.multiply(BigDecimal.valueOf(250)));
            house.setCommunity(community);
            house.setStatus(HouseStatus.SAVED.getCode());
            house.setArea(area);
            house.setFeature(features);
            house.setFloor(floor);
            house.setRenovation(renovation);
            house.setOrientation(orientation);
            house.setLayout(layout);
            house.setTitle(title);
            house.setTags(tags);
            house.setPurpose(purpose);
            house.setLastModifiedTime(new Date());


            // 把图片更新进去
            List<String> imagePathList = new ArrayList<String>();
            if (image1 != null) {
                String path = bizUploadFile.uploadHouseImageToLocal(image1, houseId);
                if (StringUtils.isNotBlank(path)) {
                    imagePathList.add(path);
                }
                house.setCover(path);
            }
            if (image2 != null) {
                String path = bizUploadFile.uploadHouseImageToLocal(image2, houseId);
                if (StringUtils.isNotBlank(path)) {
                    imagePathList.add(path);
                }
            }
            if (image3 != null) {
                String path = bizUploadFile.uploadHouseImageToLocal(image3, houseId);
                if (StringUtils.isNotBlank(path)) {
                    imagePathList.add(path);
                }
            }
            if (image4 != null) {
                String path = bizUploadFile.uploadHouseImageToLocal(image4, houseId);
                if (StringUtils.isNotBlank(path)) {
                    imagePathList.add(path);
                }
            }
            if (image5 != null) {
                String path = bizUploadFile.uploadHouseImageToLocal(image5, houseId);
                if (StringUtils.isNotBlank(path)) {
                    imagePathList.add(path);
                }
            }
            if (image6 != null) {
                String path = bizUploadFile.uploadHouseImageToLocal(image6, houseId);
                if (StringUtils.isNotBlank(path)) {
                    imagePathList.add(path);
                }
            }
            if (image7 != null) {
                String path = bizUploadFile.uploadHouseImageToLocal(image7, houseId);
                if (StringUtils.isNotBlank(path)) {
                    imagePathList.add(path);
                }
            }
            if (image8 != null) {
                String path = bizUploadFile.uploadHouseImageToLocal(image8, houseId);
                if (StringUtils.isNotBlank(path)) {
                    imagePathList.add(path);
                }
            }
            if (image9 != null) {
                String path = bizUploadFile.uploadHouseImageToLocal(image9, houseId);
                if (StringUtils.isNotBlank(path)) {
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
     * 发布房源
     */
    @RequestMapping(value = "publishhouse", method = RequestMethod.POST)
    public String publishhouse(@RequestParam(value = "imgs", required = false) MultipartFile[] imgs,
                               Integer agentId, Integer sellhouseId, String title, BigDecimal price, String tags, Community community,
                               String layout, BigDecimal area, String floor, String renovation, String orientation, String purpose,
                               String features) {
        try {
            // 参数校验
            if (agentId == null || community == null) {
                return JsonUtil.obj2ApiJson(Result.error().msg(Error_code.ERROR_CODE_0023).data("agentId或sellhouseId或community为空"));
            }
            // 小区校验
            Community origComm = communityService.createOrUpdateByUid(community);
            // 用户校验
            User agent = userService.findById(agentId);
            if (agent == null) {
                throw new BizException("查询到用户为空userId:" + agentId);
            }
            House house = new House();
            // 判断sellhouseId是否存在
            if (sellhouseId != null) {
                SellHouse sellHouse = sellHouseService.findById(sellhouseId);
                if (sellHouse == null) {
                    throw new BizException("查询到sellHouse为空sellhouseId:" + sellhouseId);
                }
                // 判断经济人是否发布过房源
                List<House> list = houseService.findByAgentIdAndSellHouseId(agent.getId(), sellhouseId);
                if (!CollectionUtils.isEmpty(list)) {
                    return JsonUtil.obj2ApiJson(Result.error().msg(Error_code.ERROR_CODE_0028));
                }
                house.setSellHouse(sellHouse);
            }

            house.setCity(community.getCity());
            house.setViewNum(0);
            house.setPrice(price);
            house.setCommission(price.multiply(BigDecimal.valueOf(250)));
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
            if (sellhouseId != null) {
                house.setType("10");
            } else {
                house.setType("20");
            }

            //保存房屋标签数据
            String[] tagList= tags.split("\\|");
            for (String tag : tagList)
            {
                tagService.upDateTagNum(tag);
            }

            houseService.create(house);

            // 把图片更新进去
            Integer houseId = house.getId();
            List<String> imagePathList = new ArrayList<String>();
            if(null != imgs){
                for(int i=0; i<imgs.length; i++){
                    String path = bizUploadFile.uploadHouseImageToLocal(imgs[i], houseId);
                    if (StringUtils.isNotBlank(path)) {
                        imagePathList.add(path);
                    }
                    if (0 == i) {
                        house.setCover(path);
                    }
                }
            }
            house.setImgs(JsonUtil.obj2Json(imagePathList));
            houseService.update(house);
            return JsonUtil.obj2ApiJson(Result.success().msg("").data(new HashMap<>()));
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
     *
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

        Page<House> houses = houseService.filter(pageNum, pageSize, price, area, layout, renovation, floor, null, UserType.AGENT.getCode());
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
        //删除自己
        houses.remove(houseService.findById(houseId));
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
    public Result modifyhead(Integer agentId, @RequestParam(required = true) MultipartFile imgs) {
        try {
            String path = bizUploadFile.uploadUserImageToQiniu(imgs, agentId);
            if (StringUtils.isNotBlank(path)) {
                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("remotePath", path);
                User user = userService.findById(agentId);
                user.setHead(path);
                userService.update(user);
                return Result.success().msg("").data(dataMap);
            } else {
                throw new BizException("修改头像异常");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 经济人发布订单
     */
    @RequestMapping(value = "publishorder")
    public Result publishorder(Integer agentId, Integer houseId, Community community, BigDecimal area, BigDecimal price,
                                    String toMobile, BigDecimal discount, String username, String idcard, String floor) {
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

            if (user.getId() == agentId){
                throw new BizException(Error_code.ERROR_CODE_0019,"不能给自己发订单！");
            }
            // 订单类型
            String type;
            if (houseId != null) {
                type = HouseOrderType.FROM_HOUSE.getCode();
                House house = houseService.findById(houseId);
                if (house == null || !HouseStatus.getAgentCanSeeStatus().contains(house.getStatus())) {
                    throw new BizException(Error_code.ERROR_CODE_0040, "此房源不在上架状态");
                }

                HouseOrder houseOrder1 = houseOrderService.findByHouseIdAndUserId(houseId, user.getId());
                if(null != houseOrder1){
                    throw new BizException(Error_code.ERROR_CODE_0047, "您已经向这用户已发起过此订单");
                }

                List<HouseOrder> list = houseOrderService.findPayedOrderByHouseId(houseId);
                if (!CollectionUtils.isEmpty(list)) {
                    throw new BizException(Error_code.ERROR_CODE_0026, "房源已经存在已支付的订单");
                }
                houseOrder.setHouse(house);
                houseOrder.setCommunity(house.getCommunity());
                if (null != house.getSellHouse()) {
                    houseOrder.setSeller(house.getSellHouse().getUser());
                } else {
                    houseOrder.setSeller(userService.findById(agentId));
                }
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
            }
            // 生产订单号
            String code = BizUtil.getOrderCode();
            houseOrder.setAgent(agent);
            houseOrder.setBuyer(user);
            houseOrder.setAuthorId(agent.getId());
            houseOrder.setBuyerMobile(toMobile);
            houseOrder.setStatus(HouseOrderStatus.NO_ORDER.getCode());
            houseOrder.setType(type);
            houseOrder.setAddTime(new Date());
            houseOrder.setCode(code);
            houseOrder.setArea(area);
            houseOrder.setPrice(price);
            //实际佣金等于price*250-discount 单位：元
            discount = discount == null ? BigDecimal.ZERO : discount;
            houseOrder.setCommission(price.multiply(BigDecimal.valueOf(250)).subtract(discount));

            houseOrder.setIdcard(idcard);
            houseOrder.setUsername(username);
            houseOrder.setFloor(floor);

            houseOrder.setDiscount(discount);

            houseOrderService.save(houseOrder);

            //发送推送信息
            PushDevice pushDevice = pushService.findByUser(user);
            if(null!=pushDevice)
            {
                if(pushDevice.getDevice().equals("Android"))
                {
                    Push push = new Push();
                    push.sendAndroidOrder(user.getId());
                }else
                {
                    Push push = new Push();
                    push.sendIOSOrder(user.getId());
                }
            }
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
     * 经济人确定订单
     */
    @RequestMapping(value = "confirmorder")
    public Result agentconfirmorder(Integer agentId, Integer orderId) {
        try {
            if (agentId == null || orderId == null) {
                return Result.error().msg(Error_code.ERROR_CODE_0023);
            }
            HouseOrder order = houseOrderService.findById(orderId);
            if (order == null) {
                return Result.error().msg(Error_code.ERROR_CODE_0025);
            }
            if (!HouseOrderStatus.IN_CONNECT.getCode().equals(order.getStatus())) {
                return Result.error().msg(Error_code.ERROR_CODE_0025);
            }
            order.setStatus(HouseOrderStatus.WAIT_USER_COMFIRM.getCode());
            houseOrderService.update(order);
            return Result.success().msg("").data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 经济人确定申请退款
     */
    @RequestMapping(value = "refundorder")
    public Result agentrefundorder(Integer agentId, Integer orderId) {
        try {
            if (agentId == null || orderId == null) {
                return Result.error().msg(Error_code.ERROR_CODE_0023);
            }
            HouseOrder order = houseOrderService.findById(orderId);
            if (order == null) {
                return Result.error().msg(Error_code.ERROR_CODE_0025);
            }
            if (!HouseOrderStatus.APPLY_REBATE.getCode().equals(order.getStatus())) {
                return Result.error().msg(Error_code.ERROR_CODE_0025);
            }
            order.setStatus(HouseOrderStatus.AGENT_COMFIRM_REBATE.getCode());
            houseOrderService.update(order);

            //对应房源的状态变成上架
            House house = order.getHouse();
            house.setStatus(HouseStatus.PUTAWAY_YET.getCode());
            houseService.update(house);

            return Result.success().msg("").data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 获取房屋的tag标签
     */
    @RequestMapping(value = "listtags")
    public Result getlisttags()
    {
        try {

            Map<String, Object> tags = new HashMap<String, Object>();
            tags.put("list",tagService.findAll());
            return Result.success().data(tags);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }


    /**
     * 获取经纪人的接单数 房源总数 评分
     * @param agentId
     * @return
     */
    @RequestMapping(value = "count", method = RequestMethod.POST)
    public Result count(Integer agentId){
        try{

            Map<String, Object> dataMap = new HashMap<>();

            //接单总数
            Long sellHouseSum = agentSellHouseService.count(agentId);
            Long buyHouseSum  = agentBuyHouseService.count(agentId);
            Long orderSum = sellHouseSum + buyHouseSum;

            //房源总数
            Long houseSum  = houseService.count(agentId);

            //评分
            Double average = commentService.average(agentId);
            average = average == null ?  0.0 : average;
            dataMap.put("avg", average);
            dataMap.put("orderSum", orderSum);
            dataMap.put("houseSum", houseSum);

            Map<String, Object> result = new HashMap<>();
            List<Object> list = imgWallService.findByAgentId(agentId);
            result.put("list", list);
            result.put("analysis", dataMap);

            return Result.success().msg("").data(result);

        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 附近的经纪人
     * @param longitude
     * @param latitude
     * @param district
     * @return
     */
    @RequestMapping(value = "nearbyagent", method = RequestMethod.POST)
    public Result nearbyAgent(Double longitude, Double latitude, String district, String city){
        try{
            if(null == longitude || null == latitude){
                return Result.error().msg(Error_code.ERROR_CODE_0023).data(new HashMap<>());
            }
            List<AgentLocation> listByDisOrCity = new ArrayList<>();

            if(null != district) {
                listByDisOrCity = agentLocationService.findByDis(district);
            }
            if(listByDisOrCity.isEmpty()){
                listByDisOrCity = agentLocationService.findByCity(city);
            }

            //筛选5公里之内的经纪人
            List<AgentLocation> listByLoc = agentLocationService.findByLoc(longitude, latitude, listByDisOrCity);
            if(listByLoc.isEmpty()){
                return Result.success().msg("").data(new HashMap<>());
            }
            List<Map<String, Object>> resultList = new ArrayList<>();
            for(AgentLocation agentLocation : listByLoc){
                resultList.add(APIFactory.filterAgentLocation(agentLocation));
            }
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("nearbyagent", resultList);
            return Result.success().msg("").data(dataMap);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 创建或者更新经济人的位置信息
     * @param agentLocation
     * @return
     */
    @RequestMapping(value = "createlocation", method = RequestMethod.POST)
    public Result createLocaion(Integer agentId, AgentLocation agentLocation){
        try{

            User agent = userService.findById(agentId);
            if (null == agent)
            {
                return Result.error().msg(Error_code.ERROR_CODE_0023);
            }
            agentLocation.setAgent(agent);
            AgentLocation origAgentLocation = agentLocationService.findByAgent(agentId);
            if(null == origAgentLocation){
                agentLocationService.create(agentLocation);
            }else{
                agentLocationService.update(origAgentLocation, agentLocation);
            }
            return Result.success().msg("").data(new HashMap<>());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());        }
    }


    /**
     * 反馈
     * @param agentId
     * @param content
     * @return
     */
    @RequestMapping(value = "feedback", method = RequestMethod.POST)
    public Result feedback(Integer agentId,
                           String content,
                           @RequestParam(value = "imgs", required = false) MultipartFile[] imgs){

        try{
            List<String> imagePathList = new ArrayList<String>();
            if (null != imgs){
                for(MultipartFile img: imgs){
                    String path = bizUploadFile.uploadFeedbackImageToQiniu(img, agentId);
                    if (StringUtils.isNotBlank(path)) {
                        imagePathList.add(path);
                    }
                }
            }

            Feedback feedback = new Feedback();

            feedback.setImgs(JsonUtil.obj2Json(imagePathList));
            feedback.setUser(userService.findById(agentId));
            feedback.setContent(content);

            feedbackService.create(feedback);


            return Result.success().msg("").data(new HashMap<>());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 经纪人确认接单,状态值从"10"变成"20"
     * @param agentId
     * @param orderId
     * @return
     */
    @RequestMapping(value = "receiveorder", method = RequestMethod.POST)
    public Result agentReceiveOrder(Integer agentId, Integer orderId, BigDecimal discount){
        try{
            HouseOrder houseOrder = houseOrderService.findByAgentIdAndOrderId(agentId, orderId);

            houseOrder.setStatus(HouseOrderStatus.WAIT_PAYMENT.getCode());

            discount = discount == null ? BigDecimal.ZERO : discount;
            houseOrder.setDiscount(discount);
            houseOrder.setCommission(houseOrder.getCommission().subtract(discount));
            houseOrderService.update(houseOrder);

            //更新用户信息
            User user = houseOrder.getBuyer();
            user.setUsername(houseOrder.getUsername());
            user.setIdcard(houseOrder.getIdcard());
            userService.update(user);

            return Result.success().msg("").data(new HashMap<>());
        }catch (BizException be){
            logger.error(be.getMessage(), be);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 经纪人拒绝接单
     * @param agentId
     * @param orderId
     * @return
     */
    @RequestMapping(value = "rejectorder", method = RequestMethod.POST)
    public Result agentRejectOrder(Integer agentId, Integer orderId){
        try{
            HouseOrder houseOrder = houseOrderService.findByAgentIdAndOrderId(agentId, orderId);

            houseOrder.setStatus(HouseOrderStatus.REJECT.getCode());
            houseOrderService.update(houseOrder);
            return Result.success().msg("").data(new HashMap<>());
        }catch (BizException be){
            logger.error(be.getMessage(), be);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 经纪人投诉，订单状态值从"10"变成"12"，投诉内容写进数据库
     * @param agentId
     * @param orderId
     * @param content
     * @return
     */
    @RequestMapping(value = "complain", method = RequestMethod.POST)
    public Result agentComplain(Integer agentId, Integer orderId, String content){
        try{
            complainService.create(agentId, orderId, content);

            HouseOrder houseOrder = houseOrderService.findByBuyerIdAndOrderId(agentId, orderId);
            houseOrder.setStatus(HouseOrderStatus.COMPLAIN.getCode());
            houseOrderService.update(houseOrder);
            return Result.success().msg("").data(new HashMap<>());
        }catch (BizException be){
            logger.error(be.getMessage(), be);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 我的佣金历史记录
     * @param agentId
     * @return
     */
    @RequestMapping(value = "intomoneyhistory", method = RequestMethod.POST)
    public Result intoMoneyHistory(Integer agentId){
        try{
            Page<TradeRecord> page = tradeRecordService.findByInThirdCustId(agentId);
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        }catch (BizException be){
            logger.error(be.getMessage(), be);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 经纪人上传照片墙
     * @param agentId
     * @param imgs
     * @return
     */
    @RequestMapping(value = "imgwall", method = RequestMethod.POST)
    public Result imgWall(Integer agentId, @RequestParam(value="imgs", required = false) MultipartFile[] imgs){
        try{

            Long imgWalls = imgWallService.countImg(agentId);
            if(imgWalls > 8){
                throw new Exception();
            }
            for(MultipartFile img: imgs){
                ImgWall imgWall = new ImgWall();
                String path = bizUploadFile.uploadUserImageToQiniu(img, agentId);
                imgWall.setUserId(agentId);
                imgWall.setImg(path);
                imgWall.setAddTime(new Date());
                imgWallService.save(imgWall);
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
        return viewImgWall(agentId);
    }

    /**
     * 经纪人删除照片墙中的图片
     * @param agentId
     * @param urls
     * @return
     */
    @RequestMapping(value = "deleteimgwall", method = RequestMethod.POST)
    public Result deleteImgwall(Integer agentId, String urls){
        try{

            String[] path = JsonUtil.json2Obj(urls,String[].class);
            for(String url : path){
                ImgWall wall =  imgWallService.findByAgentIdAndUrl(agentId, url);
                if(wall !=null) {
                    String filename = url.substring(url.indexOf("user"));
                    qiNiuService.deleteOneFile(filename);
                    imgWallService.delete(wall);
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
        return Result.success().msg("").data(new HashMap<>());
    }

    /**
     * 查看照片墙
     * @return
     */
    @RequestMapping(value = "viewimgwall", method = RequestMethod.POST)
    public Result viewImgWall(Integer agentId){

        try{
            Map<String, Object> data = new HashMap();
            List<Object> list = imgWallService.findByAgentId(agentId);
            data.put("list", list);
            return Result.success().msg("").data(data);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }
}


package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.JsonUtil;
import com.ryel.zaja.utils.bean.FileBo;
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
import java.util.*;

/**
 * 经济人相关功能
 */
@RestController()
@RequestMapping("/api/agent/")
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
     * 待开发
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Result register(User user, AgentMaterial agentMaterial,String verifyCode) {
        try {
            Object origVerCode = redisTemplate.opsForValue().get(user.getMobile());
            if (origVerCode == null || StringUtils.isBlank(verifyCode) || !origVerCode.equals(verifyCode)) {
                return Result.error().msg(Error_code.ERROR_CODE_0010);
            }
            userService.agentRegister(user,agentMaterial,verifyCode);
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
     * @param userId 经济人id
     */
    @RequestMapping(value = "querymypublishlist", method = RequestMethod.POST)
    public Result querymypublishlist(Integer userId, Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<House> houses = houseService.pageByAgentId(userId,
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
     * @param communityUid 小区uid
     */
    @RequestMapping(value = "houselistbycommunityuid", method = RequestMethod.POST)
    public Result houselistbycommunityuid(String communityUid, Integer pageNum, Integer pageSize) {
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
            Page<House> houses = houseService.pageByCommunityUid(communityUid,status,
                    new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
            if (null == houses) {
                return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
            }
            Map<String, Object> dataMap = APIFactory.fitting(houses);
            return Result.success().data(dataMap).data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    @RequestMapping(value = "houselist", method = RequestMethod.POST)
    public Result houselist(Integer pageNum, Integer pageSize) {
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
            Page<House> houses = houseService.agentPage(status, new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
            if (null == houses) {
                return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
            }
            Map<String, Object> dataMap = APIFactory.fitting(houses);
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
            return Result.error().msg(Error_code.ERROR_CODE_0001);
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
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().msg("").data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001);
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
            return Result.error().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 创建订单
     */
    @RequestMapping(value = "createorder", method = RequestMethod.POST)
    public Result createorder(Integer userId, Integer houseId, String communityUid, BigDecimal area,BigDecimal price,
                              String toMobile,Integer buyerId) {
        try {

            User agent = new User();
            agent.setId(userId);
            House house = new House();
            house.setId(houseId);
            User user = new User();
            user.setId(buyerId);
//            Community community = new Community();
//            community.setUid(communityUid);

            HouseOrder houseOrder = new HouseOrder();
            houseOrder.setAgent(agent);
            houseOrder.setBuyer(user);
            houseOrder.setCommunity(communityUid);
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
    public Result receiveorder(Integer demandId,Integer userId,String type) {
        try {
            if(demandId == null || ("10".equals(type) && "20".equals(type))){
                return Result.error().msg(Error_code.ERROR_CODE_0023).data(new HashMap<>());
            }
            User user = new User();
            user.setId(userId);
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
    public Result mycomment(Integer userId, Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<Comment> page = commentService.pageByAgentId(userId,
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
    public Result similarHouse(Integer houseId) {
        House house = houseService.findById(houseId);
        Map<String, Object> similarHouse = new HashMap<String, Object>();
        List<House> houses = houseService.findSimilar(house.getPrice(),
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
            return Result.success().msg("").data(similarHouse);
        } else {
            return Result.success().msg("").data(similarHouse.put("list", recommendService.findByStatus("10")));
        }
    }

    /**
     * 修改头像
     */
    @RequestMapping(value = "modifyhead")
    public Result modifyhead(Integer userId, @RequestParam(required = true) MultipartFile image){
        try {
            String path = bizUploadFile.uploadUserImageToQiniu(image,userId);
            if(StringUtils.isNotBlank(path)){
                Map<String ,String> dataMap = new HashMap<>();
                dataMap.put("remotePath",path);
                User user = userService.findById(userId);
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


}

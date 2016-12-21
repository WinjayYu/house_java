package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.APIFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 登录
     * 测试ok
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(String mobile, String password) {
        try {
            if (StringUtils.isBlank(mobile) || StringUtils.isBlank(password)) {
                return Result.success().msg(Error_code.ERROR_CODE_0022).data("用户名或密码为空");
            }
            User user = userService.agentLogin(mobile, password);
            if (user == null) {
                return Result.success().msg(Error_code.ERROR_CODE_0004).data("用户名或密码错误");
            }
            AgentMaterial agentMaterial = agentMaterialService.findByAgentId(user.getId());
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("user",user);
            data.put("agentMaterial",agentMaterial);
            return Result.success().data(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001).data("服务器异常");
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
            return Result.success();
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }


    /**
     * 删除房源
     *
     * @param houseId 房源id
     */
    @RequestMapping(value = "deleteHouse", method = RequestMethod.POST)
    public Result deleteHouse(int houseId) {
        try {
            houseService.agentDeleteHouse(houseId);
            return Result.success().msg("");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 经纪人编辑房源
     *
     * @param house 房源信息
     */
    @RequestMapping(value = "modifyHouse", method = RequestMethod.POST)
    public Result modifyHouse(House house) {
        try {
            houseService.update(house);
            return Result.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 获取发布的房源列表
     * @param userId 经济人id
     */
    @RequestMapping(value = "queryMyPublishList", method = RequestMethod.POST)
    public Result queryMyPublishList(Integer userId, Integer pageNum, Integer pageSize) {
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
                return Result.error().msg(Error_code.ERROR_CODE_0014);
            }
            Map<String, Object> dataMap = APIFactory.fitting(houses);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 根据小区uid查询访问信息
     * @param communityUid 小区uid
     */
    @RequestMapping(value = "houseListByCommunityUid", method = RequestMethod.POST)
    public Result houseListByCom(String communityUid, Integer pageNum, Integer pageSize) {
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
                return Result.error().msg(Error_code.ERROR_CODE_0014);
            }
            Map<String, Object> dataMap = APIFactory.fitting(houses);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    @RequestMapping(value = "houseList", method = RequestMethod.POST)
    public Result houseList(Integer pageNum, Integer pageSize) {
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
                return Result.error().msg(Error_code.ERROR_CODE_0014);
            }
            Map<String, Object> dataMap = APIFactory.fitting(houses);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 我的订单列表
     * @param agentId 经济人id
     */
    @RequestMapping(value = "myOrderList", method = RequestMethod.POST)
    public Result myOrderList(Integer agentId, Integer pageNum, Integer pageSize) {
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
                return Result.error().msg(Error_code.ERROR_CODE_0014);
            }
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 我的买房需求列表
     * @param agentId 经济人id
     */
    @RequestMapping(value = "buyHouseList", method = RequestMethod.POST)
    public Result buyHouseList(Integer agentId, Integer pageNum, Integer pageSize) {
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
                return Result.error().msg(Error_code.ERROR_CODE_0014);
            }
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 我的卖房需求列表
     * @param agentId 经济人id
     */
    @RequestMapping(value = "sellHouseList", method = RequestMethod.POST)
    public Result sellHouseList(Integer agentId, Integer pageNum, Integer pageSize) {
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
                return Result.error().msg(Error_code.ERROR_CODE_0014);
            }
            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 全部买房需求列表
     */
    @RequestMapping(value = "allBuyHouseList", method = RequestMethod.POST)
    public Result allBuyHouseList(Integer pageNum, Integer pageSize) {
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
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 全部卖房需求列表
     */
    @RequestMapping(value = "allSellHouseList", method = RequestMethod.POST)
    public Result allSellHouseList(Integer pageNum, Integer pageSize) {
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
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 创建订单
     */
    @RequestMapping(value = "createOrder", method = RequestMethod.POST)
    public Result createOrder(Integer userId, Integer houseId, String communityUid, BigDecimal area,BigDecimal price,
                              String toMobile,Integer buyerId) {
        try {

            User agent = new User();
            agent.setId(userId);
            House house = new House();
            house.setId(houseId);
            User user = new User();
            user.setId(buyerId);
            Community community = new Community();
            community.setUid(communityUid);

            HouseOrder houseOrder = new HouseOrder();
            houseOrder.setAgent(agent);
            houseOrder.setBuyer(user);
            houseOrder.setCommunity(community);
            houseOrder.setArea(area);
            houseOrder.setPrice(price);
            houseOrder.setBuyerMobile(toMobile);

            houseService.agentPutawayHouse(houseId);
            return Result.success();
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 经济人上架房源
     * @param houseId 房源id
     */
    @RequestMapping(value = "putawayHouse", method = RequestMethod.POST)
    public Result putawayHouse(Integer houseId) {
        try {
            houseService.agentPutawayHouse(houseId);
            return Result.success();
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 下架房源
     * @param houseId 房源id
     */
    @RequestMapping(value = "soldOutHouse", method = RequestMethod.POST)
    public Result soldOutHouse(Integer houseId) {
        try {
            houseService.agentSoldOutHouse(houseId);
            return Result.success();
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 接单
     */
    @RequestMapping(value = "receiveOrder", method = RequestMethod.POST)
    public Result receiveOrder(Integer demandId,Integer userId,String type) {
        try {
            if(demandId == null || ("10".equals(type) && "20".equals(type))){
                return Result.success().msg(Error_code.ERROR_CODE_0023);
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
            return Result.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 我的评论
     */
    @RequestMapping(value = "myComment", method = RequestMethod.POST)
    public Result myComment(Integer userId, Integer pageNum, Integer pageSize) {
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
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
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

}

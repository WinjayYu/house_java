package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.entity.AgentMaterial;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.HouseService;
import com.ryel.zaja.service.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
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
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 登录
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(String mobile,String password) {
        try {
            if(StringUtils.isBlank(mobile) || StringUtils.isBlank(password)){
                return Result.success().msg(Error_code.ERROR_CODE_0022);
            }
            User user = userService.agentLogin(mobile,password);
            if(user == null){
                return Result.success().msg(Error_code.ERROR_CODE_0004);
            }
            // TODO: 2016/12/18 查询经济人扩展信息
            return Result.success().data(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success().msg(Error_code.ERROR_CODE_0001);
        }
    }

    /**
     * 注册
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Result register(User user, AgentMaterial agentMaterial,String verifyCode) {
        try {
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
            return Result.success().msg("");
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

    @RequestMapping(value = "register111", method = RequestMethod.POST)
    public Result register111(User user, @RequestParam("verCode") String verCode) {
        Object origVerCode = redisTemplate.opsForValue().get(user.getMobile());
        if (null == origVerCode) {
            return Result.error().msg(Error_code.ERROR_CODE_0010).data("");
        }
        if (!origVerCode.equals(verCode)) {
            return Result.error().msg(Error_code.ERROR_CODE_0009).data(new Object());
        }
        try {
            user.setHead("");
            user.setNickname("");
            user.setUsername("");
            if (null == user.getSex()) {
                user.setSex("30");//未设置
            }
            if (null == user.getType()) {
                user.setType("20");//用户
            }
            userService.create(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0006).data("");//手机号被占用
        }

        return Result.success().msg("").data(user2map(user));
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

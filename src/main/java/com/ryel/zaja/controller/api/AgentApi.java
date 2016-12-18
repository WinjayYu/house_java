package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.entity.AgentMaterial;
import com.ryel.zaja.entity.BuyHouse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
     * @param agentId 经济人id
     */
    @RequestMapping(value = "queryMyPublishList", method = RequestMethod.POST)
    public Result queryMyPublishList(Integer agentId, Integer pageNum, Integer pageSize) {
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
}

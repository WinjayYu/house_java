package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.entity.Comment;
import com.ryel.zaja.service.AgentBuyHouseService;
import com.ryel.zaja.service.AgentSellHouseService;
import com.ryel.zaja.service.CommentService;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by billyu on 2016/12/19.
 */
@RestController
@RequestMapping(value = "/api/comment/",produces = "application/json; charset=UTF-8")
public class CommentApi {
    protected final static Logger logger = LoggerFactory.getLogger(CommentApi.class);


    @Autowired
    private CommentService commentService;

    @Autowired
    private AgentSellHouseService agentSellHouseService;

    @Autowired
    private AgentBuyHouseService agentBuyHouseService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result create(Integer userId, Integer agentId, Integer houseOrderId, Integer star, String content){
        if(null == userId
                || null == agentId
                || null == content
                || null == star){
            return Result.error().msg(Error_code.ERROR_CODE_0002).data(new HashMap<>());
        }
        try{

            commentService.create(userId, agentId, houseOrderId, star, content);
            Comment comment = commentService.findByHouseOrderId(houseOrderId);
            Map<String, Object> map = new HashMap<>();
            map.put("comment", comment);
            return Result.success().data(map);
        }catch (BizException e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }

    }

    /**
     * 检查订单是否评价过
     * @param houseOrderId
     * @return
     */
    public Result check(Integer houseOrderId){
        try{
            Map<String, String> map = new HashMap<>();
            String flag = "";
           Comment comment = commentService.findByHouseOrderId(houseOrderId);
            if(null != comment){
                flag = "0";//已评价过
            }else{
                flag = "1";//未评价
            }
            map.put("flag", flag);
            return Result.success().data(map);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }

    }

    /**
     * 经纪人的评论列表
     * @param userId
     * @return
     */
    @RequestMapping(value = "listcomment")
    public String listComment(Integer userId, Integer pageNum, Integer pageSize){
        try{
            Page<Comment> page = commentService.findByAgentId(userId, pageNum, pageSize);
            if(0 != page.getContent().size()){
                Map<String, Object> dataMap = APIFactory.fitting(page);
                Result result = Result.success().msg("").data(dataMap);
                return JsonUtil.obj2ApiJson(result);
            }
            Result result = Result.error().msg(Error_code.ERROR_CODE_0014);
            return JsonUtil.obj2ApiJson(result);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            Result result = Result.error().msg(Error_code.ERROR_CODE_0025);
            return JsonUtil.obj2ApiJson(result,"houseOrder");
        }
    }

    @RequestMapping(value = "findonecomment", method = RequestMethod.POST)
    public Result findOneComment(Integer userId){
        try{
            Page<Comment> page = commentService.findOneComment(userId,
                    new PageRequest(0, 1, Sort.Direction.DESC, "addTime"));
            if(null != page){
//                Map<String, Object> dataMap = APIFactory.fitting(page);
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("comment", page.getContent());

                Long sellHouseCount = agentSellHouseService.count(userId);
                Long buyHouseCount  = agentBuyHouseService.count(userId);
                Long count = sellHouseCount + buyHouseCount;
                if(null == count) {
                    count = 0L;
                }

                dataMap.put("count", count);

                Double average = commentService.average(userId);
                average = average == null ?  0.0 :  1.0;
                dataMap.put("avg", average);

                return Result.success().msg("").data(dataMap);
            }else{
                return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
    }

}

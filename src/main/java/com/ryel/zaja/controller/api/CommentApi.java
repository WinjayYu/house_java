package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.entity.Comment;
import com.ryel.zaja.service.CommentService;
import com.ryel.zaja.utils.APIFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billyu on 2016/12/19.
 */
@RestController
@RequestMapping(value = "/api/comment/")
public class CommentApi {
    protected final static Logger logger = LoggerFactory.getLogger(CommentApi.class);


    @Autowired
    private CommentService commentService;

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
    public Result listComment(Integer userId, Integer pageNum, Integer pageSize){
        try{
            Page<Comment> page = commentService.findByAgentId(userId, pageNum, pageSize);
            if(null != page){
                Map<String, Object> dataMap = APIFactory.fitting(page);
                return Result.success().data(dataMap);
            }
            return Result.error().msg(Error_code.ERROR_CODE_0014).data(new HashMap<>());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
    }

}

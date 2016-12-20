package com.ryel.zaja.service;

import com.ryel.zaja.entity.Comment;
import com.ryel.zaja.entity.Recommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by billyu on 2016/12/19.
 */
public interface CommentService extends ICommonService<Comment>{

    void create(Integer userId, Integer agentId, Integer houseOrderId, Integer star, String content);

    Comment findByHouseOrderId(Integer houseOrderId);

    Page<Comment> findByAgentId(Integer agentId, Integer pageNum, Integer pageSize);
}

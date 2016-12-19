package com.ryel.zaja.service;

import com.ryel.zaja.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService extends ICommonService<Comment> {

    Page<Comment> pageByAgentId(Integer agentId, Pageable pageable);



}

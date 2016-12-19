package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.CommentDao;
import com.ryel.zaja.entity.Comment;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommentServiceImpl extends AbsCommonService<Comment> implements CommentService {

    @Autowired
    private CommentDao commentDao;


    @Override
    public JpaRepository<Comment, Integer> getDao() {
        return commentDao;
    }

    @Override
    public Page<Comment> pageByAgentId(Integer agentId, Pageable pageable) {
        return commentDao.pageByAgentId(agentId,pageable);
    }


}

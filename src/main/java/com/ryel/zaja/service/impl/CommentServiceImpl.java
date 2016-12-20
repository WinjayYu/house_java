package com.ryel.zaja.service.impl;

<<<<<<< HEAD
import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.CommentDao;
import com.ryel.zaja.dao.HouseOrderDao;
import com.ryel.zaja.dao.UserDao;
=======
import com.ryel.zaja.dao.CommentDao;
>>>>>>> origin/master
import com.ryel.zaja.entity.Comment;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
<<<<<<< HEAD
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
=======
import org.springframework.data.domain.Pageable;
>>>>>>> origin/master
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import java.util.Date;

/**
 * Created by billyu on 2016/12/19.
 */
=======
>>>>>>> origin/master
@Service
@Transactional(readOnly = true)
public class CommentServiceImpl extends AbsCommonService<Comment> implements CommentService {

    @Autowired
<<<<<<< HEAD
    CommentDao commentDao;

    @Autowired
    UserDao userDao;

    @Autowired
    HouseOrderDao houseOrderDao;

    @Transactional
    @Override
    public void create(Integer userId, Integer agentId, Integer houseOrderId, Integer star, String content) {
        try{
            Comment comment = new Comment();
            comment.setAddTime(new Date());
            comment.setUser(userDao.findOne(userId));
            comment.setAgent(userDao.findOne(agentId));
            comment.setHouseOrder(houseOrderDao.findOne(houseOrderId));
            comment.setContent(content);
            comment.setStar(star);
            commentDao.save(comment);
        }catch (BizException be){
            new BizException(Error_code.ERROR_CODE_0019);
        }
    }


    @Override
    public Comment findByHouseOrderId(Integer houseOrderId) {
        return commentDao.findByHouseOrderId(houseOrderId);
    }

    @Override
    public Page<Comment> findByAgentId(Integer agentId, Integer pageNum, Integer pageSize) {
        return commentDao.findByAgentId(agentId, new PageRequest(pageNum-1,pageSize, Sort.Direction.DESC, "addTime"));
    }

    @Override
    public JpaRepository<Comment, Integer> getDao() {
        return null;
    }
=======
    private CommentDao commentDao;


    @Override
    public JpaRepository<Comment, Integer> getDao() {
        return commentDao;
    }

    @Override
    public Page<Comment> pageByAgentId(Integer agentId, Pageable pageable) {
        return commentDao.pageByAgentId(agentId,pageable);
    }


>>>>>>> origin/master
}

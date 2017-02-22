package com.ryel.zaja.service.impl;

import com.ryel.zaja.config.enums.HouseOrderStatus;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.dao.Impl.CommentDaoImpl;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.HouseOrder;
import com.ryel.zaja.service.HouseOrderService;
import com.ryel.zaja.service.HouseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.CommentDao;
import com.ryel.zaja.dao.HouseOrderDao;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.dao.CommentDao;
import com.ryel.zaja.entity.Comment;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;

/**
 * Created by billyu on 2016/12/19.
 */

@Service
@Transactional(readOnly = true)
public class CommentServiceImpl extends AbsCommonService<Comment> implements CommentService {

    @Autowired
    CommentDao commentDao;

    @Autowired
    UserDao userDao;

    @Autowired
    HouseOrderDao houseOrderDao;

    @Autowired
    CommentDaoImpl commentDaoImpl;

    @Autowired
    HouseOrderService houseOrderService;

    @Transactional
    @Override
    public Comment create(Integer userId, Integer agentId, Integer houseOrderId, Integer star, String content) {
        try{
            Comment comment = new Comment();
            comment.setAddTime(new Date());
            comment.setUser(userDao.findOne(userId));
            comment.setAgent(userDao.findOne(agentId));
            comment.setHouseOrder(houseOrderDao.findOne(houseOrderId));
            comment.setContent(content);
            comment.setStar(star);

            HouseOrder houseOrder = comment.getHouseOrder();
            houseOrder.setStatus(HouseOrderStatus.FINISHED.getCode());
            houseOrderService.update(houseOrder);

            if(null != houseOrder.getHouse()) {
                House house = houseOrder.getHouse();
                house.setStatus(HouseStatus.CLOSED.getCode());
            }

            return commentDao.save(comment);
        }catch (BizException be){
           throw new BizException(Error_code.ERROR_CODE_0001);
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


    @Override
    public Page<Comment> findOneComment(Integer agentId, Pageable pageable) {
        return commentDao.findOneComment(agentId, pageable);
    }

    @Override
    public Page<Comment> pageByAgentId(Integer agentId, Pageable pageable) {
        return commentDao.findByAgentId(agentId,pageable);
    }


    @Override
    public Double average(Integer agentId) {
        return commentDaoImpl.avg(agentId);
    }
}

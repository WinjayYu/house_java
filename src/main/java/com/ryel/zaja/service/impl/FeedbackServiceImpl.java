package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.FeedbackDao;
import com.ryel.zaja.entity.Feedback;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by billyu on 2017/1/9.
 */
@Service
@Transactional(readOnly = true)
public class FeedbackServiceImpl extends AbsCommonService<Feedback> implements FeedbackService{

    @Autowired
    FeedbackDao feedbackDao;

    @Override
    public JpaRepository<Feedback, Integer> getDao() {
        return feedbackDao;
    }

    @Override
    @Transactional
    public Feedback create(Feedback feedback) {
        return feedbackDao.save(feedback);
    }
}

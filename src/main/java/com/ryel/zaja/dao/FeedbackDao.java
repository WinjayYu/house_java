package com.ryel.zaja.dao;

import com.ryel.zaja.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by billyu on 2017/1/9.
 */
public interface FeedbackDao extends JpaRepository<Feedback, Integer>,JpaSpecificationExecutor<Feedback> {
}

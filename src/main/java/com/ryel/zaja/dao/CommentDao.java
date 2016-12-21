package com.ryel.zaja.dao;

import com.ryel.zaja.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by billyu on 2016/12/19.
 */
@Repository
public interface CommentDao extends JpaRepository<Comment, Integer>,JpaSpecificationExecutor<Comment> {

    @Query("select c from Comment c where c.houseOrder.id = ?1")
    Comment findByHouseOrderId(Integer houseOrderId);

    @Query("select c from Comment c where c.agent.id = ?1")
    Page<Comment> findByAgentId(Integer anentId, Pageable pageable);
}

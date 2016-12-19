package com.ryel.zaja.dao;

import com.ryel.zaja.entity.Comment;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentDao extends JpaRepository<Comment, Integer>,JpaSpecificationExecutor<Comment> {
    @Query("select u from Comment u where u.agent.id = ?1")
    Page<Comment> pageByAgentId(Integer agentId, Pageable pageable);
}

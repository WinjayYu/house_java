package com.ryel.zaja.dao;

import com.ryel.zaja.entity.ImgWall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by billyu on 2017/2/15.
 */
@Repository
public interface ImgWallDao extends JpaRepository<ImgWall, Integer>,JpaSpecificationExecutor<ImgWall> {
    @Query("select i from ImgWall i where i.userId = ?1")
    List<ImgWall> findByAgentId(Integer agentId);

    @Query("select count(i.userId) from ImgWall i where i.userId = ?1")
    Long countImg(Integer agentId);

    @Query("select i from ImgWall i where i.userId = ?1 and i.img = ?2")
    ImgWall findByAgentIdAndUrl(Integer agentId, String url);
}

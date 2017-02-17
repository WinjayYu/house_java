package com.ryel.zaja.service;

import com.ryel.zaja.entity.ImgWall;

import java.util.List;

/**
 * Created by billyu on 2017/2/15.
 */
public interface ImgWallService extends ICommonService<ImgWall>{
    List<ImgWall> findByAgentId(Integer agentId);

    Long countImg(Integer agentId);
}

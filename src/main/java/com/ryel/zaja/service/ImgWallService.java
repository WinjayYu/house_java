package com.ryel.zaja.service;

import com.ryel.zaja.entity.ImgWall;

import java.util.List;

/**
 * Created by billyu on 2017/2/15.
 */
public interface ImgWallService extends ICommonService<ImgWall>{
    List<Object> findByAgentId(Integer agentId);

    Long countImg(Integer agentId);

    void deleteByUrl(String url);

    boolean findByAgentIdAndUrl(Integer agentId, String url);
}

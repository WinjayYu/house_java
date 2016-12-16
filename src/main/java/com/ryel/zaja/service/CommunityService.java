package com.ryel.zaja.service;

import com.ryel.zaja.entity.Community;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */
public interface CommunityService extends ICommonService<Community>{

    void create(Community community);

    Community update(Community community);

    Community findByUid(String uid);


    /**
     * 获取所有小区列表
     * @return
     */
    List<Community> list();

    Page<Community> findByPage(String name, final Integer type, int pageNum, int pageSize);
}

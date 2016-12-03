package com.ryel.zaja.service;

import com.ryel.zaja.entity.Community;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */
public interface CommunityService extends ICommonService<Community>{

    public Community create(Community community);

    public Community update(Community community);


    /**
     * 获取所有小区列表
     * @return
     */
    public List<Community> list();

    public Page<Community> findByPage(String name, final Integer type, int pageNum, int pageSize);
}

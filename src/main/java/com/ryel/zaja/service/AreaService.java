package com.ryel.zaja.service;

import com.ryel.zaja.entity.community;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */
public interface AreaService extends ICommonService<community>{

    public community create(community community);

    public community update(community community);


    /**
     * 获取所有小区列表
     * @return
     */
    public List<community> list();

    public Page<community> findByPage(String name, final Integer type, int pageNum, int pageSize);
}

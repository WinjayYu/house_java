package com.ryel.zaja.service;

import com.ryel.zaja.entity.Area;
import com.ryel.zaja.entity.Area;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */
@Service
public interface AreaService extends ICommonService<Area>{

    public Area create(Area area);

    public Area update(Area area);


    /**
     * 获取所有小区列表
     * @return
     */
    public List<Area> list();

    public Page<Area> findByPage(String name, final Integer type, int pageNum, int pageSize);
}

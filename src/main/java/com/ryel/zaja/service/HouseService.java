package com.ryel.zaja.service;

import com.ryel.zaja.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */
public interface HouseService extends ICommonService<House>{

    House create(House house);

    House update(House house);



    /**
     * 获取所有房源列表
     * @return
     */
    List<House> list();

}

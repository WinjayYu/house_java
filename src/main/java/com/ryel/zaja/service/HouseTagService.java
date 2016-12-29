package com.ryel.zaja.service;

import com.ryel.zaja.entity.HouseTag;

import java.util.List;

/**
 * Created by Nathan on 2016/12/29.
 */
public interface HouseTagService extends ICommonService<HouseTag> {

    void create(HouseTag tag);

    HouseTag findByTag(String tag);

    void upDateTagNum(String tag);

    List<HouseTag> list();
}

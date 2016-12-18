package com.ryel.zaja.service;

import com.ryel.zaja.entity.HomeCoverUrl;

/**
 * Created by billyu on 2016/12/7.
 */
public interface HomeCoverUrlService extends ICommonService<HomeCoverUrl>{

    HomeCoverUrl find(int id);
}

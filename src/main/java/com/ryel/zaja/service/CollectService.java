package com.ryel.zaja.service;

import com.ryel.zaja.entity.Collect;
import com.ryel.zaja.entity.Recommend;
import org.springframework.data.domain.Page;

/**
 * Created by billyu on 2016/12/14.
 */
public interface CollectService extends ICommonService<Collect>{

    Collect create(Integer userid, Integer houseId);

    Collect cancelCollect(Integer userId, Integer houseId);

    Integer countByHouseId(Integer id);

    boolean check(Integer userid, Integer houseId);

    Page<Collect> pageByUserId(Integer userId, Integer pageNum, Integer pageSize);

}

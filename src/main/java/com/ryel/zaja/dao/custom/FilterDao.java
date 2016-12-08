package com.ryel.zaja.dao.custom;

import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.vo.FilterVo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by billyu on 2016/12/6.
 */
@Repository
public interface FilterDao {
    List<House> findByFilter(FilterVo filterVo);
}

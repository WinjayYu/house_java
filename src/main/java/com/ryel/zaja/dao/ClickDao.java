package com.ryel.zaja.dao;

import com.ryel.zaja.entity.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by billyu on 2016/12/7.
 */
@Repository
public interface ClickDao extends JpaRepository<Click, Integer>,JpaSpecificationExecutor<Click> {

    Click findByHouseId(Integer id);
}

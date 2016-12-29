package com.ryel.zaja.dao;

import com.ryel.zaja.entity.HouseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nathan on 2016/12/29.
 */
@Repository
public interface HouseTagDao extends JpaRepository<HouseTag, Integer>,JpaSpecificationExecutor<HouseTag> {

    HouseTag findByTag(String tag);

}

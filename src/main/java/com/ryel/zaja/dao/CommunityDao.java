package com.ryel.zaja.dao;

import com.ryel.zaja.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by burgl on 2016/8/16.
 */
@Repository
public interface CommunityDao extends JpaRepository<Community, Integer> ,JpaSpecificationExecutor<Community> {

    List<Community> findByName(String name);

    List<Community> findByAddress(String address);

    List<Community> findByCityname(String cityname);

    Community findByUid(String uid);

    Page<Community> findByUid(String uid, Pageable pageable);
}

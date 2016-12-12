package com.ryel.zaja.dao;

import com.ryel.zaja.entity.HomeCoverUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by billyu on 2016/12/10.
 */
@Repository
public interface HomeCoverUrlDao extends JpaRepository<HomeCoverUrl, Integer> ,JpaSpecificationExecutor<HomeCoverUrl> {

}


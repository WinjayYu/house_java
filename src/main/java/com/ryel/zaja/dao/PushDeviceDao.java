package com.ryel.zaja.dao;

import com.ryel.zaja.entity.PushDevice;
import com.ryel.zaja.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Nathan on 2017/1/4.
 */
@Repository
public interface PushDeviceDao extends JpaRepository<PushDevice, Integer>,JpaSpecificationExecutor<PushDevice> {

    @Query("select a from PushDevice a where a.user = ?1")
    PushDevice findByUser(User user);

}

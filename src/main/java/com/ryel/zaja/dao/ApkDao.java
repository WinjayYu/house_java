package com.ryel.zaja.dao;

import com.ryel.zaja.entity.Apk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by billyu on 2017/1/12.
 */
@Repository
public interface ApkDao extends JpaRepository<Apk, Integer>,JpaSpecificationExecutor<Apk> {

    Apk findByVersionAndType(String version, String type);

    @Query("select a from Apk a where a.addTime = (select MAX(a.addTime) from Apk a) and a.type = ?1")
    Apk findLatestVersion(String type);

}

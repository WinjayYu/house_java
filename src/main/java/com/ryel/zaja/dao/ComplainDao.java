package com.ryel.zaja.dao;

import com.ryel.zaja.entity.Complain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by billyu on 2017/1/9.
 */
@Repository
public interface ComplainDao extends JpaRepository<Complain, Integer>,JpaSpecificationExecutor<Complain> {


}

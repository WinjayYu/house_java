package com.ryel.zaja.dao;

import com.ryel.zaja.entity.ZjjzCnapsBankinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by billyu on 2017/2/8.
 */
public interface ZjjzCnapsBankinfoDao extends JpaRepository<ZjjzCnapsBankinfo, Integer>,JpaSpecificationExecutor<ZjjzCnapsBankinfo> {


    List<ZjjzCnapsBankinfo> findByBankclscodeAndCitycode(String bankclscode, String citycode);
}

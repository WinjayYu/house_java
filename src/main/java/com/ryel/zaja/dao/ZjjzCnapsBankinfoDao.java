package com.ryel.zaja.dao;

import com.ryel.zaja.entity.ZjjzCnapsBankinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by billyu on 2017/2/8.
 */
public interface ZjjzCnapsBankinfoDao extends JpaRepository<ZjjzCnapsBankinfo, Integer>,JpaSpecificationExecutor<ZjjzCnapsBankinfo> {

    @Query(value = "select z.bankno as bankno, z.bankname as banknamw from zjjz_cnaps_bankinfo z where z.bankclscode=?1 and z.citycode=?2", nativeQuery = true)
    List<Object> findByBankclscodeAndCitycode(String bankclscode, String citycode);

    @Query(value = "select z.bankno as bankno, z.bankname as banknamw from zjjz_cnaps_bankinfo z where z.bankclscode=?1 and z.citycode=?2 and z.bankname like CONCAT('%',?3, '%')",nativeQuery = true)
    List<Object> findByBankclscodeAndCitycodeAndBankname(String bankclscode, String citycode, String bankname);
}

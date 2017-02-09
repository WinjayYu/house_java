package com.ryel.zaja.dao;

import com.ryel.zaja.entity.ZjjzCnapsBankinfo;
import com.ryel.zaja.entity.vo.ZjjzCnapsBankinfoVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by billyu on 2017/2/8.
 */
public interface ZjjzCnapsBankinfoDao extends JpaRepository<ZjjzCnapsBankinfo, Integer>,JpaSpecificationExecutor<ZjjzCnapsBankinfo> {

    @Query("select new com.ryel.zaja.entity.vo.ZjjzCnapsBankinfoVo(z.bankno,z.bankname) from ZjjzCnapsBankinfo z where z.bankclscode=?1 and z.citycode=?2")
    List<com.ryel.zaja.entity.vo.ZjjzCnapsBankinfoVo> findByBankclscodeAndCitycode(String bankclscode, String citycode);

    @Query("select new com.ryel.zaja.entity.vo.ZjjzCnapsBankinfoVo(z.bankno,z.bankname) from ZjjzCnapsBankinfo z where z.bankclscode=?1 and z.citycode=?2 and z.bankname like CONCAT('%',?3, '%')")
    List<com.ryel.zaja.entity.vo.ZjjzCnapsBankinfoVo> findByBankclscodeAndCitycodeAndBankname(String bankclscode, String citycode, String bankname);
}

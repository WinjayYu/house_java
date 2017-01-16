package com.ryel.zaja.dao;

import com.ryel.zaja.entity.SuperBankInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuperBankInfoDao extends JpaRepository<SuperBankInfo, Integer> ,JpaSpecificationExecutor<SuperBankInfo> {

}

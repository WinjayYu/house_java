package com.ryel.zaja.service.impl;

import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.HouseService;
import com.ryel.zaja.service.ICommonService;
import com.ryel.zaja.utils.ClassUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */
@Service
@Transactional(readOnly = true)
public class HouseServiceImpl extends AbsCommonService<House> implements HouseService{

    @Autowired
    private HouseDao houseDao;

    @Override
    public House create(House house) {
        return null;
    }

    @Override
    public List<House> list() {
        return null;
    }

    @Override
    @Transactional
    public void agentDeleteHouse(int houseId) {
        House house = this.findById(houseId);
        if(house == null){
            throw new BizException("房源信息已经不存在");
        }
        this.deleteById(houseId);
    }

    @Override
    public Page<House> pageByAgentId(int agentId, Pageable pageable) {
        return houseDao.pageByAgentId(agentId,pageable);
    }

    @Override
    @Transactional
    public House update(House house) {
        House origUser = findById(house.getId());
        ClassUtil.copyProperties(origUser, house);
        return save(origUser);
    }

    @Override
    public List<House> findByCommunityUid(String uid) {
        return houseDao.findByCommunityUid(uid);
    }

    @Override
    public Page<House> findByUid(String uid, Pageable pageable) {
        return houseDao.findByUid(uid, pageable);
    }

    @Override
    public List<House> findByCityname(String cityname) {
        return houseDao.findByCityname(cityname);
    }

    @Override
    public List<House> findByHouseType(String houseType) {
        return houseDao.findByHouseType(houseType);
    }

    @Override
    public List<House> findByCommumityAndAreaAndFitmentLevel(String uid, BigDecimal area, String fitmentlevel) {
        return houseDao.findByCommumityAndAreaAndFitmentLevel(uid, area, fitmentlevel);
    }

    @Override
    public Page<House> filter(int pageNum,
                              int pageSize,
                              final String sellPrice,
                              final String area,
                              final String houseType,
                              final String fitmentLevel,
                              final String floor) {
        Page<House> page = houseDao.findAll(new Specification<House>() {
            @Override
            public Predicate toPredicate(Root<House> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                Predicate result = null;
                if (null != sellPrice) {
                    String[] arr = sellPrice.split("\\|");
                    if(1 == arr.length){
                        BigDecimal bg = new BigDecimal(arr[0]);
                        //double d = Double.parseDouble(arr[0]);
                        Predicate predicate = cb.lessThan(root.get("sellPrice").as(BigDecimal.class), bg);
                        predicateList.add(predicate);
                    }else if(!StringUtils.isNotBlank(arr[0])){
                        BigDecimal bg = new BigDecimal(arr[1]);
                        Predicate predicate = cb.greaterThan(root.get("sellPrice").as(BigDecimal.class), bg);
                        predicateList.add(predicate);
                    }else{
                        BigDecimal bg1 = new BigDecimal(arr[0]);
                        BigDecimal bg2 = new BigDecimal(arr[1]);
                        Predicate predicate = cb.between(root.get("sellPrice").as(BigDecimal.class), bg1, bg2);
                        predicateList.add(predicate);
                    }
                }

                if (null != area) {
                    String[] arr = area.split("\\|");
                    if(1 == arr.length){
                        BigDecimal bg = new BigDecimal(arr[0]);
                        //double d = Double.parseDouble(arr[0]);
                        Predicate predicate = cb.lessThan(root.get("area").as(BigDecimal.class), bg);
                        predicateList.add(predicate);
                    }else if(!StringUtils.isNotBlank(arr[0])){
                        BigDecimal bg = new BigDecimal(arr[1]);
                        Predicate predicate = cb.greaterThan(root.get("area").as(BigDecimal.class), bg);
                        predicateList.add(predicate);
                    }else{
                        BigDecimal bg1 = new BigDecimal(arr[0]);
                        BigDecimal bg2 = new BigDecimal(arr[1]);
                        Predicate predicate = cb.between(root.get("area").as(BigDecimal.class), bg1, bg2);
                        predicateList.add(predicate);
                    }
                }



                if (null != houseType) {
                    Predicate predicate = cb.equal(root.get("houseType").as(String.class), houseType);
                    predicateList.add(predicate);
                }


                if (null != fitmentLevel) {
                    Predicate predicate = cb.equal(root.get("fitmentLevel").as(String.class), fitmentLevel);
                    predicateList.add(predicate);
                }

                if (null != floor) {
                    Predicate predicate = cb.equal(root.get("floor").as(String.class), floor);
                    predicateList.add(predicate);
                }

                if (predicateList.size() > 0) {
                    result = cb.and(predicateList.toArray(new Predicate[]{}));
                }
                if (result != null) {
                    query.where(result);
                }
                return query.getRestriction();
            }
        }, new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));

        return page;
    }


    @Override
    public JpaRepository<House, Integer> getDao() {
        return null;
    }
}

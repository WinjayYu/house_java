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

    /*@Override
    public Page<House> filter(int pageNum,
                              int pageSize,
                              final String sellPrice,
                              final String area,
                              final String type,
                              final String decoration,
                              final String floor) {
        Page<House> page = houseDao.findAll(new Specification<House>() {
            @Override
            public Predicate toPredicate(Root<House> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                Predicate result = null;
                if (null != sellPrice) {
                    Predicate predicate = cb.between(root.get("user").get("name").as(String.class), "%"+name+"%");
                    predicateList.add(predicate);
                }
                if (forumId != null) {
                    Predicate predicate = cb.equal(root.get("forumId").as(Integer.class), forumId);
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
        }, new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "inboundDate"));

        return page;
    }*/


    @Override
    public JpaRepository<House, Integer> getDao() {
        return null;
    }
}

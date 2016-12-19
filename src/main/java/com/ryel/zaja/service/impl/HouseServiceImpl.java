package com.ryel.zaja.service.impl;

import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.enums.HouseStatus;
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
public class HouseServiceImpl extends AbsCommonService<House> implements HouseService {

    @Autowired
    private HouseDao houseDao;

//    private final static String MANAGER = "10";
//    private final static String USER = "20";
//    private final static String AGENT = "30";

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
        if (house == null) {
            throw new BizException("房源信息已经不存在");
        }
        this.deleteById(houseId);
    }

    @Override
    public Page<House> pageByAgentId(int agentId, Pageable pageable) {
        return houseDao.pageByAgentId(agentId, pageable);
    }

    @Override
    @Transactional
    public void agentPutawayHouse(Integer houseId) {
        House house = getHouseByCheck(houseId);
        if(!HouseStatus.SOLD_OUT_YET.getCode().equals(house.getStatus())){
            throw new BizException(Error_code.ERROR_CODE_0025);
        }
        house.setStatus(HouseStatus.PUTAWAY_YET.getCode());
        update(house);
    }

    @Override
    @Transactional
    public void agentSoldOutHouse(Integer houseId) {
        House house = getHouseByCheck(houseId);
        if(!HouseStatus.PUTAWAY_YET.getCode().equals(house.getStatus())){
            throw new BizException(Error_code.ERROR_CODE_0025);
        }
        house.setStatus(HouseStatus.SOLD_OUT_YET.getCode());
        update(house);
    }

    @Override
    @Transactional
    public House getHouseByCheck(Integer houseId) {
        House house = this.findById(houseId);
        if(house == null){
            throw new BizException(Error_code.ERROR_CODE_0025);
        }
        return house;
    }

    @Override
    @Transactional
    public House update(House house) {
        House origUser = findById(house.getId());
        ClassUtil.copyProperties(origUser, house);
        return save(origUser);
    }

    @Override
    public List<House> findByCommunityUid(String uid, String type) {
        List<String> list = new ArrayList<>();
        if (UserType.AGENT.getType().equals(type)) {
            list.add("10");
            list.add("50");
            return houseDao.findByCommunityUid(uid, list);
        }else if(UserType.USER.getType().equals(type)){
            list.add("10");
            return houseDao.findByCommunityUid(uid, list);
        }else{
            list.add("10");
            list.add("20");
            list.add("30");
            list.add("40");
            list.add("50");
            list.add("60");
            return houseDao.findByCommunityUid(uid, list);
        }
    }

    @Override
    public Page<House> findByUid(String uid,String type, Pageable pageable) {
        List<String> list = new ArrayList<>();
        if (UserType.AGENT.getType().equals(type)) {
            list.add("10");
            list.add("50");
            return houseDao.findByUid(uid, list, pageable);
        }else if(UserType.USER.getType().equals(type)){
            list.add("10");
            return houseDao.findByUid(uid, list, pageable);
        }else{
            list.add("10");
            list.add("20");
            list.add("30");
            list.add("40");
            list.add("50");
            list.add("60");
            return houseDao.findByUid(uid, list, pageable);
        }
    }

    @Override
    public List<House> findByCity(String city) {
        return houseDao.findByCity(city);
    }

    public List<House> findByLayout(String houseType, String type) {
        List<String> list = new ArrayList<>();
        if (UserType.AGENT.getType().equals(type)) {
            list.add("10");
            list.add("50");
            return houseDao.findByHouseLayout(houseType, list);
        }else if(UserType.USER.getType().equals(type)){
            list.add("10");
            return houseDao.findByHouseLayout(houseType, list);
        }else{
            list.add("10");
            list.add("20");
            list.add("30");
            list.add("40");
            list.add("50");
            list.add("60");
            return houseDao.findByHouseLayout(houseType, list);
        }
    }

    @Override
    public List<House> findByCommumityAndAreaAndRenovation(String uid, BigDecimal area, String fitmentlevel) {
        return houseDao.findByCommumityAndAreaAndRenovation(uid, area, fitmentlevel);
    }

    @Override
    public Page<House> findByCommunities(List<String> uids, Pageable pageable) {
        return houseDao.findByCommunities(uids, pageable);
    }

    @Override
    public Page<House> findByAddTime(Pageable pageable) {
        return houseDao.findByAddTime(pageable);
    }

    @Override
    public List<House> findByCommunities(List<String> uids) {
        return null;
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
                    if (1 == arr.length) {
                        BigDecimal bg = new BigDecimal(arr[0]);
                        //double d = Double.parseDouble(arr[0]);
                        Predicate predicate = cb.lessThan(root.get("sellPrice").as(BigDecimal.class), bg);
                        predicateList.add(predicate);
                    } else if (!StringUtils.isNotBlank(arr[0])) {
                        BigDecimal bg = new BigDecimal(arr[1]);
                        Predicate predicate = cb.greaterThan(root.get("sellPrice").as(BigDecimal.class), bg);
                        predicateList.add(predicate);
                    } else {
                        BigDecimal bg1 = new BigDecimal(arr[0]);
                        BigDecimal bg2 = new BigDecimal(arr[1]);
                        Predicate predicate = cb.between(root.get("sellPrice").as(BigDecimal.class), bg1, bg2);
                        predicateList.add(predicate);
                    }
                }

                if (null != area) {
                    String[] arr = area.split("\\|");
                    if (1 == arr.length) {
                        BigDecimal bg = new BigDecimal(arr[0]);
                        //double d = Double.parseDouble(arr[0]);
                        Predicate predicate = cb.lessThan(root.get("area").as(BigDecimal.class), bg);
                        predicateList.add(predicate);
                    } else if (!StringUtils.isNotBlank(arr[0])) {
                        BigDecimal bg = new BigDecimal(arr[1]);
                        Predicate predicate = cb.greaterThan(root.get("area").as(BigDecimal.class), bg);
                        predicateList.add(predicate);
                    } else {
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
        return houseDao;
    }
}

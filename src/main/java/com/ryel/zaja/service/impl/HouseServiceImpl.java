package com.ryel.zaja.service.impl;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.HouseType;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.ClassUtil;
import com.ryel.zaja.utils.GetDistanceUtil;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by billyu on 2016/11/28.
 */
@Service
@Transactional(readOnly = true)
public class HouseServiceImpl extends AbsCommonService<House> implements HouseService {

    @Autowired
    private HouseDao houseDao;

    @Autowired
    private CommunityService communityService;

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    SellHouseService sellHouseService;

    @Override
    @Transactional
    public House create(House house) {

        //经纪人从已有的sellHouse编辑房源，则sellHouse的house_num加1
       /* if(null != house.getSellHouse()){
            SellHouse sellHouse = house.getSellHouse();
            sellHouse.setHouseNum(sellHouse.getHouseNum() + 1);
            sellHouseService.save(sellHouse);
        }*/
        return houseDao.save(house);
    }

    @Override
    public List<House> list() {
        return null;
    }

    @Override
    @Transactional
    public void agentDeleteHouse(Integer houseId, Integer agentId) {
        House house = this.findById(houseId);
        if (house == null) {
            throw new BizException("房源信息已经不存在");
        }
        if (!agentId.equals(house.getAgent().getId()))
        {
            throw new BizException(Error_code.ERROR_CODE_0035);
        }
        house.setStatus(HouseStatus.DELETE.getCode());
        update(house);
    }

    @Override
    public Page<House> pageByAgentId(Integer agentId, Pageable pageable) {
        return houseDao.pageByAgentId(agentId,HouseStatus.getManagerCanSeeStatus(), pageable);
    }

    @Override
    public Page<House> pageByCommunityUid(String uid,List<String> status, Pageable pageable) {
        return houseDao.pageByCommunityUid(uid,status, pageable);
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
        if (UserType.AGENT.getCode().equals(type)) {
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            list.add(HouseStatus.IN_CONNECT.getCode());
            return houseDao.findByCommunityUid(uid, list);
        }else if(UserType.USER.getCode().equals(type)){
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            return houseDao.findByCommunityUid(uid, list);
        }else{
            list.add(HouseStatus.SAVED.getCode());
            list.add(HouseStatus.REJECT.getCode());
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            list.add(HouseStatus.SOLD_OUT_YET.getCode());
            list.add(HouseStatus.IN_CONNECT.getCode());
            list.add(HouseStatus.CLOSED.getCode());
            return houseDao.findByCommunityUid(uid, list);
        }
    }

    @Override
    public List<House> findByAgentIdAndSellHouseId(Integer agentId, Integer sellHouseId) {
        return houseDao.findByAgentIdAndSellHouseId(agentId,sellHouseId);
    }

    @Override
    public Page<House> findByUid(String uid,String type, Pageable pageable) {
        List<String> list = new ArrayList<>();
        if (UserType.AGENT.getCode().equals(type)) {
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            list.add(HouseStatus.IN_CONNECT.getCode());
            return houseDao.findByUid(uid, list, pageable);
        }else if(UserType.USER.getCode().equals(type)){
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            return houseDao.findByUid(uid, list, pageable);
        }else{
            list.add(HouseStatus.SAVED.getCode());
            list.add(HouseStatus.REJECT.getCode());
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            list.add(HouseStatus.SOLD_OUT_YET.getCode());
            list.add(HouseStatus.IN_CONNECT.getCode());
            list.add(HouseStatus.CLOSED.getCode());
            return houseDao.findByUid(uid, list, pageable);
        }
    }

    @Override
    public Page<House> findByUids(List<String> uids,String type, Pageable pageable) {
        List<String> list = new ArrayList<>();
        if (UserType.AGENT.getCode().equals(type)) {
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            list.add(HouseStatus.IN_CONNECT.getCode());
            return houseDao.findByUids(uids, list, pageable);
        }else if(UserType.USER.getCode().equals(type)){
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            return houseDao.findByUids(uids, list, pageable);
        }else{
            list.add(HouseStatus.SAVED.getCode());
            list.add(HouseStatus.REJECT.getCode());
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            list.add(HouseStatus.SOLD_OUT_YET.getCode());
            list.add(HouseStatus.IN_CONNECT.getCode());
            list.add(HouseStatus.CLOSED.getCode());
            return houseDao.findByUids(uids, list, pageable);
        }
    }


    @Override
    public List<House> findByCity(String city) {
        return houseDao.findByCity(city);
    }

    public List<House> findByLayout(String houseType, String type) {
        List<String> list = new ArrayList<>();
        if (UserType.AGENT.getCode().equals(type)) {
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            list.add(HouseStatus.IN_CONNECT.getCode());
            return houseDao.findByHouseLayout(houseType, list);
        }else if(UserType.USER.getCode().equals(type)){
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            return houseDao.findByHouseLayout(houseType, list);
        }else{
            list.add(HouseStatus.SAVED.getCode());
            list.add(HouseStatus.REJECT.getCode());
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            list.add(HouseStatus.SOLD_OUT_YET.getCode());
            list.add(HouseStatus.IN_CONNECT.getCode());
            list.add(HouseStatus.CLOSED.getCode());
            return houseDao.findByHouseLayout(houseType, list);
        }
    }

    @Override
    public List<House> findSimilar(BigDecimal price, String uid, BigDecimal area, String renovation) {
        return houseDao.findSimilar(price, uid, area, renovation,HouseStatus.getUserCanSeeStatus());
    }

    @Override
    public List<House> agentFindSimilar(BigDecimal price, String uid, BigDecimal area, String renovation) {
        return houseDao.agentFindSimilar(price, uid, area, renovation,HouseStatus.getAgentCanSeeStatus());
    }


    @Override
    public Page<House> findByAddTime(String type, Pageable pageable) {
        List<String> list = new ArrayList<>();
        if (UserType.AGENT.getCode().equals(type)) {
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            list.add(HouseStatus.IN_CONNECT.getCode());
            return houseDao.findByAddTime(list, pageable);
        }else if(UserType.USER.getCode().equals(type)){
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            return houseDao.findByAddTime(list, pageable);
        }else{
            list.add(HouseStatus.SAVED.getCode());
            list.add(HouseStatus.REJECT.getCode());
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            list.add(HouseStatus.SOLD_OUT_YET.getCode());
            list.add(HouseStatus.IN_CONNECT.getCode());
            list.add(HouseStatus.CLOSED.getCode());
            return houseDao.findByAddTime(list, pageable);
        }

    }

    @Override
    public Page<House> findByCommunitiesStatus(List<String> status, List<String> uidList, Pageable pageable) {
        return houseDao.findByCommunitiesStatus(status, uidList, pageable);
    }


    @Override
    public Page<House> findBySellHouse(Integer userId, Pageable pageable) {
        return houseDao.findBySellHouse(userId,pageable);
    }

    @Override
    public Page<House> filter(int pageNum,
                              int pageSize,
                              final String price,
                              final String area,
                              final String layout,
                              final String renovation,
                              final String floor,
                              final String userType) {
        Page<House> page = houseDao.findAll(new Specification<House>() {
            @Override
            public Predicate toPredicate(Root<House> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                Predicate result = null;
                if (null != price) {
                    String[] arr = price.split("\\|");
                    if (1 == arr.length) {
                        BigDecimal bg = new BigDecimal(arr[0]);
                        //double d = Double.parseDouble(arr[0]);
                        Predicate predicate = cb.lessThan(root.get("price").as(BigDecimal.class), bg);
                        predicateList.add(predicate);
                    } else if (!StringUtils.isNotBlank(arr[0])) {
                        BigDecimal bg = new BigDecimal(arr[1]);
                        Predicate predicate = cb.greaterThan(root.get("price").as(BigDecimal.class), bg);
                        predicateList.add(predicate);
                    } else {
                        BigDecimal bg1 = new BigDecimal(arr[0]);
                        BigDecimal bg2 = new BigDecimal(arr[1]);
                        Predicate predicate = cb.between(root.get("price").as(BigDecimal.class), bg1, bg2);
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


                if (null != layout) {
                    Predicate predicate = cb.equal(root.get("layout").as(String.class), layout);
                    predicateList.add(predicate);
                }


                if (null != renovation) {
                    Predicate predicate = cb.equal(root.get("renovation").as(String.class), renovation);
                    predicateList.add(predicate);
                }

                if (null != floor) {
                    Predicate predicate = cb.equal(root.get("floor").as(String.class), floor);
                    predicateList.add(predicate);
                }


                 //  如果是用户则只筛选上架的房源，如果是经纪人则筛选上架和交接中的房源
               if(UserType.USER.getCode().equals(userType)) {
                    Predicate p = cb.equal(root.get("status").as(String.class), HouseStatus.PUTAWAY_YET.getCode());
                    predicateList.add(p);
                }else{
                    List<String> list = new ArrayList<>();
                    list.add(HouseStatus.PUTAWAY_YET.getCode());
                    list.add(HouseStatus.IN_CONNECT.getCode());

                    Expression<String> exp = root.get("status").as(String.class);
                    predicateList.add(exp.in(list));

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
    public Page<House> mgtPageHouse(int pageNum, int pageSize) {
        Page<House> page = houseDao.findAll(new Specification<House>() {
            @Override
            public Predicate toPredicate(Root<House> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                Predicate result = null;

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
    public House findById(Integer id) {
        return houseDao.findOne(id);
    }

    @Override
    public JpaRepository<House, Integer> getDao() {
        return houseDao;
    }

    //用户端查看经纪人发布的房源
    @Override
    public Page<House> pageByAgentId2(int agentId, Pageable pageable) {
        return houseDao.pageByAgentId2(agentId, HouseStatus.PUTAWAY_YET.getCode(), pageable);
    }

    @Override
    public Long count(Integer agentId) {
        return houseDao.countByAgentId(agentId);
    }
}

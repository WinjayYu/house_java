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
import com.ryel.zaja.utils.APIFactory;
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
    EntityManagerFactory emf;

    @Override
    @Transactional
    public House create(House house) {
        return houseDao.save(house);
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
    public Page<House> pageByCommunityUid(String uid,List<String> status, Pageable pageable) {
        return houseDao.pageByCommunityUid(uid,status, pageable);
    }
    @Override
    public Map<String, Object> agentPage(Integer pageNum, Integer pageSize,BigDecimal longitude,BigDecimal latitude,String cityName) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> status = new ArrayList<String>();
        status.add(HouseStatus.PUTAWAY_YET.getCode());
        status.add(HouseStatus.IN_CONNECT.getCode());
        Page<House> page;
        if(StringUtils.isNotBlank(cityName)){
            page = houseDao.agentPage(status, cityName, new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
        }else {
            page = houseDao.agentPage(status, new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
        }

        if(longitude != null && latitude != null){
            EntityManager em = emf.createEntityManager();
            //定义SQL
            String sql = "SELECT";
            sql += "(SELECT ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-c.latitude*PI()/180)/2),2)+COS(?*PI()/180)*COS(c.latitude*PI()/180)*POW(SIN((?*PI()/180-c.longitude*PI()/180)/2),2))))) AS distance";
            sql += ",h.* FROM house h INNER JOIN community c";
            sql += " WHERE h.community_uid = c.uid AND c.latitude IS NOT NULL AND c.longitude IS NOT NULL";
            sql += " and (h.`status` = ? or h.`status` = ?) ";
            if(StringUtils.isNotBlank(cityName)){
                sql += " and h.city = ?";
            }
            sql += " ORDER BY distance ASC LIMIT ?,?";
            Query query;
            if(StringUtils.isNotBlank(cityName)){
                query =  em.createNativeQuery(sql,House.class).setParameter(1,latitude).setParameter(2,latitude).setParameter(3,longitude)
                        .setParameter(4,HouseStatus.PUTAWAY_YET.getCode()).setParameter(5,HouseStatus.IN_CONNECT.getCode())
                        .setParameter(6,cityName)
                        .setParameter(7,pageNum - 1).setParameter(8,pageSize);
            }else {
                query =  em.createNativeQuery(sql,House.class).setParameter(1,latitude).setParameter(2,latitude).setParameter(3,longitude)
                        .setParameter(4,HouseStatus.PUTAWAY_YET.getCode()).setParameter(5,HouseStatus.IN_CONNECT.getCode())
                        .setParameter(6,pageNum - 1).setParameter(7,pageSize);
            }

            List<House> houseList = query.getResultList();
            em.close();
            dataMap = APIFactory.fitting(page,houseList);
        }else {
            dataMap = APIFactory.fitting(page);
        }

        return dataMap;
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
            list.add(HouseStatus.ENABLED.getCode());
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
            list.add(HouseStatus.ENABLED.getCode());
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            list.add(HouseStatus.SOLD_OUT_YET.getCode());
            list.add(HouseStatus.IN_CONNECT.getCode());
            list.add(HouseStatus.CLOSED.getCode());
            return houseDao.findByUid(uid, list, pageable);
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
            list.add(HouseStatus.ENABLED.getCode());
            list.add(HouseStatus.PUTAWAY_YET.getCode());
            list.add(HouseStatus.SOLD_OUT_YET.getCode());
            list.add(HouseStatus.IN_CONNECT.getCode());
            list.add(HouseStatus.CLOSED.getCode());
            return houseDao.findByHouseLayout(houseType, list);
        }
    }

    @Override
    public List<House> findSimilar(BigDecimal price, String uid, BigDecimal area, String renovation) {
        return houseDao.findSimilar(price, uid, area, renovation);
    }

    @Override
    public List<House> agentFindSimilar(BigDecimal price, String uid, BigDecimal area, String renovation) {
        return houseDao.agentFindSimilar(price, uid, area, renovation,HouseStatus.getAgentCanSeeStatus());
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
    public House findById(Integer id) {
        return houseDao.findOne(id);
    }

    @Override
    public JpaRepository<House, Integer> getDao() {
        return houseDao;
    }
}

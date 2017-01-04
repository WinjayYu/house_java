package com.ryel.zaja.service.impl;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.HouseType;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.ClassUtil;
import com.ryel.zaja.utils.GetDistanceUtil;
import com.ryel.zaja.utils.MapSortUtils;
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
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;

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

    @Autowired
    BuyHouseService  buyHouseService;

    @Autowired
    RecommendService recommendService;

    @Autowired
    CollectService collectService;

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
        if (UserType.AGENT.getCode().equals(type)) {
            return houseDao.findByCommunityUid(uid, HouseStatus.getAgentCanSeeStatus());
        }else if(UserType.USER.getCode().equals(type)){
            return houseDao.findByCommunityUid(uid, HouseStatus.getUserCanSeeStatus());
        }else{

            return houseDao.findByCommunityUid(uid, HouseStatus.getManagerCanSeeStatus());
        }
    }

    @Override
    public List<House> findByAgentIdAndSellHouseId(Integer agentId, Integer sellHouseId) {
        return houseDao.findByAgentIdAndSellHouseId(agentId,sellHouseId);
    }

    @Override
    public Page<House> findByUid(String uid,String type, Pageable pageable) {
        if (UserType.AGENT.getCode().equals(type)) {
            return houseDao.findByUid(uid, HouseStatus.getAgentCanSeeStatus(), pageable);
        }else if(UserType.USER.getCode().equals(type)){
            return houseDao.findByUid(uid, HouseStatus.getUserCanSeeStatus(), pageable);
        }else{
            return houseDao.findByUid(uid, HouseStatus.getManagerCanSeeStatus(), pageable);
        }
    }

    @Override
    public Page<House> findByUids(List<String> uids,String type, Pageable pageable) {
        if (UserType.AGENT.getCode().equals(type)) {
            return houseDao.findByUids(uids, HouseStatus.getAgentCanSeeStatus(), pageable);
        }else if(UserType.USER.getCode().equals(type)){
            return houseDao.findByUids(uids, HouseStatus.getUserCanSeeStatus(), pageable);
        }else{

            return houseDao.findByUids(uids, HouseStatus.getManagerCanSeeStatus(), pageable);
        }
    }


    public List<House> findByLayout(String houseType, String type) {
        if (UserType.AGENT.getCode().equals(type)) {
            return houseDao.findByHouseLayout(houseType, HouseStatus.getAgentCanSeeStatus());
        }else if(UserType.USER.getCode().equals(type)){
            return houseDao.findByHouseLayout(houseType, HouseStatus.getUserCanSeeStatus());
        }else{
            return houseDao.findByHouseLayout(houseType, HouseStatus.getManagerCanSeeStatus());
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
        if (UserType.AGENT.getCode().equals(type)) {
            return houseDao.findByAddTime(HouseStatus.getAgentCanSeeStatus(), pageable);
        }else if(UserType.USER.getCode().equals(type)){
            return houseDao.findByAddTime(HouseStatus.getUserCanSeeStatus(), pageable);
        }else{
            return houseDao.findByAddTime(HouseStatus.getManagerCanSeeStatus(), pageable);
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
    public Page<House> mgtPageHouse(int pageNum, int pageSize,final String title,final String status) {
        Page<House> page = houseDao.findAll(new Specification<House>() {
            @Override
            public Predicate toPredicate(Root<House> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                Predicate result = null;
                if (StringUtils.isNotBlank(title)) {
                    Predicate predicate = cb.like(root.get("title").as(String.class), "%" + title + "%");
                    predicateList.add(predicate);
                }
                if (StringUtils.isNotBlank(status)) {
                    Predicate predicate = cb.equal(root.get("status").as(String.class), status);
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
        if(!CollectionUtils.isEmpty(page.getContent())){
            for(House house : page.getContent()){
                house.setStatusDesc(HouseStatus.getDescByCode(house.getStatus()));
            }
        }
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

    //home界面的zaja推荐房源
    @Override
    public List<House> recommend(int userId, String type) {
        List<House> result = new ArrayList<>();
        List<BuyHouse> buyHouses = new ArrayList<>();
        if (UserType.USER.getCode().equals(type)) {
            buyHouses = buyHouseService.findByUserId(userId);
        }
        if (buyHouses.isEmpty()) {
            return recommendService.findByStatus("10");
        } else {
            //随机获取一条数据
            BuyHouse bh = buyHouses.get(new Random().nextInt(buyHouses.size()));

            List<House> houses = new ArrayList<House>();

            //加上小区一样的房源
            /*if (-1 != bh.getCommunity().indexOf("|")) {
                String[] str = bh.getCommunity().split("\\|");
                int j = new Random().nextInt(str.length);
                for (int i = 0; i < str.length; i++) {
                    houses.addAll(houseService.findByCommunityUid(str[i], type));
                }
            } else {*/

            houses = findByCommunityUid(bh.getCommunity().getUid(), type);

            //加上户型一样的房源
            houses.addAll(findByLayout(bh.getLayout(), type));

            //如果小于5条数据则返回结果
            if (houses.size() <= 5) {
                List<House> recoHouses = recommendService.findByStatus("10");
                for (int i = 0; houses.size() < 5; ) {
                    houses.add(recoHouses.get(i));
                    i++;
                }
                return houses;
            }
        }

        return null;
    }

    @Override
    public List<House> hotHouse(double lon1, double lat1, String city, String type) {

        List<Community> communities = new ArrayList<Community>();
        List<Community> communityBycity = communityService.findByCity(city);
        List<String> uids = new ArrayList<>();
        List<House> houses = new ArrayList<>();

        for (Community community : communityBycity) {
            double lon2 = community.getLongitude().doubleValue();
            double lat2 = community.getLatitude().doubleValue();
            //计算两个点之间的距离
            double distance = GetDistanceUtil.GetDistance(lon1, lat1, lon2, lat2);
            if (distance <= 100000) {//100公里之内的
                communities.add(community);
            }
        }
        for (Community community : communities) {
            uids.add(community.getUid());
        }
        //return uids;
        for (int i = 0; i < uids.size(); i++) {
            houses.addAll(findByCommunityUid(uids.get(i), type));
            if (6 == houses.size()) break;
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < houses.size(); i++) {

            int clickNum = houses.get(i).getViewNum();
            int collect = collectService.countByHouseId(houses.get(i).getId());
            //热度值，clickNum*1  + collect*2
            int hotNum = clickNum + collect * 2;
            map.put(i, hotNum);
        }

        Map sortMap = MapSortUtils.sortByValue(map);
        List<Integer> list = new ArrayList<Integer>(sortMap.keySet());

        List<House> sortHouses = new ArrayList<>();
        for (int b = list.size() - 1; b >= 0; b--) {
            sortHouses.add(houses.get(list.get(b)));
            if (20 == sortHouses.size()) break;
        }
        return sortHouses;

    }

}

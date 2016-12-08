package com.ryel.zaja.dao.Impl;

import com.ryel.zaja.dao.custom.FilterDao;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.vo.FilterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by billyu on 2016/12/6.
 */
@Service
public class FilterDaoImpl implements FilterDao {

    @Autowired
    EntityManagerFactory emf;

    Logger logger = LoggerFactory.getLogger(FilterDaoImpl.class);



    @Override
    public List<House> findByFilter(FilterVo filterVo) {
        EntityManager em = emf.createEntityManager();

        StringBuilder sql = new StringBuilder("SELECT h.* FROM house as h WHERE 1=1 ");

        if (null != filterVo.getSellPrice()) {
            String str = filterVo.getSellPrice();
            String[] arr = str.split("\\|");
            if ("".equals(arr[0])) {
                 sql.append(" AND h.sell_price =  " + arr[1]);
            } else if ("".equals(arr[1])) {
                sql.append(" AND h.sell_price =  " + arr[0]);
            } else {
                sql.append(" AND h.sell_price BETWEEN " + arr[0] + " AND " + arr[1]);
            }
        }
        if (null != filterVo.getArea()) {
            String str = filterVo.getArea();
            String[] arr = str.split("\\|");
            if ("".equals(arr[0])) {
                sql.append(" AND h.area =  " + arr[1]);
            } else if ("".equals(arr[1])) {
                sql.append(" AND h.area =  " + arr[0]);
            } else {
                sql.append(" AND h.area BETWEEN " + arr[0] + " AND " + arr[1]);
            }
        }
        if(null != filterVo.getType()){
            sql.append(" AND h.type = " + filterVo.getType());
        }
        if(null != filterVo.getDecoration()){
            sql.append(" AND h.decoration = " + filterVo.getDecoration());
        }
        if(null != filterVo.getFloor()){
            sql.append(" AND h.floor = " + filterVo.getFloor());
        }

        Query query =  em.createNativeQuery(sql.toString(),House.class);
        List<House> houses= query.getResultList();
        em.close();

        return houses;
    }

}

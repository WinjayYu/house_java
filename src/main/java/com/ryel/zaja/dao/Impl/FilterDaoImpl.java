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
    public List<House> findByFilter(String sellPrice,
                                    String area,
                                    String houseType,
                                    String fitmentLevel,
                                    String floor
                                    ) {
        EntityManager em = emf.createEntityManager();

            StringBuffer sql = new StringBuffer("SELECT h.* FROM house as h WHERE 1=1 ");

        if (null != sellPrice) {
            String str = sellPrice;
            String[] arr = str.split("\\|");
            if ("".equals(arr[0])) {
                 sql.append(" AND h.sell_price =  " + arr[1]);
            } else if ("".equals(arr[1])) {
                sql.append(" AND h.sell_price =  " + arr[0]);
            } else {
                sql.append(" AND h.sell_price BETWEEN " + arr[0] + " AND " + arr[1]);
            }
        }
        if (null != area) {
            String str = area;
            String[] arr = str.split("\\|");
            if ("".equals(arr[0])) {
                sql.append(" AND h.area =  " + arr[1]);
            } else if ("".equals(arr[1])) {
                sql.append(" AND h.area =  " + arr[0]);
            } else {
                sql.append(" AND h.area BETWEEN " + arr[0] + " AND " + arr[1]);
            }
        }
        if(null != houseType){
            sql.append(" AND h.type = '" + houseType + "'");
        }
        if(null != fitmentLevel){
            sql.append(" AND h.fitment_level = '" + fitmentLevel + "'");
        }
        if(null != floor){
            sql.append(" AND h.floor = '" + floor +"'");
        }

        Query query =  em.createNativeQuery(sql.toString(),House.class);
        List<House> houses= query.getResultList();
        em.close();

        return houses;
    }

}


package com.ryel.zaja.dao.Impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by billyu on 2016/12/12.
 */
public class CollectDaoImpl {

    @PersistenceContext
    private EntityManager em;
    @SuppressWarnings("unchecked")
    public int count(Integer id) {
        String sql = "select count(c.houseId) from Collect c where c.house.id = ?1";

        Query dataQuery = em.createQuery(sql);

        int data = (Integer) dataQuery.getSingleResult();

        return data;
    }
}

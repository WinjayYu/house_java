package com.ryel.zaja.dao.Impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by billyu on 2016/12/23.
 */
public class CommentDaoImpl {

    @PersistenceContext
    private EntityManager em;
    @SuppressWarnings("unchecked")
    public Double avg(Integer agnetId) {
        String sql = "select avg(c.star) from Comment c where c.agent.id = " + agnetId;

        Query dataQuery = em.createQuery(sql);

        Double data = (Double) dataQuery.getSingleResult();

        return data;
    }
}

package com.ryel.springBootJpa.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangbin on 2016/6/28.
 */
@Transactional(readOnly = true)
public abstract class AbsCommonService<T> implements ICommonService<T>{

    public abstract JpaRepository<T,Integer> getDao();

    public List<T> findAll(){
       return getDao().findAll();
    }

    public Page<T> find(int pageNum, int pageSize) {
        return getDao().findAll(new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));
    }

    public T findById(int id){
        return getDao().findOne(id);
    }

    @Transactional
    public void deleteById(int id){
        getDao().delete(id);
    }

    @Transactional
    public void deleteByIds(int... ids){
        for(int id : ids){
            deleteById(id);
        }
    }

    @Transactional
    public T save(T t){
        return getDao().save(t);
    }




}

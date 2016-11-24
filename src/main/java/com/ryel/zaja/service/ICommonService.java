package com.ryel.zaja.service;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by wangbin on 2016/6/28.
 */
public interface ICommonService<T> {

    /**
     * 查询全部
     * @return
     */
     List<T> findAll();

     /**
      * 分页查询
      * @param pageNum
      * @param pageSize
      * @return
     */
     Page<T> find(int pageNum, int pageSize);

     /**
      * 根据ID查询
      * @param id
      * @return
     */
     T findById(int id);

     /**
      * 根据ID删除
      * @param id
      */
     void deleteById(int id);

    /**
     * 批量删除
     * @param ids
     */
    void deleteByIds(int... ids);


     /**
      * 新增和修改(根据ID来判断)
      * @param t
      * @return
     */
     T save(T t);

}

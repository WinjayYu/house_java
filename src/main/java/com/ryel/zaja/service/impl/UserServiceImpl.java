package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.exception.UserException;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.utils.ClassUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by burgl on 2016/8/16.
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends AbsCommonService<User> implements UserService {

    @Autowired
    private UserDao userDao;


    @Autowired
    EntityManagerFactory emf;

    @Transactional
    public User create(User user){

        this.save(user);
        return user;
    }



    @Override
    public User login(String mobile, String password) {
        User user = null;
        return user;
    }


    @Override
    @Transactional
    public User update(User user) {
        User origUser  = findById(user.getId());
        ClassUtil.copyProperties(origUser, user);
        return save(origUser);
    }



    @Override
    public List<User> list() {
        return userDao.findAll();
    }


    @Override
    public Page<User> findByPage(final String name, final Integer type, int pageNum, int pageSize) {
        Page<User> page = userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                Predicate result = null;
                if(StringUtils.isNotBlank(name)){
                    Predicate predicate = cb.like(root.get("name").as(String.class), "%"+name+"%");
                    predicateList.add(predicate);
                }
                if (type != null) {
                    Predicate predicate = cb.equal(root.get("type").as(Integer.class), type);
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
        }, new PageRequest(pageNum - 1, pageSize, Sort.Direction.ASC, "id"));

        return page;
    }



    @Override
    public JpaRepository<User, Integer> getDao() {
        return userDao;
    }

}

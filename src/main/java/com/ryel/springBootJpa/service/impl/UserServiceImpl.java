package com.ryel.springBootJpa.service.impl;

import com.ryel.springBootJpa.dao.UserDao;
import com.ryel.springBootJpa.dao.UserFllowDao;
import com.ryel.springBootJpa.entity.User;
import com.ryel.springBootJpa.entity.UserFllow;
import com.ryel.springBootJpa.exception.UserException;
import com.ryel.springBootJpa.service.AbsCommonService;
import com.ryel.springBootJpa.service.UserService;
import com.ryel.springBootJpa.utils.ClassUtil;
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
    private UserFllowDao userFllowDao;


    @Autowired
    EntityManagerFactory emf;

    @Transactional
    public User create(User user){
        user.setCreateDate(new Date());
        if (userDao.check(user.getEmail(), user.getTel()) != null){
            throw new UserException("手机或者邮箱已经存在");
        }
        userDao.save(user);
        return user;
    }



    @Override
    public User login(String username, String password) {
        User user = userDao.findByEmailOrMobileAndPassword(username, password);
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
    @Transactional
    public void watch(Integer userId, Integer fllowUserId) {
        if (userFllowDao.check(userId, fllowUserId) == null) {
            UserFllow fllow = new UserFllow();
            User user = new User();
            user.setId(userId);
            User fllowUser = new User();
            fllowUser.setId(fllowUserId);
            fllow.setFllowUser(fllowUser);
            fllow.setUser(user);
            userFllowDao.save(fllow);
        }
    }

    @Override
    public List<User> list() {
        return userDao.findAll();
    }

    /**
     * 获取普通注册用户列表(非主席和讲师)
     * @return
     */
    @Override
    public List<User> registUser() {
        return userDao.registUser();
    }

    @Override
    public List<User> watchlist(Integer userId) {
        return userFllowDao.watchlist(userId);
    }

    @Override
    public List<User> fllowlist(Integer userId) {
        return userFllowDao.fllowlist(userId);
    }



    @Override
    @Transactional
    public void unwatch(Integer userId, Integer fllowUserId) {
        userFllowDao.unWatch(userId, fllowUserId);
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

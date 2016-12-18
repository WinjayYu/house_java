package com.ryel.zaja.service.impl;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.AgentMaterial;
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
        if(userDao.findByMobile(user.getMobile()) != null ){
            throw new RuntimeException("手机号已存在！");
        }
        this.save(user);
        return user;
    }

    @Override
    public User login(String mobile, String password) {
        User user = userDao.findByMobileAndPassword(mobile, password);
        return user;
    }

    @Override
    public User agentLogin(String mobile, String password) {
        return userDao.findByMobileAndPasswordAndType(mobile, password,20+"");
    }


    @Override
    @Transactional
    public User update(User user) {
        User origUser  = findById(user.getId());
        ClassUtil.copyProperties(origUser, user);
        return save(origUser);
    }

    @Override
    public User findByMobile(String mobile) {
        return userDao.findByMobile(mobile);
    }

    @Override
    public List<User> list() {
        return userDao.findAll();
    }


    @Override
    public Page<User> findByPage(final String name, int pageNum, int pageSize) {
        Page<User> page = userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                Predicate result = null;
                if(StringUtils.isNotBlank(name)){
                    Predicate predicate = cb.like(root.get("name").as(String.class), "%"+name+"%");
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
    @Transactional
    public void agentRegister(User user, AgentMaterial agentMaterial, String verifyCode) {
        // TODO: 2016/12/18 校验经济人图片信息
        if(user == null 
                || agentMaterial == null
                || StringUtils.isBlank(user.getMobile())
                || StringUtils.isBlank(user.getPassword())
                || StringUtils.isBlank(agentMaterial.getIdcard())
                || StringUtils.isBlank(agentMaterial.getCompanyName())
                || StringUtils.isBlank(agentMaterial.getCompanyCode())
                ){
            throw new BizException(Error_code.ERROR_CODE_0023);
        }
        User u = userDao.findByMobile(user.getMobile());
        if(u != null){
            throw new BizException(Error_code.ERROR_CODE_0024);
        }
    }


    @Override
    public JpaRepository<User, Integer> getDao() {
        return userDao;
    }

}

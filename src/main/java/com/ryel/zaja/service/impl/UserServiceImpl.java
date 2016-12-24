package com.ryel.zaja.service.impl;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.AgentRegisterStatus;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.AgentMaterialDao;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.AgentMaterial;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.exception.UserException;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.AgentMaterialService;
import com.ryel.zaja.service.BizUploadFile;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.utils.ClassUtil;
import com.ryel.zaja.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

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
    private AgentMaterialService agentMaterialService;
    @Autowired
    private AgentMaterialDao agentMaterialDao;
    @Autowired
    private BizUploadFile bizUploadFile;


    @Autowired
    EntityManagerFactory emf;

    @Transactional
    @Override
    public User create(User user){
        if(userDao.findByMobile(user.getMobile()) != null ){
            throw new RuntimeException("手机号已存在！");
        }
        user.setAddTime(new Date());
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
    public void agentRegister(User user, AgentMaterial agentMaterial, String verifyCode,
                              MultipartFile positive, MultipartFile negative, MultipartFile companyPic) {
        // 校验参数
        if(user == null
                || agentMaterial == null
                || StringUtils.isBlank(user.getMobile())
                || StringUtils.isBlank(user.getPassword())
                || StringUtils.isBlank(agentMaterial.getIdcard())
                || StringUtils.isBlank(agentMaterial.getCompanyName())
                || StringUtils.isBlank(agentMaterial.getCompanyCode())
                ){
            throw new BizException(Error_code.ERROR_CODE_0023, "必填参数为空,user:" + JsonUtil.obj2Json(user)
                    + ",agentMaterial:" + JsonUtil.obj2Json(agentMaterial));
        }
        if(positive == null || negative == null || companyPic == null){
            throw new BizException(Error_code.ERROR_CODE_0023, "有图片为空");
        }
        User agent = userDao.findByMobile(user.getMobile());
        if(agent != null){
            throw new BizException(Error_code.ERROR_CODE_0024,"用户已经存在");
        }
        List<AgentMaterial> agentMaterialList = agentMaterialDao.findByIdcard(agentMaterial.getIdcard());
        if(!CollectionUtils.isEmpty(agentMaterialList)){
            throw new BizException(Error_code.card_exist,"身份证已经存在");
        }
        // 保存用户
        user.setAgentStatus(AgentRegisterStatus.APPROVE_APPLY.getCode());  // 申请审核状态
        user.setType(UserType.AGENT.getCode());
        create(user);
        // 上传图片
        String positivePath = bizUploadFile.uploadUserImageToQiniu(positive,user.getId());
        if(StringUtils.isBlank(positivePath)){
            throw new BizException(Error_code.ERROR_CODE_0025,"图片上传七牛失败");
        }
        String negativePath = bizUploadFile.uploadUserImageToQiniu(negative,user.getId());
        if(StringUtils.isBlank(negativePath)){
            throw new BizException(Error_code.ERROR_CODE_0025,"图片上传七牛失败");
        }
        String companyPicPath = bizUploadFile.uploadUserImageToQiniu(companyPic,user.getId());
        if(StringUtils.isBlank(companyPicPath)){
            throw new BizException(Error_code.ERROR_CODE_0025,"图片上传七牛失败");
        }
        // 保存扩展信息
        agentMaterial.setAgent(user);
        agentMaterial.setPositive(positivePath);
        agentMaterial.setNegative(negativePath);
        agentMaterial.setCompanyPic(companyPicPath);
        agentMaterialService.save(agentMaterial);
    }


    @Override
    public JpaRepository<User, Integer> getDao() {
        return userDao;
    }

}

package com.ryel.zaja.service.impl;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.HouseOrderStatus;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.HouseOrderDao;
import com.ryel.zaja.entity.Comment;
import com.ryel.zaja.entity.HouseOrder;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.HouseOrderService;
import com.ryel.zaja.utils.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burgl on 2016/8/27.
 */
@Service
public class HouseOrderServiceImpl extends AbsCommonService<HouseOrder> implements HouseOrderService {

    @Autowired
    private HouseOrderDao houseOrderDao;

    @Override
    @Transactional
    public HouseOrder create(HouseOrder houseOder) {
        return this.save(houseOder);
    }


    @Override
    public JpaRepository<HouseOrder, Integer> getDao() {
        return houseOrderDao;
    }

    @Override
    public List<HouseOrder> list(Integer id) {
        return houseOrderDao.list(id);
    }

    @Override
    @Transactional
    public HouseOrder update(HouseOrder houseOrder) {
        HouseOrder origHouseOrder = check(houseOrder.getId());
        ClassUtil.copyProperties(origHouseOrder, houseOrder);
        return houseOrderDao.save(houseOrder);
    }

    /**
     * 确认交接
     * @param userId
     * @param houseOrderId
     * @return
     */
    @Override
    @Transactional
    public HouseOrder confirm(Integer userId, Integer houseOrderId){
        HouseOrder houseOrder = check(houseOrderId);
        if(!HouseOrderStatus.WAIT_USER_COMFIRM.getCode().equals(houseOrder.getStatus())){
            throw new BizException(Error_code.ERROR_CODE_0025);
        }
        houseOrder.setStatus(HouseOrderStatus.WAIT_COMMENT.getCode());
        return update(houseOrder);
    }

    /**
     * 根据房源id查询已经付过款的订单
     */
    @Override
    public List<HouseOrder> findPayedOrderByHouseId(Integer houseId) {
        return houseOrderDao.findPayedOrderByHouseId(houseId,HouseOrderStatus.getPayedList());
    }

    @Override
    public List<HouseOrder> findPayedOrderByAgentId(Integer agentId) {
        Page<HouseOrder> page = houseOrderDao.findPayedOrderByAgentId(agentId, HouseOrderStatus.getPayedList(),
                new PageRequest(0, 20, Sort.Direction.DESC, "id"));
        return page.getContent();
    }

    public HouseOrder check(Integer houseOrderId){
        HouseOrder houseOrder = findById(houseOrderId);
        if(null == houseOrder){
            throw new BizException(Error_code.ERROR_CODE_0025);
        }
        return  houseOrder;
    }

    @Override
    public Page<HouseOrder> pageByUserId(Integer userId, Pageable pageable) {
        return houseOrderDao.pageByUserId(userId,pageable);
    }

    @Override
    public Page<HouseOrder> pageByAgentId(Integer agentId, Pageable pageable) {
        return houseOrderDao.pageByAgentId(agentId,pageable);
    }
}

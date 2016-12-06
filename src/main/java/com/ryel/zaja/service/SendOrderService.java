package com.ryel.zaja.service;

import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.SendOrder;
import com.ryel.zaja.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by billyu on 2016/11/28.
 */
public interface SendOrderService extends ICommonService<SendOrder>{

    SendOrder create(SendOrder sendOrder);


    /**
     * 获取所有发送订单列表
     * @return
     */
    List<SendOrder> list();

    Page<SendOrder> findByPage(Integer agentId, int pageNum, int pageSize);


}

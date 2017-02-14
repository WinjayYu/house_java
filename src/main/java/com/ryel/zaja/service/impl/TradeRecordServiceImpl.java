package com.ryel.zaja.service.impl;

import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.TradeRecordDao;
import com.ryel.zaja.entity.TradeRecord;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.TradeRecordService;
import com.ryel.zaja.utils.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TradeRecordServiceImpl extends AbsCommonService<TradeRecord> implements TradeRecordService {
    protected final static Logger logger = LoggerFactory.getLogger(TradeRecordServiceImpl.class);

    @Autowired
    private TradeRecordDao tradeRecordDao;

    @Override
    public TradeRecord findByThirdHtId(String thirdHtId) {
        return  tradeRecordDao.findByThirdHtId(thirdHtId);
    }

    @Override
    @Transactional
    public TradeRecord create(TradeRecord tradeRecord) {
        return tradeRecordDao.save(tradeRecord);
    }

    @Override
    @Transactional
    public TradeRecord update(TradeRecord tradeRecord) {
        TradeRecord dest  = findById(tradeRecord.getId());
        ClassUtil.copyProperties(dest, tradeRecord);
        return save(dest);
    }

    @Override
    @Transactional
    public TradeRecord updateStatus(String thirdHtId,String status) {
        TradeRecord dest  = findByThirdHtId(thirdHtId);
        dest.setStatus(status);
//        ClassUtil.copyProperties(dest, tradeRecord);
        return save(dest);
    }

    @Override
    public TradeRecord findByOrderId(Integer orderId) {
        return tradeRecordDao.findByOrderId(orderId);
    }

    @Override
    public Page<TradeRecord> findByInThirdCustId(Integer userId) {
        return tradeRecordDao.findByInThirdCustId(userId,new PageRequest(0,20, Sort.Direction.DESC, "id"));
    }

    @Override
    public JpaRepository<TradeRecord, Integer> getDao() {
        return tradeRecordDao;
    }


}

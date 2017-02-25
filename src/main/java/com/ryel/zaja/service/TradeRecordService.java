package com.ryel.zaja.service;

import com.ryel.zaja.entity.TradeRecord;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TradeRecordService extends ICommonService<TradeRecord> {
    TradeRecord findByThirdHtId(String thirdHtId);
    TradeRecord create(TradeRecord tradeRecord);
    TradeRecord update(TradeRecord tradeRecord);
    TradeRecord updateStatus(String thirdHtId,String status);
    TradeRecord findByOrderId(Integer orderId);

    Page<TradeRecord> findByInThirdCustId(Integer userId);

}

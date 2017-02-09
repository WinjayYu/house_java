package com.ryel.zaja.service;

import com.ryel.zaja.entity.TradeRecord;

public interface TradeRecordService extends ICommonService<TradeRecord> {
    TradeRecord findByThirdHtId(String thirdHtId);
    TradeRecord create(TradeRecord tradeRecord);
    TradeRecord update(TradeRecord tradeRecord);
    TradeRecord updateStatus(String thirdHtId,String status);

}

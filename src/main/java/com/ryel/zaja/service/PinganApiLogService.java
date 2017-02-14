package com.ryel.zaja.service;

import com.ryel.zaja.config.enums.PinganApiEnum;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.PinganApiLog;

public interface PinganApiLogService extends ICommonService<PinganApiLog> {
    PinganApiLog create(PinganApiEnum pinganApiEnum,String request,String response,String thirdlogno);

}

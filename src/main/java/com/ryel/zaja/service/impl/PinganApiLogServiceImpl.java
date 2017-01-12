package com.ryel.zaja.service.impl;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.config.enums.PinganApiEnum;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.dao.HouseDao;
import com.ryel.zaja.dao.PinganApiLogDao;
import com.ryel.zaja.entity.BuyHouse;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.PinganApiLog;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.ClassUtil;
import com.ryel.zaja.utils.GetDistanceUtil;
import com.ryel.zaja.utils.JsonUtil;
import com.ryel.zaja.utils.MapSortUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PinganApiLogServiceImpl extends AbsCommonService<PinganApiLog> implements PinganApiLogService {
    protected final static Logger logger = LoggerFactory.getLogger(PinganApiLogServiceImpl.class);

    @Autowired
    private PinganApiLogDao pinganApiLogDao;

    @Override
    @Transactional
    public PinganApiLog create(PinganApiEnum pinganApiEnum, String request, String response, Integer userId) {
        PinganApiLog pinganApiLog = new PinganApiLog();
        try {
            pinganApiLog.setCode(pinganApiEnum.getCode());
            pinganApiLog.setName(pinganApiEnum.getName());
            pinganApiLog.setPinganCode(pinganApiEnum.getPinganCode());
            pinganApiLog.setRequest(request);
            pinganApiLog.setResponse(response);
            pinganApiLog.setAddBy(userId);
            pinganApiLog.setAddTime(new Date());
            logger.info(JsonUtil.obj2Json(pinganApiLog));
            return pinganApiLogDao.save(pinganApiLog);
        } catch (Exception e) {
            logger.error("保存PinganApiLog异常", e);
        }
        return null;
    }


    @Override
    public JpaRepository<PinganApiLog, Integer> getDao() {
        return pinganApiLogDao;
    }

}

package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.AgentLocationDao;
import com.ryel.zaja.entity.AgentLocation;
import com.ryel.zaja.entity.Community;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.AgentLocationService;
import com.ryel.zaja.utils.ClassUtil;
import com.ryel.zaja.utils.GetDistanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by billyu on 2017/1/7.
 */
@Service
@Transactional(readOnly = true)
public class AgentLocationServiceImpl extends AbsCommonService<AgentLocation> implements AgentLocationService {

    @Autowired
    AgentLocationDao agentLocationDao;

    @Override
    public JpaRepository<AgentLocation, Integer> getDao() {
        return agentLocationDao;
    }

    @Override
    public List<AgentLocation> findByDis(String district) {
        return agentLocationDao.findByDistrict(district);
    }

    @Override
    public List<AgentLocation> findByLoc(Double lon1, Double lat1, List<AgentLocation> listByDis) {
        List<AgentLocation> list = new ArrayList<>();
        for (AgentLocation agentLocation : listByDis) {
            double lon2 = agentLocation.getLongitude().doubleValue();
            double lat2 = agentLocation.getLatitude().doubleValue();
            //计算两个点之间的距离
            double distance = GetDistanceUtil.GetDistance(lon1, lat1, lon2, lat2);
            if (distance <= 5000) {//10公里之内的
                list.add(agentLocation);
            }
        }
        return list;
    }

    @Override
    public AgentLocation findByAgent(Integer agentId) {
        return agentLocationDao.findByAgentId(agentId);
    }

    @Override
    @Transactional
    public AgentLocation create(AgentLocation agentLocation) {
        return save(agentLocation);
    }

    @Override
    @Transactional
    public AgentLocation update(AgentLocation origAgentLocation, AgentLocation agentLocation) {
        ClassUtil.copyProperties(origAgentLocation, agentLocation);
        return save(origAgentLocation);
    }

    @Override
    public List<AgentLocation> findByCity(String city) {
        return agentLocationDao.findByCity(city);
    }
}

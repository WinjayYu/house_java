package com.ryel.zaja.service.impl;

import com.ryel.zaja.dao.HouseTagDao;
import com.ryel.zaja.dao.PushDeviceDao;
import com.ryel.zaja.entity.PushDevice;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.AbsCommonService;
import com.ryel.zaja.service.PushDeviceService;
import com.ryel.zaja.utils.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Nathan on 2017/1/4.
 */
@Service
@Transactional(readOnly = true)
public class PushDeviceServiceImpl extends AbsCommonService<PushDevice> implements PushDeviceService {

    @Autowired
    private PushDeviceDao deviceDao;

    @Override
    @Transactional
    public void create(PushDevice device) {

        PushDevice origdevice = findByUser(device.getUser());
        if(origdevice == null)
        {
            save(device);
            return;
        }
        ClassUtil.copyProperties(origdevice, device);
        save(origdevice);
    }

    @Override
    public PushDevice findByUser(User user) {
        return deviceDao.findByUser(user);
    }

    @Override
    public JpaRepository<PushDevice, Integer> getDao() {
        return deviceDao;
    }
}

package com.ryel.zaja.service;

import com.ryel.zaja.entity.PushDevice;
import com.ryel.zaja.entity.User;

/**
 * Created by Nathan on 2017/1/4.
 */
public interface PushDeviceService extends ICommonService<PushDevice> {

    void create(PushDevice device);

    PushDevice findByUser(User user);
}

package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.entity.PushDevice;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.PushDeviceService;
import com.ryel.zaja.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Nathan on 2017/1/4.
 */
@RestController()
@RequestMapping(value = "/api/push/", produces = "application/json; charset=UTF-8")
public class PushApi {


    @Autowired
    private PushDeviceService pushService;

    @Autowired
    private UserService userService;

    /**
     * 存储设备型号
     *
     */
    @RequestMapping(value = "device", method = RequestMethod.POST)
    public Result device(Integer userId, String device) {
        try {

            User user =userService.findById(userId);
            if(null == user)
            {
                return Result.error().msg(Error_code.ERROR_CODE_0004).data(new HashMap<>());
            }
            PushDevice pushdevice = new PushDevice();
            pushdevice.setUser(user);
            pushdevice.setDevice(device);
            pushdevice.setAddTime(new Date());
            pushService.create(pushdevice);

            return Result.success().msg("").data(new HashMap<>());
        } catch (Exception e) {
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

}

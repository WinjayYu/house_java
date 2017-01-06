package com.ryel.zaja.service;

import com.ryel.zaja.config.enums.HouseOrderStatus;
import com.ryel.zaja.entity.HouseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by billyu on 2016/12/28.
 */
@Component
public class ScheduledTasks {

    @Autowired
    HouseOrderService houseOrderService;

   /* private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
    }*/
    @Scheduled(cron = "0 0 0 ? * *")
    public void allOrder() {
        Date currentTime = new Date();

        List<HouseOrder> list = houseOrderService.findAll();
        for (HouseOrder houseOrder : list) {
            if (currentTime.getTime() - houseOrder.getAddTime().getTime() > (10 * 24 * 60 * 60 * 1000)){
                houseOrder.setStatus(HouseOrderStatus.CLOSED.getCode());
            }
        }
    }

}

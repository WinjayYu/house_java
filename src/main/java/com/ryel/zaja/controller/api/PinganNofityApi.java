package com.ryel.zaja.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/api/pingannotify/", produces = "application/json; charset=UTF-8")
public class PinganNofityApi {
    protected final static Logger logger = LoggerFactory.getLogger(PinganNofityApi.class);

    @RequestMapping(value = "commissionnotify")
    public void submitNotify(String orig, String sign) {
        try {
            System.err.print("---orig---" + orig);
            System.err.print("---sign---" + sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}


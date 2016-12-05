package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.bean.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by billyu on 2016/12/3.
 */
@RestController
@RequestMapping(value = "/api/home")
public class HomeApi {

    @RequestMapping(value = "home", method = RequestMethod.POST)
    public Result home(){
    return null;
    }
}

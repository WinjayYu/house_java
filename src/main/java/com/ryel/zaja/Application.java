package com.ryel.zaja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by burgl on 2016/8/13.
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }



}

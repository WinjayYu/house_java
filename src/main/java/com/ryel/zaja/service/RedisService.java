package com.ryel.zaja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billyu on 2016/12/29.
 */
@Service
public class RedisService {


    @Autowired
    private RedisTemplate<String,Integer> redisTemplate;


    public void insert(Integer id, int num) {

        redisTemplate.opsForValue().set(id.toString(), num);

    }

    public void incr(String type, Integer id){
//        stringRedisTemplate.opsForValue().increment(type, id);
    }



}

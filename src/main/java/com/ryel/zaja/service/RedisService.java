package com.ryel.zaja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by billyu on 2016/12/29.
 */
@Service
public class RedisService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private RedisTemplate<String, Map<Integer, Integer>> longTemplate;


    public Object findOne(String type, Integer id) {
        Object info = longTemplate.opsForHash().get(type, id);
        // boolean have = userTemplate.opsForHash().hasKey(hashKey, hashKey);
        return info != null ? info : null;
    }

    public void incrementLong(String type, Integer id, Integer num) {
        Long value1 = longTemplate.opsForHash().increment(type, id, num);
//        long value2 = LongTemplate.opsForHash().increment(type, id, num);
    }

    public void insert(String type, Integer id, Integer num) {
        longTemplate.opsForHash().put(type, id, num);

//        log.info("insert User[" + info + "] success!");
//        log.info("User Hash size is : " + userTemplate.opsForHash().size(key));
    }

    public RedisTemplate<String, Map<Integer, Integer>> getLongTemplate() {
        return longTemplate;
    }


}

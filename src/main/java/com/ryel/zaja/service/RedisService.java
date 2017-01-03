package com.ryel.zaja.service;

import com.ryel.zaja.entity.House;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


/**
 * Created by billyu on 2016/12/29.
 */
@Service
public class RedisService {

    // CACHE_NAME在redis中会作为key的前缀
    private static final String CACHE_NAME = "MyBusiness_Test:";

    // 过期时间设为0，表示永不过期。
    private static final int EXPIRE_TIME = 0;

    @Autowired
    private StringRedisTemplate template;

    private RedisCache cache;

    @PostConstruct
    public void init() {
        cache = new RedisCache(CACHE_NAME, CACHE_NAME.getBytes(), template, EXPIRE_TIME);
    }

    // redis set <K,V>
    public void put(String key, House obj) {
        cache.put(key, obj);
    }

    // redis get <K>
    public House get(String key) {
        return cache.get(key) == null ? null :
                cache.get(key, House.class);
    }

    // redis del <K>
    public void de(String key) {
        cache.evict(key);
    }



    public void incr(String type, Integer id){
//        stringRedisTemplate.opsForValue().increment(type, id);
    }
}

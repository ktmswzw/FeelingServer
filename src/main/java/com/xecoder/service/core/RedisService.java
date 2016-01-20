package com.xecoder.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/19-8:55
 * Feeling.com.xecoder.service.core
 */

@Component
public class RedisService {
    private final Random random = new Random();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(StringRedisTemplate template) {
        this.redisTemplate = template;
    }


    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        int timeout = random.nextInt(86400);
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }
}
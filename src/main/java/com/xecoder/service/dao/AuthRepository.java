package com.xecoder.service.dao;

import com.xecoder.service.core.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/18-17:27
 * Feeling.com.xecoder.service.dao
 */
@Service
public class AuthRepository  {

    @Autowired
    private RedisService redisService;

}

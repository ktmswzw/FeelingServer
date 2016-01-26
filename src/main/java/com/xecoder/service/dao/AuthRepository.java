package com.xecoder.service.dao;

import com.xecoder.model.business.Auth;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/18-17:27
 * Feeling.com.xecoder.service.dao
 */
@Service
public interface AuthRepository  extends MongoRepository<Auth, String> {
    Auth findByOwner(String phone);
}

package com.xecoder.dao;

import com.xecoder.model.business.Auth;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/18-17:27
 * Feeling.com.xecoder.dao
 */
@Repository
public interface AuthDao extends MongoRepository<Auth, String> {
    Auth findByOwner(String phone);
}

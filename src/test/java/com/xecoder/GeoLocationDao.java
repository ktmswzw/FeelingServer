package com.xecoder;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/28-12:38
 * Feeling.com.xecoder.service.dao
 */
@Repository
public interface GeoLocationDao extends MongoRepository<GeoLocation,String> {
}

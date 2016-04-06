package com.xecoder.dao;

import com.xecoder.model.business.LogRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/22-14:06
 * Feeling.com.xecoder.dao
 */
@Repository
public interface LogRecordDao extends MongoRepository<LogRecord,String> {
}

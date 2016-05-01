package com.xecoder.dao;

import com.xecoder.model.business.TryHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by vincent on 16/5/1.
 */
@Repository
public interface TryHistoryDao extends MongoRepository<TryHistory,String> {
}

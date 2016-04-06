package com.xecoder.dao;

import com.xecoder.model.business.Friend;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by vincent on 4/6/16.
 */
public interface FriendDao extends MongoRepository<Friend,String> {
}

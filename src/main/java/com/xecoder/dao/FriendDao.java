package com.xecoder.dao;

import com.xecoder.model.business.Friend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by vincent on 4/6/16.
 */

@Repository
public interface FriendDao extends MongoRepository<Friend,String> {
}

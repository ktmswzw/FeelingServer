package com.xecoder.dao;

import com.xecoder.model.business.FriendGrouping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by vincent on 4/6/16.
 * http://www.baeldung.com/queries-in-spring-data-mongodb
 */

@Repository
public interface FriendGroupingDao extends MongoRepository<FriendGrouping,String>{ //,QueryDslPredicateExecutor<FriendGrouping>
    List<FriendGrouping> findByGroupingAndUserId(String grouping, String id);
}

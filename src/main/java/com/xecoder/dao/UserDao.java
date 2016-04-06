package com.xecoder.dao;

import com.xecoder.model.business.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/16-17:06
 * Feeling.com.xecoder.dao
 */

@Repository
//@Qualifier("primary")
public interface UserDao extends MongoRepository<User, String> {

    String FIND =
            "{$or:[" +
                    "{'customerName': {$regex: '?0', $options: 'i'}}," +
                    "{'contactFirstName': {$regex: '?0', $options: 'i'}}," +
                    "{'contactLastName': {$regex: '?0', $options: 'i'}}" +
                    "]}";

    User findByPhone(String phone);
//
//    @Query(value = FIND, count = true)
//    Long searchCount(String keyword);
}

package com.xecoder.dao;

import com.xecoder.model.embedded.MessagesSecret;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/3/13-14:21
 * Feeling.com.xecoder.dao
 */
@Repository
public interface MessagesSecretDao extends MongoRepository<MessagesSecret,String> {

    MessagesSecret findByMsgId(String msgId);
}

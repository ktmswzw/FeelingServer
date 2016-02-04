package com.xecoder.service.dao;

import com.xecoder.model.business.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/2/4-16:00
 * Feeling.com.xecoder.service.dao
 */
@Repository
public interface BookDao  extends MongoRepository<Book, String> {
}

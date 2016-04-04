package com.xecoder.service.service;

import com.xecoder.model.embedded.MessagesSecret;
import com.xecoder.service.core.AbstractService;
import com.xecoder.service.dao.MessagesSecretDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/3/13-14:22
 * Feeling.com.xecoder.service.service
 */
@Service(value = "messagesSecretService")
public class MessagesSecretService extends AbstractService<MessagesSecret> {


    @Autowired
    private MessagesSecretDao dao;

    @Override
    protected MongoRepository<MessagesSecret, String> getRepository() {
        return dao;
    }

    @Override
    protected long count(MessagesSecret searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query = makeQuery(criteria);
        return doCount(query, MessagesSecret.class);
    }

    public MessagesSecret findByMsgId(String msgId){
        return dao.findByMsgId(msgId);
    }

    @Override
    public List<MessagesSecret> search(int page, int size, Sort sort, MessagesSecret searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query= makeQuery(criteria);
        query.skip(calcSkipNum(page, size)).limit(size);
        if (sort != null) {
            query.with(sort);
        }
        return doFind(query, MessagesSecret.class);
    }

    @Override
    protected Criteria makeCriteriaByPk(MessagesSecret model) {
        return null;
    }

    @Override
    protected Criteria makeCriteria(MessagesSecret model) {
        Criteria criteria = null;
        if (StringUtils.isNotEmpty(model.getMsgId())) {
            criteria = makeCriteria(criteria, "msg_id", model.getMsgId());
        }
        return criteria;
    }

    @Override
    public MessagesSecret findByPk(Object... keys) {
            return dao.findOne((String) keys[0]);
    }

    @Override
    public Iterable<MessagesSecret> findByNameLike(String name, String sortColumn) {
        return null;
    }

    @Override
    public Iterable<MessagesSecret> search(String keyword, int page, int size, String sortColumn) {
        return null;
    }
}

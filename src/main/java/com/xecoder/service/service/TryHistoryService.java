package com.xecoder.service.service;

import com.xecoder.dao.TryHistoryDao;
import com.xecoder.model.business.Messages;
import com.xecoder.model.business.TryHistory;
import com.xecoder.service.core.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vincent on 16/5/1.
 */
@Service(value = "tryHistoryService")
public class TryHistoryService extends AbstractService<TryHistory> {

    @Autowired
    private TryHistoryDao dao;

    @Override
    protected MongoRepository<TryHistory, String> getRepository() {
        return dao;
    }

    @Override
    public long count(TryHistory searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query = makeQuery(criteria);
        return doCount(query, TryHistory.class);
    }

    @Override
    public List<TryHistory> search(int page, int size, Sort sort, TryHistory searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query = makeQuery(criteria);
        query.skip(calcSkipNum(page, size)).limit(size);
        return doFind(query, TryHistory.class);
    }

    @Override
    protected Criteria makeCriteria(TryHistory model) {
        Criteria criteria = null;
        if (StringUtils.isNotEmpty(model.getMessageId())) {
            criteria = makeCriteria(criteria, "messageId",model.getMessageId());
        }
        if (StringUtils.isNotEmpty(model.getTryId())) {
            criteria = makeCriteria(criteria, "try_id",model.getTryId());
        }
        return criteria;
    }

    @Override
    public TryHistory findByPk(Object... keys) {
        return dao.findOne(String.valueOf(keys));
    }
}

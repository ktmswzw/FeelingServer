package com.xecoder.service.impl;

import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBObject;
import com.xecoder.model.business.Messages;
import com.xecoder.service.core.AbstractService;
import com.xecoder.service.dao.MessagesDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/27-13:37
 * Feeling.com.xecoder.service.impl
 */
@Service
public class MessagesServerImpl extends AbstractService<Messages> {

    @Autowired
    private MessagesDao messagesDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected MongoRepository<Messages, String> getRepository() {
        return messagesDao;
    }

    @Override
    protected long count(Messages searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query = makeQuery(criteria);
        return doCount(query, Messages.class);
    }

    @Override
    protected List<Messages> search(int page, int size, Sort sort, Messages searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query= makeQuery(criteria);
        query.skip(calcSkipNum(page, size)).limit(size);
        if (sort != null) {
            query.with(sort);
        }
        return doFind(query, Messages.class);
    }

    @Override
    protected Criteria makeCriteriaByPk(Messages model) {
        return null;
    }

    @Override
    protected Criteria makeCriteria(Messages model) {
        Criteria criteria = null;
        if (StringUtils.isNotEmpty(model.getTo())) {
            criteria = makeCriteriaRegex(criteria, "to", "^.*" + model.getTo() + ".*$");//模糊查询
        }
        criteria = makeCriteria(criteria,"state",Messages.CLOSE);
        return criteria;
    }

    @Override
    protected Update makeAllUpdate(Messages model) {
        return null;
    }

    @Override
    public Messages findByPk(Object... keys) {
        return null;
    }

    @Override
    public Iterable<Messages> findByNameLike(String name, String sortColumn) {
        return null;
    }

    @Override
    public long searchCount(String keyword) {
        return 0;
    }

    @Override
    public Iterable<Messages> search(String keyword, int page, int size, String sortColumn) {
        return null;
    }


    public List<DBObject> geoNear(DBObject query, Point point, int limit, long maxDistance) {
        if(query==null)
            query = new BasicDBObject();

        List<DBObject> pipeLine = new ArrayList<>();
        BasicDBObject aggregate = new BasicDBObject("geoNear",
                new BasicDBObject("near",new BasicDBObject("type","Point").append("coordinates",new double[]{point.getX(),point.getY()})))
                .append("query",query)
                .append("num",limit)
                .append("maxDistance",maxDistance)
                .append("spherical",true);
        pipeLine.add(aggregate);
        Cursor cursor=mongoTemplate.getCollection("messages").aggregate(pipeLine, AggregationOptions.builder().build());
        List<DBObject> list = new LinkedList<>();
        while (cursor.hasNext()) {
            list.add(cursor.next());
        }
        return list;
    }
}

package com.xecoder.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Locale;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/12-15:07
 * Feeling.com.xecoder.service
 */

public abstract class AbstractService<T> implements IService<T> {

    @Autowired
    private MongoTemplate template;


    @Autowired
    public MessageSource messageSource;

    @Override
    public long count() {
        return getRepository().count();
    }

    @Override
    public T findById(String id) {
        return getRepository().findOne(id);
    }

    @Override
    public Iterable<T> findAll(int page, int size, String sort) {
        Pageable pager = new PageRequest(currentPage(page), size, Sort.Direction.ASC, sort);
        Iterable<T> result = getRepository().findAll(pager);
        return result;
    }

    @Override
    public T save(T model) {
        return getRepository().save(model);
    }

    @Override
    public Iterable<T> save(Iterable<T> model) {
        return getRepository().save(model);
    }

    @Override
    public void delete(String id) {
        getRepository().delete(id);
    }

    @Override
    public void delete(Iterable<T> model) {
        getRepository().delete(model);
    }

    abstract protected MongoRepository<T, String> getRepository();


    protected long doCount(Query query, Class<T> clazz) {
        return template.count(query, clazz);
    }

    protected T doFindOne(Query query, Class<T> clazz) {
        return template.findOne(query, clazz);
    }

    protected List<T> doFind(Query query, Class<T> clazz) {
        return template.find(query, clazz);
    }

    protected List<T> doFindAll(Class<T> clazz) {
        return template.findAll(clazz);
    }

    protected Criteria makeCriteriaById(String id) {
        return Criteria.where("id").is(id);
    }

    protected Criteria makeCriteriaRegex(Criteria criteria, String field, String param) {
        if (criteria == null) {
            criteria = Criteria.where(field).regex(param,"i");
        } else {
            criteria.and(field).regex(param,"i");
        }
        return criteria;
    }

    protected Criteria makeCriteria(Criteria criteria, String field, Object param) {
        if (criteria == null) {
            criteria = Criteria.where(field).is(param);
        } else {
            criteria.and(field).is(param);
        }
        return criteria;
    }

    protected Criteria makeWhere(String name) {
        return Criteria.where(name);
    }

    protected Criteria makeWhere(String name, Object param) {
        return Criteria.where(name).is(param);
    }

    protected Query makeQuery(Criteria criteria) {
        Query query;
        if (criteria != null) {
            query = new Query(criteria);
        } else {
            query = new Query();
        }
        return query;
    }

    protected int calcSkipNum(int page, int size) {
        return (page - 1) * size;
    }

    protected String getLocalException(String errorKey)
    {
        return this.messageSource.getMessage(errorKey,null, Locale.getDefault());
    }

    abstract public long count(T searchCondition);
    abstract public List<T> search(int page, int size, Sort sort, T searchCondition);
    abstract protected Criteria makeCriteria(T model);

}

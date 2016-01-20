package com.xecoder.service.impl;

import com.xecoder.model.business.Auth;
import com.xecoder.model.business.AuthToken;
import com.xecoder.service.core.AbstractService;
import com.xecoder.service.core.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/20-11:19
 * Feeling.com.xecoder.service.impl
 */

@Service
public class AuthServerImpl extends AbstractService<Auth> {


    @Autowired
    private RedisService redisService;

    private static int EXPIRED_SECOND = (int) (AuthToken.EXPIRED_TIME / 1000);

    private static String PREFIX_KEY = "AT_";
    private static String PREFIX_DEVICE_KEY = "DI_";

    @Override
    protected MongoRepository<Auth, String> getRepository() {
        return null;
    }

    @Override
    protected long count(Auth searchCondition) {
        return 0;
    }

    @Override
    protected List<Auth> search(int page, int size, Sort sort, Auth searchCondition) {
        return null;
    }

    @Override
    protected Criteria makeCriteriaByPk(Auth model) {
        return null;
    }

    @Override
    protected Criteria makeCriteria(Auth model) {
        return null;
    }

    @Override
    protected Update makeAllUpdate(Auth model) {
        return null;
    }

    @Override
    public Auth findByPk(Object... keys) {
        return null;
    }

    @Override
    public Iterable<Auth> findByNameLike(String name, String sortColumn) {
        return null;
    }

    @Override
    public long searchCount(String keyword) {
        return 0;
    }

    @Override
    public Iterable<Auth> search(String keyword, int page, int size, String sortColumn) {
        return null;
    }

    public void storeToken(AuthToken loginToken) {
        String key = loginToken.getToken();
        redisService.setValue(getKey(key), loginToken.getUser().getId());
        redisService.expire(getKey(key), EXPIRED_SECOND);
    }

    private String getKey(String key) {
        return PREFIX_KEY + key;
    }
}

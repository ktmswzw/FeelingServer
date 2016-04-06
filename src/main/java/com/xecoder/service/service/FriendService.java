package com.xecoder.service.service;

import com.xecoder.common.util.DateTools;
import com.xecoder.dao.FriendDao;
import com.xecoder.model.business.Friend;
import com.xecoder.model.business.Messages;
import com.xecoder.service.core.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by vincent on 4/6/16.
 */
@Service(value = "friendService")
public class FriendService extends AbstractService<Friend>{
    @Autowired
    private FriendDao friendDao;

    @Override
    protected MongoRepository<Friend, String> getRepository() {
        return friendDao;
    }

    @Override
    protected long count(Friend searchCondition) {
        return 0;
    }

    @Override
    public List<Friend> search(int page, int size, Sort sort, Friend searchCondition) {
        return null;
    }

    @Override
    protected Criteria makeCriteria(Friend model) {
        Criteria criteria = null;
        if (StringUtils.isNotEmpty(model.getId())) {
            criteria = makeCriteria(criteria, "_id", new ObjectId(model.getId()));
        }
        if (StringUtils.isNotEmpty(model.getKeyUserId())) {
            criteria = makeCriteria(criteria, "key_user_id", model.getKeyUserId());
        }
        if (StringUtils.isNotEmpty(model.getRemark())) {
            criteria = makeCriteria(criteria, "remark", model.getRemark());
        }
        if (StringUtils.isNotEmpty(model.getUser())) {
            criteria = makeCriteria(criteria, "user", model.getUser());
        }
        criteria = makeCriteria(criteria, "blacklist", false);//未被开启过

        return criteria;
    }

    @Override
    public Friend findByPk(Object... keys) {
        return null;
    }

}

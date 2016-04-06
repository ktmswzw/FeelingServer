package com.xecoder.service.service;

import com.xecoder.dao.FriendDao;
import com.xecoder.dao.FriendGroupingDao;
import com.xecoder.model.business.Friend;
import com.xecoder.model.business.FriendGrouping;
import com.xecoder.model.business.Messages;
import com.xecoder.service.core.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 4/6/16.
 */
@Service(value = "friendService")
public class FriendService extends AbstractService<Friend>{
    @Autowired
    private FriendDao friendDao;

    @Autowired
    private FriendGroupingDao friendGroupingDao;

    @Override
    protected MongoRepository<Friend, String> getRepository() {
        return friendDao;
    }

    @Override
    protected long count(Friend searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query = makeQuery(criteria);
        return doCount(query, Friend.class);
    }

    @Override
    public List<Friend> search(int page, int size, Sort sort, Friend searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query = makeQuery(criteria);
        query.skip(calcSkipNum(page, size)).limit(size);
        List<Friend> list = doFind(query, Friend.class);
        return list;
    }

    /**
     * 进一步判断保存用户
     *
     * @param friend
     * @return
     */
    public Friend addAdv(Friend friend) {
        String groupingName = friend.getGrouping(), relUserId = friend.getUser();
        //判断分组情况
        FriendGrouping friendGrouping = new FriendGrouping();
        friendGrouping.setUserId(friend.getKeyUserId());
        groupingName = StringUtils.isNotBlank(groupingName) ? groupingName : "默认分组";
        List<FriendGrouping> list = friendGroupingDao.findByGroupingAndUserId(friend.getGrouping(),friend.getKeyUserId());
        if (list.size() == 0) {
            friendGrouping.setSort(1);
            friendGrouping.setGrouping(groupingName);
            friendGroupingDao.save(friendGrouping);
        } else {
            boolean isExist = false;
            for (FriendGrouping friendGrouping1 : list) {
                if (StringUtils.equals(friendGrouping1.getGrouping(), groupingName)) {
                    isExist = true;
                }
            }
            if (isExist) {
                friendGrouping.setSort(1);
                friendGrouping.setGrouping(groupingName);
                friendGroupingDao.save(friendGrouping);
            }
        }
        //开始保存
        Friend searchFriend = new Friend();
        searchFriend.setKeyUserId(friend.getKeyUserId());
        searchFriend.setUser(relUserId);
        searchFriend.setAll(true);
        List<Friend> list1 = this.search(0,1000,null,searchFriend);
        if (list1.size() != 0) {
            Friend result = list1.get(0);
            result.setBlacklist(false);
            result.setGrouping(groupingName);
            return this.save(result);//更新
        } else {
            Friend friend1 = new Friend();
            friend1.setGrouping(groupingName);
            friend1.setKeyUserId(friend.getKeyUserId());
            friend1.setUser(relUserId);
            return save(friend1);//保存
        }
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
        if(!model.isAll())
        criteria = makeCriteria(criteria, "blacklist", model.isBlacklist());

        return criteria;
    }

    @Override
    public Friend findByPk(Object... keys) {
        return null;
    }

}

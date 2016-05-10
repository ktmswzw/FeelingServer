package com.xecoder.service.service;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.xecoder.common.util.DateTools;
import com.xecoder.common.util.ImageUtil;
import com.xecoder.common.util.SurfaceDistanceUtils;
import com.xecoder.model.business.Messages;
import com.xecoder.model.business.User;
import com.xecoder.model.embedded.MessagesSecret;
import com.xecoder.service.core.AbstractService;
import com.xecoder.dao.MessagesDao;
import com.xecoder.dao.MessagesSecretDao;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/27-13:37
 * Feeling.com.xecoder.service.impl
 */
@Service(value = "messagesService")
public class MessagesService extends AbstractService<Messages> {

    @Autowired
    private MessagesDao messagesDao;

    @Autowired
    private MessagesSecretDao secretDao;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected MongoRepository<Messages, String> getRepository() {
        return messagesDao;
    }

    @Override
    public long count(Messages searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query = makeQuery(criteria);
        return doCount(query, Messages.class);
    }

    @Override
    public List<Messages> search(int page, int size, Sort sort, Messages searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query = makeQuery(criteria);
        query.skip(calcSkipNum(page, size)).limit(size);
        return doFind(query, Messages.class);
    }

    /**
     * 默认搜索20千米范围内无指定人的信息，按位置排序
     *
     * @param page
     * @param size
     * @param point
     * @return
     */
    public List<Messages> search(int page, int size,GeoJsonPoint point) {
        Criteria criteria = Criteria.where("to").is("").and("state").is(1);
        Query query = makeQuery(criteria);
        query.skip(calcSkipNum(page, size)).limit(size);
        List<Messages> list = new ArrayList<>();
        //按经纬度搜索
            NearQuery nq = NearQuery.near(point.getX(), point.getY(), Metrics.KILOMETERS).maxDistance(new Double(200)).query(query);//单位: 20千米
            GeoResults<Messages> empGeoResults = mongoTemplate.geoNear(nq, Messages.class);
            if (empGeoResults != null) {
                for (GeoResult<Messages> e : empGeoResults) {
                    Messages messages = e.getContent();
                    User u = userService.findById(messages.getFromId());
                    if (u != null)
                        messages.setAvatar(ImageUtil.getPathSmall(u.getAvatar()));
                    messages.setDistance(e.getDistance().getValue());
                    messages.setX(messages.getPoint().getX());
                    messages.setY(messages.getPoint().getY());
                    list.add(messages);
                }
            }

        return list;
    }


    /**
     * 默认搜索姓名和手机号码
     *
     * @param user
     * @return
     */
    public List<Messages> searchByNameAndPhone(User user,Point point) {
        DBObject queryObject = new BasicDBObject();
        queryObject.put("toId","$eq:null");
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("to", user.getPhone()));
        values.add(new BasicDBObject("to", user.getNickname()));
        values.add(new BasicDBObject("to", user.getRealname()));
        queryObject.put("$or", values);
        Query query = new BasicQuery(queryObject);
        List<Messages> list = doFind(query, Messages.class);
        for (Messages m : list) {
            User u = userService.findById(m.getFromId());
            if (u != null)
                m.setAvatar(ImageUtil.getPathSmall(u.getAvatar()));
            m.setDistance(SurfaceDistanceUtils.getShortestDistance(m.getPoint(), point));
            m.setX(m.getPoint().getX());
            m.setY(m.getPoint().getY());
        }
        return list;
    }


    @Override
    protected Criteria makeCriteria(Messages model) {
        Criteria criteria = null;
        if (StringUtils.isNotEmpty(model.getFromId())) {
            criteria = makeCriteria(criteria, "fromId",model.getFromId());
        }
        if (StringUtils.isNotEmpty(model.getToId())) {
            criteria = makeCriteria(criteria, "toId",model.getToId());
        }
        return criteria;
    }

    @Override
    public Messages findByPk(Object... keys) {
        return messagesDao.findOne(String.valueOf(keys));
    }

    /**
     * '
     * 验证答案，见逻辑
     *
     * @param id
     * @param answer
     * @return
     */
    public String validate(String id, String answer,String uid) {
        if (StringUtils.isNotBlank(id)) {
            Messages msg = findById(id);
            MessagesSecret m = secretDao.findByMsgId(id);
            if (StringUtils.isBlank(msg.getQuestion()))
                return m.getId();
            else if (StringUtils.isBlank(m.getAnswer()))
                return m.getId();
            else if (StringUtils.equals(m.getAnswer(), answer.trim())) {
                msg.setState(Messages.LOCKED);
                msg.setToId(uid);
                this.save(msg);//锁定，待距离小于0.1KM时发起解锁申请
                return m.getId();
            } else
                return null;
        } else {
            return null;
        }
    }

    /**
     * 获取最终结果
     *
     * @param id
     * @return
     */
    public MessagesSecret getSecretContent(String id) {
        if (StringUtils.isNotBlank(id)) {
            MessagesSecret m = secretDao.findByMsgId(id);
            return m;
        } else {
            return null;
        }
    }

    /**
     * 是否到达，小于0.1千米 则由APP端解锁
     *
     * @param id
     * @param point
     * @return
     */
    public boolean isArrival(String id, Point point) {
        Messages messages = new Messages();
        messages.setId(id);
        //java计算
        messages = this.findById(id);
        Point point1 = new Point(messages.getPoint().getX(), messages.getPoint().getY());
        return SurfaceDistanceUtils.getShortestDistance(point, point1) <= 0.1 ? true : false;//曲面计算

        //mongodb计算
//        Criteria criteria = makeCriteria(messages);
//        Query query = makeQuery(criteria);
//        query.skip(calcSkipNum(0, 1)).limit(1);
//        NearQuery nq = NearQuery.near(x, y, Metrics.KILOMETERS).maxDistance(new Double(20038)).query(query);//地球赤道40076千米
//        GeoResults<Messages> empGeoResults = mongoTemplate.geoNear(nq, Messages.class);
//        if (empGeoResults != null) {
//            for (GeoResult<Messages> e : empGeoResults) {
//                return e.getDistance().getValue()>=0.1?true:false;
//            }
//        }
//        return false;
    }
}

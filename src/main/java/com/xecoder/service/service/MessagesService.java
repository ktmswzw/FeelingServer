package com.xecoder.service.service;

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
    protected long count(Messages searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query = makeQuery(criteria);
        return doCount(query, Messages.class);
    }

    /**
     * 默认搜索20千米范围内信息，按位置排序
     *
     * @param page
     * @param size
     * @param sort
     * @param searchCondition
     * @return
     */
    @Override
    public List<Messages> search(int page, int size, Sort sort, Messages searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
//        Criteria criteria =   new Criteria();

//        Query query = new Query(criteria);
        Query query = makeQuery(criteria);
        query.skip(calcSkipNum(page, size)).limit(size);
        List<Messages> list = new ArrayList<>();
        GeoJsonPoint point = searchCondition.getPoint();
        if (point != null) {//按经纬度搜索
            NearQuery nq = NearQuery.near(point.getX(), point.getY(), Metrics.KILOMETERS).maxDistance(new Double(500)).query(query);//单位: 20千米
            GeoResults<Messages> empGeoResults = mongoTemplate.geoNear(nq, Messages.class);
            if (empGeoResults != null) {
                for (GeoResult<Messages> e : empGeoResults) {
                    Messages messages = e.getContent();
                    User u = userService.findById(messages.getFromId());
                    if(u!=null)
                    messages.setAvatar(StringUtils.isBlank(u.getAvatar())?"": ImageUtil.getPathSmall(u.getAvatar()));
                    messages.setDistance(e.getDistance().getValue());
                    messages.setX(messages.getPoint().getX());
                    messages.setY(messages.getPoint().getY());
                    list.add(messages);
                }
            }
        } else {//按姓名搜索
            if (StringUtils.isNotBlank(searchCondition.getTo()))
                list = doFind(query, Messages.class);
        }
        return list;
    }

    @Override
    protected Criteria makeCriteria(Messages model) {
        Criteria criteria = null;
        if (StringUtils.isNotEmpty(model.getId())) {
            criteria = makeCriteria(criteria, "_id", new ObjectId(model.getId()));
        }
        if (StringUtils.isNotEmpty(model.getTo())) {
//            criteria = makeCriteriaRegex(criteria, "to", "^.*" + model.getTo() + ".*$");//模糊查询
            criteria = makeCriteria(criteria, "to", model.getTo());
        }
        else
        {
            if(criteria==null)
                criteria.where("to").exists(false);
            else
                criteria.and("to").exists(false);
        }
        if (StringUtils.isNotEmpty(model.getFrom())) {
            criteria = makeCriteriaRegex(criteria, "from", "^.*" + model.getFrom() + ".*$");
        }
        criteria = makeCriteria(criteria, "state", Messages.CLOSE);//未被开启过
        criteria.where("limit_date").lte(DateTools.addDay(new Date(), 365)).gt(new Date());//一年有效期内

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
    public String validate(String id, String answer) {
        if (StringUtils.isNotBlank(id)) {
            Messages msg = findById(id);
            MessagesSecret m = secretDao.findByMsgId(id);
            if (StringUtils.isBlank(msg.getQuestion()))
                return m.getId();
            else if (StringUtils.isBlank(m.getAnswer()))
                return m.getId();
            else if (StringUtils.equals(m.getAnswer(), answer.trim())) {
                msg.setState(Messages.LOCKED);
                this.save(msg);//锁定，待距离小于0.1KM时发起解锁申请
                return m.getId();
            }
            else
                return null;
        } else {
            return null;
        }
    }

    /**
     * 获取最终结果
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
        Point point1 = new Point(messages.getPoint().getX(),messages.getPoint().getY());
        return SurfaceDistanceUtils.getShortestDistance(point,point1)<=0.1?true:false;//曲面计算

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

package com.xecoder.service.dao;

import com.xecoder.model.business.Messages;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/27-13:35
 * Feeling.com.xecoder.service.dao
 */
@Repository
public interface MessagesDao extends MongoRepository<Messages,String> {

//    GeoResults<Messages> findByPositionWithin(Circle c);
//
//    //  Point location = new Point(-73.99171, 40.738868);
//    //  NearQuery query = NearQuery.near(location).maxDistance(new Distance(10, Metrics.MILES));
//    //  GeoResults<Restaurant> = operations.geoNear(query, Restaurant.class);
//
//    // No metric: {'geoNear' : 'person', 'near' : [x, y], maxDistance : distance }
//    // Metric: {'geoNear' : 'person', 'near' : [x, y], 'maxDistance' : distance,
//    // 'distanceMultiplier' : metric.multiplier, 'spherical' : true }
//    GeoResults<Messages> findByCoordinate(Point location, Distance distance);
//
//    GeoResults<Messages> findByPositionWithin(Box b);
}


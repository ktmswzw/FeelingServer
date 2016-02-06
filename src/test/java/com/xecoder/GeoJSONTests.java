package com.xecoder;

import com.mongodb.BasicDBObject;
import com.xecoder.common.util.SurfaceDistanceUtils;
import com.xecoder.model.business.Messages;
import com.xecoder.service.impl.MessagesServerImpl;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/28-10:02
 * Feeling.com.xecoder
 */

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FeelingApplication.class)
public class GeoJSONTests {

    @Autowired
    private MongoTemplate template;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private GeoLocationDao geoLocationDao;
    @Autowired
    private MessagesServerImpl server;

    @Test
    public void contextLoads() {

    }

    @Test
    public void init() {
        geoLocationDao.save(new GeoLocation("鹤山市",new Point(112.99206,22.740501)));
//        geoLocationDao.save(new GeoLocation("桂林市",new Point(110.295787,25.288211)));
    }

    /**
     * 创建空间索引
     */
    public void makeSpatialIndexs(){
        template.getCollection("geo_point").createIndex( new BasicDBObject( "point", "2dsphere"));
    }

    @Test
    public  void query()
    {
        Point point = new Point(110.295787, 25.288211);
//        Criteria criteria =  Criteria.where("_id").is(new ObjectId("56a9a7ad6aa4f9d7168ff143"));
        Criteria criteria =   new Criteria();
        Query query = new Query(criteria);
        query.limit(3);
        query.skip(0);
        NearQuery nq = NearQuery.near(point.getX(),point.getY(), Metrics.KILOMETERS).maxDistance(new Double(490)).query(query);
        GeoResults<Messages> empGeoResults = template.geoNear(nq, Messages.class);
        if (empGeoResults != null) {
            List<Messages> empList = new ArrayList<>();
            for (GeoResult<Messages> e : empGeoResults) {
                empList.add(e.getContent());
            }
        }

    }

    @Test
    public void distance()
    {
        Point point = new Point(110.295787, 25.288211);
        GeoLocation geoLocation = template.findById(new  ObjectId("56a9a7ad6aa4f9d7168ff143"),GeoLocation.class);

        if(geoLocation!=null) {
            Point point1 = new Point(geoLocation.getGeoPoint().getX(), geoLocation.getGeoPoint().getY());

            boolean s = SurfaceDistanceUtils.getShortestDistance(point, point1) >= 0.1 ? true : false;

            System.out.println("s = " + s);
        }
    }
}

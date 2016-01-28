package com.xecoder;

import com.mongodb.BasicDBObject;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/28-10:02
 * Feeling.com.xecoder
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FeelingApplication.class)
public class GeoJSONTests {

    @Autowired
    private MongoTemplate template;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private GeoLocationDao geoLocationDao;

    @Test
    public void contextLoads() {

    }

//    @Test
    public void init() {
        geoLocationDao.save(new GeoLocation("鹤山市",new Point(112.99206,22.740501)));
        geoLocationDao.save(new GeoLocation("桂林市",new Point(110.295787,25.288211)));
    }

    /**
     * 创建空间索引
     */
    public void makeSpatialIndexs(){
        template.getCollection("geo_point").createIndex( new BasicDBObject( "point", "2dsphere"));
    }

//    @Test
    public  void query()
    {
        Point point = new Point(110.295787, 25.288211);
        Criteria criteria = new Criteria();// Criteria.where("name").is("皇冠小区");
        Query query = new Query(criteria);
        query.limit(20);
        NearQuery nq = NearQuery.near(point.getX(),point.getY(), Metrics.KILOMETERS).maxDistance(new Double(490));
        GeoResults<GeoLocation> empGeoResults = template.geoNear(nq, GeoLocation.class);
        if (empGeoResults != null) {
            List<GeoLocation> empList = new ArrayList<>();
            for (GeoResult<GeoLocation> e : empGeoResults) {
                empList.add(e.getContent());
            }
        }

    }
}

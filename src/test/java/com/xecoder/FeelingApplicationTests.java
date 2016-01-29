package com.xecoder;

import com.xecoder.service.dao.MessagesDao;
import com.xecoder.service.impl.MessagesServerImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FeelingApplication.class)
public class FeelingApplicationTests {


	@Autowired
	private MongoTemplate template;

	@Autowired
	private MessagesDao messagesDao;

	@Autowired
	private MessagesServerImpl server;

	@Test
	public void contextLoads() {
//		Messages msg = new Messages();
//
//		msg.setFrom("小红");
//		msg.setTo("小明");
//		msg.setContent("我在这里，你在哪里？");
//		msg.setCity("宁波");
//		msg.setDistrict("鄞州");
//		msg.setAddress("学士路655号");
//		msg.setBurnAfterReading(true);

		//double x = server.getDistance("1",0,0);


	}

}




//		db.runCommand(
//				{
//						geoNear: "geography",
//			near: { type: "Point", coordinates: [ 114.028184, 22.541118 ] },
//		spherical: true,
//				maxDistance: 400,
//			query: { name: "皇冠小区" }
//		}
//		)
//		db.runCommand(
//				{
//						geoNear: "geography",
//			near: { type: "Point", coordinates: [110.295787,25.288211 ] },
//		spherical: true,
//				maxDistance: 395000
//		}
//		)
//db.geo_location.ensureIndex( { "geoPoint" : "2dsphere" } )

//		db.habit.ensureIndex( { "geoPoint" : "2dsphere" } )
//		db.port.insert( { name: "Boston", loc : { type : "Point", coordinates : [ 71.0603, 42.3583 ] } })



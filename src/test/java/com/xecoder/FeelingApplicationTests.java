package com.xecoder;

import com.xecoder.common.util.DateTools;
import com.xecoder.model.business.Messages;
import com.xecoder.model.embedded.MessagesPhoto;
import com.xecoder.model.embedded.MessagesSecret;
import com.xecoder.service.dao.MessagesDao;
import com.xecoder.service.dao.MessagesSecretDao;
import com.xecoder.service.service.MessagesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FeelingApplication.class)
public class FeelingApplicationTests {


	@Autowired
	private MongoTemplate template;

	@Autowired
	private MessagesDao messagesDao;

	@Autowired
	private MessagesSecretDao secretDao;


	@Autowired
	private MessagesService server;

	@Test
	public void contextLoads() {

		Messages msg = new Messages();

		MessagesSecret secret = new MessagesSecret();

		msg.setFrom("小红");
		msg.setTo("小明");
		secret.setContent("我在这里，你在哪里？");
		msg.setCity("宁波");
		msg.setDistrict("鄞州");
		msg.setAddress("学士路655号");
		secret.setAnswer("me is who");
		msg.setQuestion("who is me");
		secret.setLimitDate(DateTools.addDay(new Date(),10));
		GeoJsonPoint point = new GeoJsonPoint(112.99206,22.740501);
		msg.setPoint(point);

		MessagesPhoto  messagesPhoto = new MessagesPhoto();
		messagesPhoto.setName("1");
		messagesPhoto.setSource("222");
		messagesPhoto.setThumbnails("111");
		List<MessagesPhoto> list = new ArrayList<>();
		list.add(messagesPhoto);

		secret.setPhotosList(list);

		secret.setBurnAfterReading(true);

		messagesDao.save(msg);

		secret.setMsgId(msg.getId());
		secretDao.save(secret);


		//double x = server.getDistance("1",0,0);

//		List features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
//		features.forEach(n -> System.out.println(n));
//		features.forEach(System.out::println);
//
//		List languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
//
//		Predicate<String> startsWithJ = (n) -> n.startsWith("J");
//		Predicate<String> fourLetterLong = (n) -> n.length() == 4;
//		languages.stream().filter(startsWithJ.and(fourLetterLong)).forEach((n) -> System.out.print("\nName, which starts with 'J' and four letter long is : " + n));
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



package com.xecoder.service.restful;

import com.xecoder.common.exception.HttpServiceException;
import com.xecoder.common.exception.ReturnMessage;
import com.xecoder.common.util.DateTools;
import com.xecoder.model.business.Messages;
import com.xecoder.model.business.User;
import com.xecoder.model.core.NonAuthoritative;
import com.xecoder.model.embedded.MessagesPhoto;
import com.xecoder.service.service.MessagesService;
import com.xecoder.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/28-16:00
 * Feeling.com.xecoder.controller.business
 */
@RestController
@RequestMapping("/messages")
public class MessagesController extends BaseController {
    @Autowired
    private MessagesService server;

    @Autowired
    private UserService userServer;

    /**
     * 查找附近的1年内有效的消息
     * 不需要登陆
     *
     * @param to   接受人
     * @param x    经度
     * @param y    维度
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    @NonAuthoritative
    private ResponseEntity<?> search(@RequestParam(required = false) String to,
                                     @RequestParam(required = false) double x,
                                     @RequestParam(required = false) double y,
                                     @RequestParam int page,
                                     @RequestParam int size
    ) {
        Messages msg = new Messages();
        msg.setTo(to);
        GeoJsonPoint point = new GeoJsonPoint(x, y);
        msg.setPoint(point);
        List<Messages> list = server.search(page, size, new Sort("OrderByLimitDateAsc"), msg);
        if(list!=null&&list.size()!=0)
            return new ResponseEntity<>(list, HttpStatus.OK);
        else
            return new ResponseEntity<>(list, HttpStatus.NOT_FOUND);
    }


    /**
     * 发送消息
     *
     * @param to
     * @param limitDate
     * @param content
     * @param photos
     * @param video
     * @param sound
     * @param burnAfterReading
     * @param x
     * @param y
     * @return
     */
    @SuppressWarnings(value = "unchecked")
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    private ResponseEntity<?> send(@RequestParam String to,
                                        @RequestParam String limitDate,
                                        @RequestParam String content,
                                        @RequestParam(required = false) String question,
                                        @RequestParam(required = false) String answer,
                                        @RequestParam(required = false) String photos,
                                        @RequestParam(required = false) String video,
                                        @RequestParam(required = false) String sound,
                                        @RequestParam(required = false) String burnAfterReading,
                                        @RequestParam double x,
                                        @RequestParam double y
    ) {
        Messages msg = new Messages();
        if (this.getUserId() != null) {
            msg.setFrom(this.getUserId());
            User user = userServer.findById(this.getUserId());
            if (user != null)
                msg.setFrom(user.getNickname());
        }
        msg.setTo(to);
        msg.setLimitDate(DateTools.strToDate(limitDate));
        msg.setContent(content);
//        msg.setPhotos(new ArrayList(Collections.singletonList(photos)));
        List<MessagesPhoto> list = new ArrayList<>();
        List<String> stringList = new ArrayList(Collections.singletonList(photos));
        for(String s: stringList){
            MessagesPhoto p = new MessagesPhoto();
            p.setSource(s);
            list.add(p);
        }
        msg.setPhotos(list);
        msg.setVideoPath(video);
        msg.setQuestion(question);
        msg.setAnswer(answer);
        msg.setSoundPath(sound);
        msg.setBurnAfterReading(Boolean.parseBoolean(burnAfterReading));
        msg.setPoint(new GeoJsonPoint(x, y));
        server.save(msg);
        return new ResponseEntity<>(new ReturnMessage(msg.getId(),HttpStatus.OK), HttpStatus.OK);
    }

    /**
     * 验证答案，答案准确则锁定；（答案、问题）为空，不锁定
     *
     * @param answer
     * @return
     */
    @RequestMapping(value = "validate/{id}/{answer}", method = RequestMethod.GET)
    @ResponseBody
    private ResponseEntity<?> validate(@Valid @PathVariable String id, @Valid @PathVariable String answer) {
        Messages m = server.validate(id, answer);
        if (m == null) {
            throw new HttpServiceException(getLocalException("error.answer.is.error"));
        } else {
            //返回地址信息，进行查找和定位
            Messages n = new Messages();
            n.setCity(m.getCity());
            n.setDistrict(m.getDistrict());
            n.setPoint(m.getPoint());
            n.setAddress(m.getAddress());
            return new ResponseEntity<>(n, HttpStatus.OK);
        }
    }

    /**
     * 是否近距离开启
     * @param id
     * @param x
     * @param y
     * @return
     */
    @RequestMapping(value = "/arrival/{x}/{y}/{id}", method = RequestMethod.GET)
    @ResponseBody
    @NonAuthoritative
    private ResponseEntity<?> isArrival(@PathVariable double x,
                                        @PathVariable double y,
                                        @PathVariable String id
    ) {
        Messages messages;
        if(server.isArrival(id,new Point(x,y))) {
            messages = server.findById(id);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);

    }

    /**
     * 丢弃
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "drop/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    private ResponseEntity<?> drop(@Valid @PathVariable String id) {
        Messages m = server.findById(id);
        switch (m.getState()) {
            case 9:
                return new ResponseEntity<>(getLocalException("error.message.is.deleted"), HttpStatus.ALREADY_REPORTED);
            case 1:
                m.setState(Messages.DELETED);
                server.save(m);
                return new ResponseEntity<>(getLocalException("error.message.is.deleted"), HttpStatus.ACCEPTED);
            default:
                return new ResponseEntity<>(getLocalException("error.message.was.opened"), HttpStatus.ACCEPTED);
        }
    }

}

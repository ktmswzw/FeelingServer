package com.xecoder.restful;

import com.xecoder.common.exception.HttpServiceException;
import com.xecoder.common.exception.ReturnMessage;
import com.xecoder.common.util.DateTools;
import com.xecoder.common.util.ImageUtil;
import com.xecoder.model.business.Messages;
import com.xecoder.model.business.User;
import com.xecoder.model.core.NonAuthoritative;
import com.xecoder.model.embedded.MessagesPhoto;
import com.xecoder.model.embedded.MessagesSecret;
import com.xecoder.service.service.MessagesSecretService;
import com.xecoder.service.service.MessagesService;
import com.xecoder.service.service.UserService;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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
    private MessagesSecretService secretService;

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
                                     @RequestParam(required = false) String x,
                                     @RequestParam(required = false) String y,
                                     @RequestParam int page,
                                     @RequestParam int size
    ) {
        Messages msg = new Messages();
        msg.setTo(to);
        GeoJsonPoint point = new GeoJsonPoint(Double.parseDouble(x), Double.parseDouble(y));
        msg.setPoint(point);
        List<Messages> list = server.search(page, size, new Sort("OrderByLimitDateAsc"), msg);
        if(list!=null&&list.size()!=0)
            return new ResponseEntity<>(list, HttpStatus.OK);
        else
            return new ResponseEntity<>(list, NOT_FOUND);
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
                                        @RequestParam String x,
                                        @RequestParam String y
    ) {
        MessagesSecret secret = new MessagesSecret();
        Messages msg = new Messages();
        if (this.getUserId() != null) {
            msg.setFrom(this.getUserId());
            User user = userServer.findById(this.getUserId());
            if (user != null)
                msg.setFrom(user.getNickname());
        }
        msg.setTo(to);
        msg.setFromId(this.getUserId());
        secret.setLimitDate(DateTools.strToDate(limitDate));
        secret.setContent(content);
        List<MessagesPhoto> list = new ArrayList<>();
        String[] stringList = photos.split(",");
        for(String s: stringList){
            MessagesPhoto p = new MessagesPhoto();
            p.setSource(s);
            list.add(p);
        }
        secret.setPhotosList(list);
        secret.setVideoPath(video);
        msg.setQuestion(question);
        secret.setAnswer(answer);
        secret.setSoundPath(sound);
        secret.setBurnAfterReading(Boolean.parseBoolean(burnAfterReading));
        msg.setPoint(new GeoJsonPoint(Double.parseDouble(x), Double.parseDouble(y)));
        server.save(msg);
        secret.setMsgId(msg.getId());
        secretService.save(secret);

        return new ResponseEntity<>(new ReturnMessage(msg.getId(),HttpStatus.OK), HttpStatus.OK);
    }

    /**
     * 验证答案，答案准确则锁定；（答案、问题）为空，不锁定
     *
     * @param answer
     * @return
     */
    @RequestMapping(value = "validate/{id}", method = RequestMethod.GET)
    @ResponseBody
    @NonAuthoritative
    private ResponseEntity<?> validate(@PathVariable String id, @RequestParam(required = false) String answer) {
        String  sId = server.validate(id, answer);
        if (sId == null) {
            throw new HttpServiceException(getLocalException("error.answer.is.error"));
        } else {
            return new ResponseEntity<>(new ReturnMessage(sId,HttpStatus.OK), HttpStatus.OK);
        }
    }

    /**
     * 是否近距离开启( 服务器认证)
     * @param id
     * @param x
     * @param y
     * @return
     */
    @RequestMapping(value = "/arrival/{x}/{y}/{id}", method = RequestMethod.GET)
    @ResponseBody
    @NonAuthoritative
    private ResponseEntity<?> isArrival(@PathVariable String x,
                                        @PathVariable String y,
                                        @PathVariable String id
    ) {
        if(server.isArrival(id,new Point(Double.parseDouble(y),Double.parseDouble(x)))) {
            MessagesSecret messagesSecret = secretService.findById(id);
            return new ResponseEntity<>(messagesSecret, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(new ReturnMessage(getLocalException("error.destince.is.error"), NOT_FOUND), NOT_FOUND);
    }


    /**
     * 是否近距离开启( 手机端认证)
     * @param id
     * @return
     */
    @RequestMapping(value = "/openOver/{id}", method = RequestMethod.GET)
    @ResponseBody
    @NonAuthoritative
    private ResponseEntity<?> openOver(@PathVariable String id
    ) {
        MessagesSecret messagesSecret = secretService.findById(id);
        if(messagesSecret!=null){
            List<String> photos=  new ArrayList<>();
            if(messagesSecret.getPhotosList()!=null)
                photos.addAll(messagesSecret.getPhotosList().stream().map(MessagesPhoto::getSource).collect(Collectors.toList()));
            if(photos.size()>0)
            messagesSecret.setPhotos(photos.toArray(new String[photos.size()]));
            return new ResponseEntity<>(messagesSecret, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(new ReturnMessage(getLocalException("error.destince.is.error"),HttpStatus.NOT_FOUND),NOT_FOUND);
    }

    /**
     * 丢弃
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "drop/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    private ResponseEntity<?> drop(@PathVariable String id) {
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

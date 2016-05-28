package com.xecoder.restful;

import com.xecoder.common.exception.HttpServiceException;
import com.xecoder.common.exception.ReturnMessage;
import com.xecoder.common.util.DateTools;
import com.xecoder.common.util.ImageUtil;
import com.xecoder.common.util.StringUtilsSelf;
import com.xecoder.common.util.mobile.MobileProviderFactory;
import com.xecoder.dao.AuthDao;
import com.xecoder.model.business.*;
import com.xecoder.model.core.NonAuthoritative;
import com.xecoder.model.embedded.MessagesPhoto;
import com.xecoder.model.embedded.MessagesSecret;
import com.xecoder.service.service.*;
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
import java.util.Date;
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

    @Autowired
    private AuthDao authDao;

    @Autowired
    private TryHistoryService tryService;

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
    private ResponseEntity<?> search(@RequestParam(required = false) String to,
                                     @RequestParam(required = false) String x,
                                     @RequestParam(required = false) String y,
                                     @RequestParam int page,
                                     @RequestParam int size
    ) {
        List<Messages> resultList = new ArrayList<>();
        List<Messages> list = null;
        User user = null;
        GeoJsonPoint point = new GeoJsonPoint(Double.parseDouble(x), Double.parseDouble(y));
        if (this.getUserId() != null) {
            user = userServer.findById(this.getUserId());
        }
        if (user != null) {
            list = server.searchByNameAndPhone(user, point);//附近个人相关的信息
        }
        if (list != null && list.size() != 0) {
            for(Messages messages:list){
                messages.setType(1);
                resultList.add(messages);
            }
        }
        //搜索周围的数据
        list = server.search(page, size, point);//附近20公里无名消息
        if (list != null && list.size() != 0) {
            for(Messages messages:list){
                messages.setType(0);
                resultList.add(messages);
            }
        }
        if (resultList != null && resultList.size() != 0)
            return new ResponseEntity<>(resultList, HttpStatus.OK);
        else
            return new ResponseEntity<>(resultList, NOT_FOUND);
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
                                   @RequestParam(required = false) String limitDate,
                                   @RequestParam String content,
                                   @RequestParam(required = false) String question,
                                   @RequestParam(required = false) String answer,
                                   @RequestParam(required = false) String photos,
                                   @RequestParam(required = false) String video,
                                   @RequestParam(required = false) String sound,
                                   @RequestParam(required = false) String burnAfterReading,
                                   @RequestParam(required = false) String address,
                                   @RequestParam String x,
                                   @RequestParam String y
    ) {
        MessagesSecret secret = new MessagesSecret();
        Messages msg = new Messages();
        if (this.getUserId() != null) {
            User user = userServer.findById(this.getUserId());
            msg.setFrom(user.getNickname());
        }
        msg.setTo(to);
        msg.setAddress(address);
        msg.setFromId(this.getUserId());
        secret.setLimitDate(DateTools.addDay(new Date(), 10000));
        secret.setContent(content);
        List<MessagesPhoto> list = new ArrayList<>();
        String[] stringList = photos.split(",");
        for (String s : stringList) {
            MessagesPhoto p = new MessagesPhoto();
            p.setSource(s);
            list.add(p);
        }
        secret.setPhotosList(list);
        secret.setVideoPath(video);
        msg.setQuestion(question);
        msg.setAnswerTip(StringUtilsSelf.setAnswerTip(answer));
        secret.setAnswer(answer);
        secret.setSoundPath(sound);
        secret.setBurnAfterReading(Boolean.parseBoolean(burnAfterReading));
        msg.setPoint(new GeoJsonPoint(Double.parseDouble(x), Double.parseDouble(y)));
        server.save(msg);
        secret.setMsgId(msg.getId());
        secretService.save(secret);
        if(StringUtils.isNotBlank(to)){
            sendMsg(to);
        }
        return new ResponseEntity<>(new ReturnMessage(msg.getId(), HttpStatus.OK), HttpStatus.OK);
    }

    /**
     * 发送推送
     * @param to
     */
    private void sendMsg(String to){
        MobileProviderFactory mobileProviderFactory = new MobileProviderFactory();
        try {
            long phone = Long.parseLong(to);
            if(mobileProviderFactory.isValidPhone(phone)){
                User user = userServer.findByPhone(to);
                if(user!=null) {
                    run(user.getId());
                }
            }
        }
        catch (Exception e)
        {
            List<User> users = userServer.findByName(to);
            if(users!=null){
                for(User u:users){
                    run(u.getId());
                }
            }
        }
    }

    private void run(String id)
    {
        Auth auth = authDao.findByOwner(id);
        if (auth != null) {
            List<AuthToken> authTokens = auth.getEffectiveTokens();
            authTokens.stream().filter(authToken -> StringUtils.isNotBlank(authToken.getDeviceToken())).forEach(authToken -> {
                APNsPushMessage apNsPushMessage = new APNsPushMessage();
                apNsPushMessage.setToken(authToken.getDeviceToken());
                apNsPushMessage.setBadge(1);
                apNsPushMessage.setMessage("你收到一封FEELING");
                apNsPushMessage.run();
            });
        }
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
        String sId = server.validate(id, answer,this.getUserId());
        TryHistory tryHistory = new TryHistory();
        tryHistory.setMessageId(id);
        tryHistory.setTryDate(new Date());
        tryHistory.setTryAnswer(answer);
        tryService.save(tryHistory);
        if (sId == null) {
            throw new HttpServiceException(getLocalException("error.answer.is.error"));
        } else {
            return new ResponseEntity<>(new ReturnMessage(sId, HttpStatus.OK), HttpStatus.OK);
        }
    }

    /**
     * 是否近距离开启( 服务器认证)
     *
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
        if (server.isArrival(id, new Point(Double.parseDouble(y), Double.parseDouble(x)))) {
            MessagesSecret messagesSecret = secretService.findById(id);
            return new ResponseEntity<>(messagesSecret, HttpStatus.OK);
        } else
            return new ResponseEntity<>(new ReturnMessage(getLocalException("error.destince.is.error"), NOT_FOUND), NOT_FOUND);
    }


    /**
     * 是否近距离开启( 手机端认证)
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/openOver/{id}", method = RequestMethod.GET)
    @ResponseBody
    @NonAuthoritative
    private ResponseEntity<?> openOver(@PathVariable String id
    ) {
        MessagesSecret messagesSecret = secretService.findById(id);
        if (messagesSecret != null) {
            List<String> photos = new ArrayList<>();
            if (messagesSecret.getPhotosList() != null)
                photos.addAll(messagesSecret.getPhotosList().stream().map(MessagesPhoto::getSource).collect(Collectors.toList()));
            if (photos.size() > 0)
                messagesSecret.setPhotos(photos.toArray(new String[photos.size()]));
            return new ResponseEntity<>(messagesSecret, HttpStatus.OK);
        } else
            return new ResponseEntity<>(new ReturnMessage(getLocalException("error.destince.is.error"), HttpStatus.NOT_FOUND), NOT_FOUND);
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


    /**
     * 查询自己的收件箱和发件箱
     *
     * @param self
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/self/{self}", method = RequestMethod.POST)
    @ResponseBody
    private ResponseEntity<?> self(@PathVariable boolean self,
                                   @RequestParam int page,
                                   @RequestParam int size
    ) {
        Messages msg = new Messages();
        User user = userServer.findById(this.getUserId());
        if (user != null) {
            if (self) {
                msg.setFromId(this.getUserId());
            } else
                msg.setToId(this.getUserId());
        }
        List<Messages> list = server.search(page, size, new Sort("OrderByCreateDateAsc"), msg);
        TryHistory tryBean = new TryHistory();
        for (Messages bean : list) {
            MessagesSecret messagesSecret = secretService.findByMsgId(bean.getId());
            if(messagesSecret!=null)
            {
                bean.setContent(messagesSecret.getContent());
            }
            if(self) {//自己发出被破解的次数,破解者的头像
                tryBean.setMessageId(bean.getId());
                bean.setTryCount(tryService.count(tryBean));
                if(bean.getToId()!=null) {
                    User userTo = userServer.findById(bean.getToId());
                    bean.setAvatar(ImageUtil.getPathSmall(userTo.getAvatar()));
                    bean.setFrom(userTo.getNickname());
                }
            }
            else {//自己收到的信件,发出者信息
                bean.setTryCount(0);
                if(bean.getFromId()!=null) {
                    User userFrom = userServer.findById(bean.getFromId());
                    bean.setAvatar(ImageUtil.getPathSmall(userFrom.getAvatar()));
                    bean.setFrom(userFrom.getNickname());
                }
            }
        }
        if (list != null && list.size() != 0)
            return new ResponseEntity<>(list, HttpStatus.OK);
        else
            return new ResponseEntity<>(list, NOT_FOUND);
    }
}

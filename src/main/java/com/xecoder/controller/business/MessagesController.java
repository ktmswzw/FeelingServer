package com.xecoder.controller.business;

import com.xecoder.common.exception.FeelingException;
import com.xecoder.controller.core.BaseController;
import com.xecoder.model.business.Messages;
import com.xecoder.model.business.User;
import com.xecoder.model.core.NoAuth;
import com.xecoder.service.impl.MessagesServerImpl;
import com.xecoder.service.impl.UserServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    private MessagesServerImpl server;

    @Autowired
    private UserServerImpl userServer;

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
    @RequestMapping(value = "/{to}/{x}/{y}/{page}/{size}", method = RequestMethod.GET)
    @ResponseBody
    @NoAuth
    private ResponseEntity<?> search(@PathVariable String to,
                                     @Valid @PathVariable double x,
                                     @Valid @PathVariable double y,
                                     @Valid @PathVariable int page,
                                     @Valid @PathVariable int size
    ) {
        Messages msg = new Messages();
        msg.setTo(to);
        GeoJsonPoint point = new GeoJsonPoint(x, y);
        msg.setCoordinate(point);
        return new ResponseEntity<>(server.search(page, size, new Sort("OrderByLimitDateAsc"), msg), HttpStatus.OK);
    }


    /**
     * 发送消息
     *
     * @param msg
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    private ResponseEntity<String> send(@Valid @RequestBody Messages msg) {
        msg.setState(Messages.CLOSE);
        msg.setFrom(this.getUserId());
        User user = userServer.findById(this.getUserId());
        msg.setFrom(user.getNickname());
        server.save(msg);
        return new ResponseEntity<>(getLocalException("success.action.is.ok") + "_" + msg.getId(), HttpStatus.OK);
    }

    /**
     * 验证答案，答案准确则锁定；（答案、问题）为空，不锁定
     *
     * @param answer
     * @return
     */
    @RequestMapping(value = "validate/{id}/{answer}", method = RequestMethod.POST)
    private ResponseEntity<?> validate(@Valid @PathVariable String id, @Valid @PathVariable String answer) {
        Messages m = server.validate(id, answer);
        if (m == null) {
            throw new FeelingException(getLocalException("error.answer.is.error"));
        } else {
            //返回地址信息，进行查找和定位
            Messages n = new Messages();
            n.setCity(m.getCity());
            n.setDistrict(m.getDistrict());
            n.setCoordinate(m.getCoordinate());
            n.setAddress(m.getAddress());
            return new ResponseEntity<>(n, HttpStatus.OK);
        }
    }

    /**
     * 丢弃
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "drop/{id}", method = RequestMethod.DELETE)
    private ResponseEntity<?> drop(@Valid @PathVariable String id) {
        Messages m = server.findById(id);
        switch (m.getState()) {
            case 9:
                return new ResponseEntity<>(getLocalException("error.message.is.deleted"), HttpStatus.ALREADY_REPORTED);
            case 1 :
                m.setState(Messages.DELETED);
                server.save(m);
                return new ResponseEntity<>(getLocalException("error.message.is.deleted"), HttpStatus.ACCEPTED);
            default:
                return new ResponseEntity<>(getLocalException("error.message.was.opened"), HttpStatus.ACCEPTED);
        }
    }

}

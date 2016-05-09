package com.xecoder.restful;

import com.xecoder.common.exception.HttpServiceException;
import com.xecoder.common.exception.ReturnMessage;
import com.xecoder.common.util.ImageUtil;
import com.xecoder.model.embedded.DeviceEnum;
import com.xecoder.model.business.User;
import com.xecoder.model.core.NonAuthoritative;
import com.xecoder.model.embedded.Sex;
import com.xecoder.service.service.ImageSignService;
import com.xecoder.service.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    UserService userServer;

    @Autowired
    ImageSignService signService;

    /**
     * APP注册接口
     *
     * @param username  手机号
     * @param password   密码
     * @param device     设备，APP ,WEB
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    @NonAuthoritative
    public ResponseEntity<?> register(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) DeviceEnum device) {
        return new ResponseEntity<>(userServer.register(username, password, device), HttpStatus.OK);
    }

    /**
     * APP重置接口
     *
     * @param username  手机号
     * @param password   密码
     * @param device     设备，APP ,WEB
     * @return
     */
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    @NonAuthoritative
    public ResponseEntity<?> reset(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) DeviceEnum device) {
        return new ResponseEntity<>(userServer.reset(username, password, device), HttpStatus.OK);
    }


    /**
     * 获取用户列表
     * @param name
     * @return
     */
    @RequestMapping(value = "/lists/{name}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getAllUserList(@PathVariable String name) {
        User user = new User();
        user.setNickname(name);
        List<User> userList  = userServer.search(0,20, new Sort("OrderByCreateTimeAsc"),user);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }



    /**
     * 获取用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getUser(@PathVariable String id) {
        User user = userServer.findById(id);
        if(user!=null) {
            String avatar = ImageUtil.getPathSmall(user.getAvatar());
            user.setAvatar(avatar);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        else
        {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }
    }

    /**
     * 保存用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> saveUser(@PathVariable String id,@RequestParam String nickname,@RequestParam String avatar,@RequestParam Sex sex,@RequestParam String motto) {
        User user = userServer.findById(id);
        if(user!=null) {
            user.setAvatar(StringUtils.isNotBlank(avatar)?avatar:user.getAvatar());
            user.setNickname(StringUtils.isNotBlank(nickname)?nickname:user.getNickname());
            user.setMotto(StringUtils.isNotBlank(motto)?motto:user.getMotto());
            user.setSex(sex!=null?sex:user.getSex());
            userServer.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        else
        {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }
    }

    /**
     * 获取图片鉴权签名
     * @return
     */
    @RequestMapping(value = "/imageSign", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getImageSign() {
        return new ResponseEntity<>(new ReturnMessage(signService.getQSign()), HttpStatus.OK);
    }


    /**
     * 更新设备编号
     * @return
     */
    @RequestMapping(value = "/updateDeviceToken/{deviceToken}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<?> updateDeviceToken(@PathVariable String deviceToken) {
        userServer.updateDeviceToken(this.getUserId(),deviceToken);
        return new ResponseEntity<>(new ReturnMessage("ok"), HttpStatus.OK);
    }



}

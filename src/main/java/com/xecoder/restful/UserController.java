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
    public ResponseEntity<?> register(@Valid @RequestParam String username,@Valid  @RequestParam String password, @RequestParam(required = false) DeviceEnum device) {
        ReturnMessage returnMessage = new ReturnMessage(userServer.register(username, password, device),HttpStatus.OK);
        return new ResponseEntity<>(returnMessage, HttpStatus.OK);
    }


    /**
     * 获取用户列表
     * @param name
     * @return
     */
    @RequestMapping(value = "/lists/{name}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getAllUserList(@Valid @PathVariable String name) {
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
    public ResponseEntity<?> getUser(@Valid @PathVariable String id) {
        User user = userServer.findById(id);
        if(user!=null) {
            String avatar = StringUtils.isBlank(user.getAvatar())?"": ImageUtil.getPathSmall(user.getAvatar());
            user.setAvatar(avatar);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        else
        {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }
    }

    /**
     * 获取用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<?> saveUser(@Valid @PathVariable String id,@Valid @RequestParam String nickname,@RequestParam String avatar,@Valid @RequestParam Sex sex,@Valid @RequestParam String phone) {
        User user = userServer.findById(id);
        if(user!=null) {
            user.setAvatar(StringUtils.isNotBlank(avatar)?avatar:user.getAvatar());
            user.setNickname(StringUtils.isNotBlank(nickname)?nickname:user.getNickname());
            user.setPhone(StringUtils.isNotBlank(phone)?phone:user.getPhone());
            user.setSex(StringUtils.isNotBlank(sex.toString())?sex:user.getSex());
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

}

package com.xecoder.controller.business;

import com.xecoder.common.exception.ReturnMessage;
import com.xecoder.controller.core.BaseController;
import com.xecoder.model.embedded.DeviceEnum;
import com.xecoder.model.business.User;
import com.xecoder.model.core.NonAuthoritative;
import com.xecoder.service.impl.UserServerImpl;
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
    UserServerImpl userServer;

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

}

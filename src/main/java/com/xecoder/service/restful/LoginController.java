package com.xecoder.service.restful;

import com.xecoder.common.exception.ReturnMessage;
import com.xecoder.model.core.NonAuthoritative;
import com.xecoder.model.embedded.DeviceEnum;
import com.xecoder.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/25-15:32
 * Feeling.com.xecoder.controller.business
 */
@RestController
public class LoginController extends BaseController {

    @Autowired
    UserService userServer;
    /**
     * 登录接口
     *
     * @param username 手机号
     * @param password  密码
     * @param device    设备，APP ,WEB
     * @return 用户信息、token
     */
    @RequestMapping(value = "login")
    @ResponseBody
    @NonAuthoritative
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) DeviceEnum device) {
        String token = userServer.login(username, password, device, this.getVersionStr()).getToken();
        return new ResponseEntity<>(new ReturnMessage(token,HttpStatus.OK), HttpStatus.OK);
    }
}

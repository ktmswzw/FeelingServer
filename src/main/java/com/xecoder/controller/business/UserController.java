package com.xecoder.controller.business;

import com.xecoder.controller.core.BaseController;
import com.xecoder.model.business.DeviceEnum;
import com.xecoder.model.core.NoAuth;
import com.xecoder.service.impl.UserServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    UserServerImpl userServer;



     // @param telephone  手机号 password   密码  device     设备，APP ,WEB
    /*
   public ResponseEntity<String> register(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userServer.register(user.getPhone(), user.getPassword(), user.getDevice()), HttpStatus.OK);
    }*/

    /**
     * APP注册接口
     *
     * @param telephone  手机号
     * @param password   密码
     * @param device     设备，APP ,WEB
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    @NoAuth
    public ResponseEntity<String> register(@Valid @RequestParam String telephone,@Valid  @RequestParam String password, @RequestParam DeviceEnum device) {
        return new ResponseEntity<>(userServer.register(telephone, password, device), HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }

/*
    @RequestMapping(value = "exceptions/validate", method = RequestMethod.POST)
    public ResponseEntity<?> addValidObject(@Valid @RequestBody ValidateObject validateObject) {
        HttpHeaders headers = new HttpHeaders();
        URI newUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(validateObject.getId()).toUri();
        headers.setLocation(newUri);

        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }*/
}

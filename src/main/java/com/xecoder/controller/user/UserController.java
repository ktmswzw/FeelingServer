package com.xecoder.controller.user;

import com.xecoder.model.business.DeviceEnum;
import com.xecoder.model.business.User;
import com.xecoder.model.core.NoAuth;
import com.xecoder.service.impl.UserServerImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServerImpl userServer;

    private final Map<String, List<String>> userDb = new HashMap<>();

    public UserController() {
        userDb.put("ktm", Arrays.asList("user"));
        userDb.put("test", Arrays.asList("user", "admin"));
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final User user)
        throws ServletException {
        if (user.getPhone() == null || !userDb.containsKey(user.getPhone())) {
            throw new ServletException("Invalid login");
        }
        return new LoginResponse(Jwts.builder().setSubject(user.getPhone())
            .claim("body", userDb.get(user.getPhone())).setExpiration(new Date()).setIssuedAt(DateUtils.addMinutes(new Date(),1))
            .signWith(SignatureAlgorithm.HS256, "FEELING").compact());
    }


    /**
     * APP注册接口
     *
     * @param telephone  手机号
     * @param password   密码
     * @param device     设备，APP ,WEB
     * @return
     */
    @RequestMapping(value = "/register")
    @ResponseBody
    @NoAuth
    public String register(@RequestParam String telephone, @RequestParam String password,  @RequestParam DeviceEnum device) {
        return userServer.register(telephone, password, device);
    }

    @SuppressWarnings("unused")
    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }
}

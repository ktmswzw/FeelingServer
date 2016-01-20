package com.xecoder.controller.business;

import com.xecoder.controller.core.BaseController;
import com.xecoder.model.business.Greeting;
import com.xecoder.service.core.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/8-15:59
 * Feeling.com.xecoder.controller
 */
@RestController
public class GreetingController extends BaseController {

    @RequestMapping("/")
    @ResponseBody
    public String hello(){
        return "Hello，Spring Boot ！";
    }


    @Autowired
    private RedisService redisService;

    private static final String template = "Hello, %s! %s %s %s";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/greeting",method = RequestMethod.GET)
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        Greeting greeting = form(Greeting.class);
        greeting.setId(counter.incrementAndGet());
        greeting.setContent(String.format(template, name,name,name,name));
        return greeting;
    }


    @RequestMapping(value = "/reids",method = RequestMethod.POST)
    public ResponseEntity<String> redis(@RequestParam(value="key") String key, @RequestParam(value="value") String value) {
        redisService.setValue(key,value);
        System.out.println("key = " + key);
        return new ResponseEntity<>("set key/value successfully", HttpStatus.OK);
    }


    @RequestMapping("/detect-device")
    public @ResponseBody String detectDevice(Device device) {
        String deviceType = "unknown";
        if (device.isNormal()) {
            deviceType = "normal";
        } else if (device.isMobile()) {
            deviceType = "mobile";
        } else if (device.isTablet()) {
            deviceType = "tablet";
        }
        return "Hello " + deviceType + " browser!";
    }



}

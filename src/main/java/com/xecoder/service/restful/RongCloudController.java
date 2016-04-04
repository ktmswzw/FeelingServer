package com.xecoder.service.restful;

import com.xecoder.FeelingApplication;
import com.xecoder.common.exception.HttpServiceException;
import com.xecoder.common.exception.ReturnMessage;
import com.xecoder.model.business.User;
import com.xecoder.model.core.NonAuthoritative;
import com.xecoder.model.rongcloud.*;
import com.xecoder.service.service.ApiHttpService;
import com.xecoder.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 16/4/4.
 */

@RestController
public class RongCloudController extends BaseController {
    private final static String KEY = "25wehl3uwkppw";
    private final static String SECRET = "9ZOJgx4Nlc4";

    SdkHttpResult result;
    @Autowired
    UserService userServer;

    /**
     * 获取TOKEN
     * @return String token
     */
    @RequestMapping(value = "/IMToken", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?>  getIMToken(){
        User user = userServer.findById(this.getUserId());
        if(user!=null) {
            return new ResponseEntity<>(new ReturnMessage(this.getIMK(this.getUserId(),user.getNickname(),user.getAvatar()), HttpStatus.OK), HttpStatus.OK);
        }
        else
        {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }

    }

    public static void main(String[] args) {
        SdkHttpResult result;
        try {
            result = ApiHttpService.getToken(KEY, SECRET, "123456", "abc", "http://aa.com/a.png", FormatType.json);
            System.out.println("result = " + result);
            TokenJson json = (TokenJson) GsonUtil.fromJson(result.getResult().toString(),TokenJson.class);
            System.out.println("json = " + json);
        } catch (Exception e) {

        }
    }

    public String getIMK(String userId,String name,String avatar){
        try {
            result = ApiHttpService.getToken(KEY, SECRET, userId, name, this.getQcloudUrl(avatar), FormatType.json);

            if(result.getHttpCode()!=200){
                throw new HttpServiceException(getLocalException("error.user.not.register"));
            }

            TokenJson json = (TokenJson) GsonUtil.fromJson(result.getResult().toString(),TokenJson.class);
            return json.getToken();
        } catch (Exception e) {
            throw new HttpServiceException(getLocalException("error.answer.is.error"));
        }
    }

    @RequestMapping(value = "/publishMessage", method = RequestMethod.GET)
    @ResponseBody
    public String publishMessage(String message,String toId){
        User user = userServer.findById(toId);
        if(user!=null) {
            try {
                List<String> toIds = new ArrayList<>();
                toIds.add(toId);
                result = ApiHttpService.publishMessage(KEY, SECRET, this.getUserId(), toIds, new TxtMessage(message), FormatType.json);
            } catch (Exception e) {
                throw new HttpServiceException(getLocalException("error.answer.is.error"));
            }
        }
        else
        {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }
        return result.getResult();
    }

}

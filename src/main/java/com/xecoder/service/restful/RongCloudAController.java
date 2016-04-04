package com.xecoder.service.restful;

import com.alibaba.fastjson.JSONObject;
import com.xecoder.common.exception.HttpServiceException;
import com.xecoder.model.business.User;
import com.xecoder.model.core.NonAuthoritative;
import com.xecoder.model.rongcloud.FormatType;
import com.xecoder.model.rongcloud.SdkHttpResult;
import com.xecoder.model.rongcloud.TxtMessage;
import com.xecoder.service.service.ApiHttpService;
import com.xecoder.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RongCloudAController extends BaseController {
    private final static String KEY = "25wehl3uwkppw";
    private final static String SECRET = "9ZOJgx4Nlc4";

    SdkHttpResult result;
    @Autowired
    UserService userServer;

    /**
     * 获取TOKEN
     * @return String token
     */
    @RequestMapping(value = "/getIMToken", method = RequestMethod.GET)
    @ResponseBody
    public String getIMToken(){
        User user = userServer.findById(this.getUserId());
        if(user!=null) {
            try {
                result = ApiHttpService.getToken(KEY, SECRET, this.getUserId(), user.getNickname(), this.getQcloudUrl(user.getAvatar()), FormatType.json);
            } catch (Exception e) {
                throw new HttpServiceException(getLocalException("error.answer.is.error"));
            }
        }
        else
        {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }

        JSONObject jsb = JSONObject.parseObject((JSONObject.parseObject(result.toString()).get("result").toString()));
        return jsb.get("token").toString();
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

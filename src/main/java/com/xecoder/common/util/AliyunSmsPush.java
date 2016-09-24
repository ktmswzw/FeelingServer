package com.xecoder.common.util;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;


/**
 * Created by vincent on 16/9/24.
 * Duser.name = 224911261@qq.com
 * Feeling
 */
public class AliyunSmsPush {


    public static boolean sendSms(String phone,String templateCode, String jsonString) {
        TaobaoClient client = new DefaultTaobaoClient(SecurityConstants.SMS_URL, SecurityConstants.SMS_KEY, SecurityConstants.SMS_SECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("1");
        req.setSmsType("normal");
        req.setSmsFreeSignName(SecurityConstants.SMSFREESIGNNAME);
        req.setRecNum(phone);
        req.setSmsTemplateCode(templateCode);
        req.setSmsParamString(jsonString);
        try {
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            if(rsp.getResult()!=null&&rsp.getResult().getSuccess()) {
                return true;
            }
            else {
                return false;
            }
        } catch (ApiException e) {
            return false;
        }
    }
//
//    public static void main(String[] strings){
//
//        JSONObject object = new JSONObject();
//        object.put("subAgent", "test2");
//        object.put("agent", "test3");
//        sendSms("15869100507", "SMS_14771654", object.toJSONString());
//    }
}

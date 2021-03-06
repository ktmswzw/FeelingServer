package com.xecoder.service.service;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.EnhancedApnsNotification;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;

/**
 * Created by vincent on 16/5/9.
 */
public class APNsPushMessage implements Runnable {

    private final static ApnsService service = APNS.newService()
            .withCert(APNsPushMessage.class.getResourceAsStream("/" + "Feeling.p12"), "123qwe$%^<>?")
            .withSandboxDestination()
            .build();

    private String message;
    private String token;
    private int badge;

    public ApnsService getService() {
        return service;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    //    public static void main(String[] args) {
//        APNsPushMessage.sendMsg("111","873308653a342ecaad8581a6bb80937732cb867ed9c5d83fb25b3fa3ca83760c",8);
//}

    public static void sendMsg(String msg,String token,int badge)
    {
        String payload = APNS.newPayload()
                .badge(badge)
                .alertBody(msg)
                .sound("default")
                .forNewsstand()
                .build();

        int now =  (int)(new Date().getTime()/1000);

        EnhancedApnsNotification notification = new EnhancedApnsNotification(EnhancedApnsNotification.INCREMENT_ID(),
                now + 60 * 60 /* Expire in one hour */,
                token /* Device Token */,
                payload);

        service.push(notification);
    }

    @Override
    public void run() {
        sendMsg(this.message,this.token,this.badge);
    }

//    public String getDerviceToken()
//    {
//        Map<String, Date> inactiveDevices = service.getInactiveDevices();
//        for (String deviceToken : inactiveDevices.keySet()) {
//            Date inactiveAsOf = inactiveDevices.get(deviceToken);
//
//        }
//    }
}

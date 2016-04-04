package com.xecoder.service.service;

import com.xecoder.common.util.ImageUtil;
import com.xecoder.service.core.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/2/20-11:41
 * Feeling.com.xecoder.service.service
 */
@Service(value = "imageSignService")
public class ImageSignService  {

    @Autowired
    private RedisService redisService;

    private final static String KEY = "QCloud_Sign";

    public static long EXPIRED_TIME = 24 * 3600 ; //24小时

    public String getQSign()
    {
        String sign = "";
        try {
            sign = redisService.getValue(KEY);
            if (sign == null || sign.equals("")) {
                sign = ImageUtil.getAppSign();
                redisService.setValue(KEY, sign);
                redisService.expire(KEY, EXPIRED_TIME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }
}

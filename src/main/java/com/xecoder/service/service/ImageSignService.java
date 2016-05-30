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
    private final static String FILE = "QCloud_file_Sign";

    public static long EXPIRED_TIME = 24 * 3600 * 30 ; //24小时

    public String getQSign()
    {
        String sign = "",sign_file="";
        try {
            sign = redisService.getValue(KEY);
            if (sign == null || sign.equals("")) {
                sign = ImageUtil.getAppSign();
                sign_file = ImageUtil.getFileSign();
                redisService.setValue(KEY, sign+","+sign_file);
                redisService.expire(KEY, EXPIRED_TIME);
                sign += ","+sign_file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }
}

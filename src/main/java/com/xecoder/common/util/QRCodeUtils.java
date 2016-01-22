package com.xecoder.common.util;

import com.xecoder.model.business.ImageBackup;
import net.glxn.qrgen.javase.QRCode;

import java.io.ByteArrayOutputStream;

/**
 * Created by yanglu
 */
public class QRCodeUtils {

    public static byte[] generateQRCode(String url) {
        ByteArrayOutputStream stream = QRCode.from(url).stream();
        return stream.toByteArray();
    }

    public static void main(String[] args) {
        byte[] file = generateQRCode("http://www.baidu.com");
        String sign = "YpbysMpVwJupDcJ7S8YMrMY8LbxhPTEwMDA1OTk3JmI9aGFiaXQmaz1BS0lEU1dXRVZIcDAydmd0cEZ0U2E0b0ZhbEhLbTRWNFA1SWEmZT0xNDQ5OTcyNjgxJnQ9MTQ0NzM4MDY4MSZyPTg3MjU5NjAwNSZ1PTAmZj0";
        ImageBackup imageBackup = ImageUtil.upload(file, sign);
        System.out.println(imageBackup.getDomain() + " " + imageBackup.getPath());
    }
}

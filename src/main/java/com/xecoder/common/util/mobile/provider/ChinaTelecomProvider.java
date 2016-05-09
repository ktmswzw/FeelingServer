package com.xecoder.common.util.mobile.provider;

import com.xecoder.common.util.mobile.PhoneRange;

/**
 * Created by yanglu
 */
public class ChinaTelecomProvider extends MobileProvider{

    /**
     * 新电信 　　 （中国电信 <http://baike.baidu.com/view/3214.htm>+中国卫通）手机号码开头数字
     * 133、153、189、180、181
     */
    private static PhoneRange[] PHONE_RANGE = new PhoneRange[]{
            new PhoneRange("133"),
            new PhoneRange("153"),
            new PhoneRange("177"),
            new PhoneRange("189"),
            new PhoneRange("180"),
            new PhoneRange("181")
    };

    @Override
    public String getName() {
        return "ChinaTelecom";
    }

    @Override
    public PhoneRange[] getPhoneNumRange() {
        return PHONE_RANGE;
    }
}

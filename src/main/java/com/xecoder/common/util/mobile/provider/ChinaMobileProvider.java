package com.xecoder.common.util.mobile.provider;

import com.xecoder.common.util.mobile.PhoneRange;

/**
 * Created by yanglu
 */
public class ChinaMobileProvider extends MobileProvider {

    /**
     * 新移动 　　 （中国移动+中国铁通）手机号段
     * 134、135、136、137、138、139、147、150、151、152、157、158、159、182、183、187、188
     */
    private static PhoneRange[] PHONE_RANGE = new PhoneRange[]{
            new PhoneRange("134"),
            new PhoneRange("135"),
            new PhoneRange("136"),
            new PhoneRange("137"),
            new PhoneRange("138"),
            new PhoneRange("139"),
            new PhoneRange("147"),
            new PhoneRange("150"),
            new PhoneRange("151"),
            new PhoneRange("152"),
            new PhoneRange("157"),
            new PhoneRange("158"),
            new PhoneRange("159"),
            new PhoneRange("178"),
            new PhoneRange("182"),
            new PhoneRange("183"),
            new PhoneRange("184"),
            new PhoneRange("187"),
            new PhoneRange("188")
    };

    @Override
    public String getName() {
        return "ChinaMobile";
    }

    @Override
    public PhoneRange[] getPhoneNumRange() {
        return PHONE_RANGE;
    }


}

package com.xecoder.common.util.mobile.provider;

import com.xecoder.common.util.mobile.PhoneRange;

/**
 * Created by yanglu
 */
public class ChinaUnicomProvider extends MobileProvider {
    /**
     * 中国联通（中国联通+中国网通) 手机号段 130、131、132、145、155、156、185、186
     */
    private static PhoneRange[] PHONE_RANGE = new PhoneRange[]{
            new PhoneRange("130"),
            new PhoneRange("131"),
            new PhoneRange("132"),
            new PhoneRange("145"),
            new PhoneRange("155"),
            new PhoneRange("156"),
            new PhoneRange("176"),
            new PhoneRange("185"),
            new PhoneRange("186")
    };

    @Override
    public String getName() {
        return "ChinaUnicom";
    }

    @Override
    public PhoneRange[] getPhoneNumRange() {
        return PHONE_RANGE;
    }
}

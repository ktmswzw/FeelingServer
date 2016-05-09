package com.xecoder.common.util.mobile.provider;

import com.xecoder.common.util.mobile.PhoneRange;

/**
 * Created by yanglu
 */
public abstract class MobileProvider {

    public abstract String getName();

    public abstract PhoneRange[] getPhoneNumRange();

    public boolean inRange(long phoneNum) {
        PhoneRange[] ranges = this.getPhoneNumRange();
        for (PhoneRange range : ranges) {
            if (range.inRange(phoneNum)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidPhone(long phone) {
        return this.inRange(phone);
    }
}


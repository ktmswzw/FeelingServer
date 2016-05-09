package com.xecoder.common.util.mobile;

/**
 * Created by yanglu
 */
public class PhoneRange {

    public final long start;
    public final long end;

    public final String prefix;

    public PhoneRange(String prefix) {
        if (prefix.length() != 3) {
            throw new IllegalArgumentException("invalid phone prefix " + prefix);
        }
        start = Long.parseLong(prefix + "00000000");
        end = Long.parseLong(prefix + "99999999");
        this.prefix = prefix;
    }

    public boolean inRange(long value) {
        return value >= start && value <= end;
    }
}

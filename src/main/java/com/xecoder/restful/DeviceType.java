package com.xecoder.restful;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-13:46
 * Feeling.com.xecoder.controller
 */
public enum DeviceType {
    IOS("0"),
    ANDROID("1"),
    OTHER("2");

    private String value;

    DeviceType(String value) {
        this.value = value;
    }

    public static DeviceType getType(String value) {
        for (DeviceType type : DeviceType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return OTHER;
    }
}

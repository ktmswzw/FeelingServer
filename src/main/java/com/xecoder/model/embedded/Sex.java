package com.xecoder.model.embedded;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-16:03
 * Feeling.com.xecoder.model.business
 */
public enum Sex {
    MALE(1),FEMALE(2);

    private int value;
    Sex(int value) {
        this.value = value;
    }

    public static Sex getSex(int value) {
        return value == 1 ? MALE : FEMALE;
    }

    public int getValue() {
        return value;
    }
}

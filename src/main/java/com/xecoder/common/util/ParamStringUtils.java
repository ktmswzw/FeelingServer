package com.xecoder.common.util;


/**
 * Created by luxp on 2015/10/21.
 */
public class ParamStringUtils {
    public static boolean isAvailable(String str) {
        String regex = "[\\u4e00-\\u9fa5\\w]+";
        return str.matches(regex);
    }

    public static void main(String[] args) {
        boolean result = isAvailable("=-");
        System.out.println(result);
    }


}

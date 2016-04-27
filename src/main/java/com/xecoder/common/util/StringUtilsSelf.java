package com.xecoder.common.util;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by vincent on 16/4/27.
 */
public class StringUtilsSelf {


    public static String setAnswerTip(String string) {
        StringBuffer buffer = new StringBuffer();
        if (string.length() > 1) {
            char[] strings = string.toCharArray();
            for(int i=0;i<strings.length;i++){
                if(i%2==0){
                    buffer.append("*");
                }
                else
                    buffer.append(strings[i]);
            }
            return buffer.toString();
        } else {
            return "";
        }
    }


    public static void main(String[] args) {
        setAnswerTip(" 我 是一abc个大花123123朵");
    }

}

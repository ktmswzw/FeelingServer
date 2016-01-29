package com.xecoder.common.util;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2015/9/11-9:16
 * HabitServer.com.qcloud
 */
public  interface QCloudPicKey {
    public static final String M_SECRET_ID = "AKIDSWWEVHp02vgtpFtSa4oFalHKm4V4P5Ia";
    public static final String M_SECRET_KEY = "tAj32v6ZQC385aP1oUeFCqljjUpJD4iC";

    public static final int APP_ID = 10005997;
    public static final String U_SECRET_ID= "AKIDnLdXqvdAkshrcjNYqYlMXIl8pK9uAai4";
    public static final String U_SECRET_KEY = "a91NDllhlVtSnIM5QAxn66olpEFflUcM";
    public static final String BUCKET = "habit";        //空间名
    public static final String YUN = ".image.myqcloud.com";
    public static final String DOMAIN = BUCKET+"-"+APP_ID+YUN;        //空间名
}

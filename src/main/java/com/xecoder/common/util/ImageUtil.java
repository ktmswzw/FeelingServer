package com.xecoder.common.util;

import com.xecoder.common.qcloud.HabitPicKey;
import com.xecoder.common.qcloud.PicCloud;
import com.xecoder.common.qcloud.UploadResult;
import com.xecoder.model.business.ImageBackup;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2015/9/11-12:48
 * HabitServer.com.habitteam.app.commons
 * 图片上传类
 */
public class ImageUtil {
    public static final int APP_ID_V2 = QCloudPicKey.APP_ID;
    public static final String SECRET_ID_V2 = QCloudPicKey.M_SECRET_ID;
    public static final String SECRET_KEY_V2 = QCloudPicKey.M_SECRET_KEY;
    public static final String BUCKET = QCloudPicKey.BUCKET;        //空间名


    /**
     * 执行上传初始化
     *
     * @param imageBackup 图片
     * @return
     */
    public static ImageBackup upload(ImageBackup imageBackup, String sign) {
        PicCloud pc = new PicCloud(APP_ID_V2, SECRET_ID_V2, SECRET_KEY_V2, BUCKET,sign);
        return pic_base(pc, imageBackup);
    }

    /**
     * 上传
     *
     * @param pc          云密钥
     * @param imageBackup 图片
     * @return
     */
    public  static ImageBackup pic_base(PicCloud pc, ImageBackup imageBackup) {
        String path = "";
        UploadResult result = new UploadResult();
        // 上传一张图片
        int ret = pc.Upload(imageBackup.getPath(), result);
        if (ret == 0) {
            imageBackup.setPath(result.fileid);//替换为云路径
            imageBackup.setDomain(QCloudPicKey.DOMAIN);
            imageBackup.setDate(new Timestamp((new Date()).getTime()));
        } else {
            path = "404";
        }
        return imageBackup;
    }

    public static ImageBackup pic_base(PicCloud pc, ImageBackup imageBackup, MultipartFile file) {
        String path = "";
        UploadResult result = new UploadResult();
        // 上传一张图片
        int ret = pc.Upload(file, result);
        if (ret == 0) {
            imageBackup.setPath(result.fileid);//替换为云路径
            imageBackup.setDomain(QCloudPicKey.DOMAIN);
            imageBackup.setDate(new Timestamp((new Date()).getTime()));
        } else {
            path = "404";
        }
        return imageBackup;
    }


    public static ImageBackup picGroup(PicCloud pc, ImageBackup imageBackup,  InputStream file) {
        String path = "";
        UploadResult result = new UploadResult();
        // 上传一张图片
        int ret = pc.UploadGroup(file, result);
        if (ret == 0) {
            imageBackup.setPath(result.fileid);//替换为云路径
            imageBackup.setDomain(QCloudPicKey.DOMAIN);
            imageBackup.setDate(new Timestamp((new Date()).getTime()));
        } else {
            path = "404";
        }
        return imageBackup;
    }

    public static ImageBackup upload(ImageBackup imageBackup, MultipartFile file, String sign) {
        PicCloud pc = new PicCloud(APP_ID_V2, SECRET_ID_V2, SECRET_KEY_V2, BUCKET,sign);
        return pic_base(pc, imageBackup, file);
    }

    public static ImageBackup upload(byte[] file,String sign) {
        PicCloud pc = new PicCloud(APP_ID_V2, SECRET_ID_V2, SECRET_KEY_V2, BUCKET,sign);
        UploadResult result = new UploadResult();
        // 上传一张图片
        int ret = pc.Upload(file, result);
        ImageBackup imageBackup = new ImageBackup();
        if (ret == 0) {
            imageBackup.setPath(result.fileid);//替换为云路径
            imageBackup.setDomain(QCloudPicKey.DOMAIN);
            imageBackup.setDate(new Timestamp((new Date()).getTime()));
        }
        return imageBackup;
    }

    public static ImageBackup uploadGroup(ImageBackup imageBackup, InputStream file, String sign) {
        PicCloud pc = new PicCloud(APP_ID_V2, SECRET_ID_V2, SECRET_KEY_V2, BUCKET,sign);
        return picGroup(pc, imageBackup, file);
    }

    /**
     * 获取签名
     * @return
     */
    public static String getAppSign()
    {
        PicCloud pc = new PicCloud(APP_ID_V2, SECRET_ID_V2, SECRET_KEY_V2, BUCKET);
        return pc.getAppSign();
    }


    /**
     * 获取完整路径
     * @return
     */
    public static String getPath(String url){
        return "http://"+HabitPicKey.DOMAIN + "/" + url;
    }

    /**
     * 获取完整路径small
     * @return
     */
    public static String getPathSmall(String url){
        if(url!=null) {
            return getPath(url) + "/small";
        }
        else
        {
            return "";
        }
    }

}

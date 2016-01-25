package com.xecoder.common.util;


import com.xecoder.model.business.ImageBackup;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2015/10/12-14:20
 * HabitServer.com.imakehabits.commons
 * 将网络图片汇集为一张九宫格图片
 */
public class ImageMerge {

    /**
     * 根据图片数量生成坐标
     * @param size
     * @return
     */
    public static String[] getXy(int size){
        String[] s = new String[size>9?9:size];
        int _x = 0;
        int _y = 0;
        if(size ==1){
            _x = _y = 6;
            s[0] = "6,6";
        }
        else if(size == 2){
            _x =_y = 4;
            s[0] = "4,"+(132/2-60/2);
            s[1] = 60+2*_x+","+ (132/2-60/2);
        }
        else if(size == 3){
            _x=_y =4;
            s[0] = (132/2-60/2)+","+_y;
            s[1] = _x+","+(60+2*_y);
            s[2] = (60+2*_y)+","+(60+2*_y);
        }
        else if(size ==4){
            _x=_y =4;
            s[0] = _x+","+_y;
            s[1] = (_x*2 + 60)+","+_y;
            s[2] = _x+","+(60+2*_y);
            s[3] = (60+2*_y)+","+(60+2*_y);
        }
        else if(size == 5){
            _x = _y = 3;
            s[0] = (132-40*2-_x)/2+","+(132-40*2-_y)/2;
            s[1] = ((132-40*2-_x)/2+40+_x)+","+(132-40*2-_y)/2;
            s[2] = _x+","+((132-40*2-_x)/2+40+_y);
            s[3] = (_x*2+40)+","+((132-40*2-_x)/2+40+_y);
            s[4] = (_x*3+40*2)+","+((132-40*2-_x)/2+40+_y);
        }
        else if(size == 6){
            _x = _y = 3;
            s[0] = _x+","+((132-40*2-_x)/2);
            s[1] = (_x*2+40)+","+((132-40*2-_x)/2);
            s[2] = (_x*3+40*2)+","+((132-40*2-_x)/2);
            s[3] = _x+","+((132-40*2-_x)/2+40+_y);
            s[4] = (_x*2+40)+","+((132-40*2-_x)/2+40+_y);
            s[5] = (_x*3+40*2)+","+((132-40*2-_x)/2+40+_y);
        }
        else if(size == 7){
            _x=_y =3;
            s[0] = (132-40)/2+","+_y;
            s[1] = _x+","+(_y*2+40);
            s[2] = (_x*2+40)+","+(_y*2+40);
            s[3] = (_x*3+40*2)+","+(_y*2+40);
            s[4] = _x+","+(_y*3+40*2);
            s[5] = (_x*2+40)+","+(_y*3+40*2);
            s[6] = (_x*3+40*2)+","+(_y*3+40*2);
        }
        else if(size == 8){
            _x=_y =3;
            s[0] = (132-80-_x)/2+","+_y;
            s[1] = ((132-80-_x)/2+_x+40)+","+_y;
            s[2] = _x+","+(_y*2+40);
            s[3] = (_x*2+40)+","+(_y*2+40);
            s[4] = (_x*3+40*2)+","+(_y*2+40);
            s[5] = _x+","+(_y*3+40*2);
            s[6] = (_x*2+40)+","+(_y*3+40*2);
            s[7] = (_x*3+40*2)+","+(_y*3+40*2);
        }
        else if(size >= 9){
            _x=_y = 3;
            s[0]=_x+","+_y;
            s[1] = _x*2+40+","+_y;
            s[2] = _x*3+40*2 +","+_y;
            s[3] = _x+","+(_y*2+40);
            s[4] = (_x*2+40)+","+(_y*2+40);
            s[5] = (_x*3+40*2)+","+(_y*2+40);
            s[6] = _x+","+(_y*3+40*2);
            s[7] = (_x*2+40)+","+(_y*3+40*2);
            s[8] = (_x*3+40*2)+","+(_y*3+40*2);
        }
            return s;
    }

    /**
     * 获取图片大小
     * @param size
     * @return
     */
    public static int getWidth(int size){
        int width = 0;
        if(size == 1){
            width = 120;
        }
        if(size>1 && size<=4){
            width = 60;
        }
        if(size>=5){
            width = 40;
        }
        return width;
    }


    /**
     * 图片拼接
     * @param sourceUrl
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public static BufferedImage zoom(String sourceUrl, int width, int height) throws IOException {
        String urlString = "http://"+HabitPicKey.DOMAIN + "/" + sourceUrl+"/small";
        URL url = new URL(urlString);
        int num = urlString.lastIndexOf('/');
        String u = urlString.substring(0,num);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("referer", u);       //通过这个http头的伪装来反盗链
        BufferedImage image = ImageIO.read(connection.getInputStream());
        image = zoom(image,width,height);
        return image;
    }

    /**
     * 图片流
     * @param sourceImage
     * @param width
     * @param height
     * @return
     */
    private static BufferedImage zoom(BufferedImage sourceImage , int width , int height){
        BufferedImage zoomImage = new BufferedImage(width, height, sourceImage.getType());
        Image image = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        Graphics gc = zoomImage.getGraphics();
        gc.setColor(Color.WHITE);
        gc.drawImage( image , 0, 0, null);
        return zoomImage;
    }

    /**
     * 主方法，创建图片
     * @param files
     * @throws Exception
     */
    public static String createImage(String[] files, String sign) throws Exception {
        String[] imageSize = getXy(files.length);
        String qyId = "";
        if(imageSize!=null) {
            int width = getWidth(files.length>9?9:files.length);
            BufferedImage ImageNew = new BufferedImage(132, 132, BufferedImage.TYPE_INT_RGB);
            //设置背景为白色
            for (int m = 0; m < 132; m++) {
                for (int n = 0; n < 132; n++) {
                    ImageNew.setRGB(m, n, 0xFFFFFF);
                }
            }
            for (int i = 0; i < imageSize.length; i++) {
                String size = imageSize[i];
                String[] sizeArr = size.split(",");
                int x = Integer.valueOf(sizeArr[0]);
                int y = Integer.valueOf(sizeArr[1]);
                BufferedImage ImageOne = zoom(files[i], width,width);
                //从图片中读取RGB
                int[] ImageArrayOne = new int[width * width];
                ImageArrayOne = ImageOne.getRGB(0, 0, width, width, ImageArrayOne, 0, width);
                ImageNew.setRGB(x, y, width, width, ImageArrayOne, 0, width);//设置RGB
            }
            try {
                ImageOutputStream imOut;
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                imOut = ImageIO.createImageOutputStream(bs);
                ImageIO.write(ImageNew, "png",imOut);
                ImageBackup imageBackup = new ImageBackup();
                imageBackup = ImageUtil.uploadGroup(imageBackup, new ByteArrayInputStream(bs.toByteArray()),sign);
                qyId = imageBackup.getPath();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return qyId;
    }

//    public static void main(String[] args) throws Exception {
//
//        String[] images = new String[9];
//        for(int i=0;i<9;i++)
//        {
//            images[i] = "98ac7f85-1d56-4d8c-83d3-fc2e8d65afb0";
//        }
//        //System.out.println(createImage(images));
//    }

}

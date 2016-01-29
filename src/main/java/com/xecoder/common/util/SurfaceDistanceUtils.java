package com.xecoder.common.util;

import org.springframework.data.geo.Point;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/29-15:54
 * Feeling.com.xecoder.common.util
 */
public class SurfaceDistanceUtils {
    //地球半径
    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;     //计算弧长
    }
    /**
     * 计算曲面最短距离
     * @param target
     * @param source
     * @return
     */
    public static double getShortestDistance(Point target, Point source) {
        double radLat1 = rad(target.getY());
        double radLat2 = rad(source.getY());
        double a = radLat1 - radLat2;
        double b = rad(target.getX()) - rad(source.getX());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }
}

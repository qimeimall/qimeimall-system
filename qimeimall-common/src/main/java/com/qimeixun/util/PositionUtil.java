package com.qimeixun.util;

public class PositionUtil {

    private static final double EARTH_RADIUS = 6378.137; // 6378.137为地球半径(单位:千米)

    // Java 计算两个GPS坐标点之间的距离
    // lat1、lng1 表示A点经纬度，lat2、lng2 表示B点经纬度，计算出来的结果单位为千米
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;      // a 是两坐标点的纬度之差
        double b = rad(lng1) - rad(lng2);  // b 是两坐标点的经度之差

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        System.out.println("s = " + s + "千米"); // 单位:千米

        s = Math.round(s * 1000); // 转为米，用 Math.round() 取整
        s = s/1000; // 米转千米
        return s;
    }

    // 角度转弧度
    // 把用角度表示的角转换为近似相等的用弧度表示的角 Math.toRadians。
    // 经纬度是以度数（0-360）表示。如果要把度数转换为弧度（0-2π），要除以360再乘以2ππ（相当于，乘以π/ 180）。
    private static double rad(double d) {  return d * Math.PI / 180.0; }
}

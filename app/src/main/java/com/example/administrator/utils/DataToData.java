package com.example.administrator.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HBL on 2015/12/27.
 * http://blog.csdn.net/thanklife/article/details/17002641
 */
public class DataToData {
    // 大小端转化问题
    //short转化成byte数组
    public static byte[] shortTobyte(int n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    // int转化成byte数组
    public static byte[] intTobyte(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    // byte数组转化为int
    public static int byteToint(byte[] bArr) {
        byte begin = bArr[0];
        if (begin < 0) {
            short n = 0;
            for (int i = 0; i < bArr.length && i < 4; i++) {
                int left = i * 8;
                n += (bArr[i] << left);
            }
            return n + 256;
        } else {
            short n = 0;
            for (int i = 0; i < bArr.length && i < 4; i++) {
                int left = i * 8;
                n += (bArr[i] << left);
            }
            return n;
        }
    }
    public static String getCurrentTime()
    {
        //获取当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = format.format(curDate);
        System.out.println(str);
        return str;
    }
}

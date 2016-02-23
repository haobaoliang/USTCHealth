package com.example.administrator.utils;



/**
 * Created by HBL on 2016/1/25.
 */
public class ComFormu {

    static public double getKcal(int stepnum)
    {
        //已知体重、距离
        //跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
        double mKcal;
        mKcal=55*stepnum*1.306/1000;
        return mKcal;
    }
}

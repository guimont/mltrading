package com.mltrading.ml.util;

public class MLMath {


    public static int PERCENT = 100;
    public static int SUM20 = 210;


    /**
     * calculate Yield
     * @param endValue
     * @param startValue
     * @return
     */
    public static double yield(double endValue, double startValue) {
        if (startValue == 0.) return 0.;

        return (endValue - startValue) / startValue * 100;
    }

}

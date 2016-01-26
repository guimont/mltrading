package com.mltrading.ml;

import java.io.Serializable;

/**
 * Created by gmo on 19/01/2016.
 */
public class MLPerformance implements Serializable{
    private double yield_1D;
    private double realyield_1D;
    private boolean sign;


    public MLPerformance(double yield_1D) {
        this.yield_1D = yield_1D;
    }

    public MLPerformance(double yield_1D, boolean sign) {
        this.yield_1D = yield_1D;
        this.sign = sign;
    }

    public MLPerformance(double yield_1D, double realyield_1D, boolean sign) {
        this.yield_1D = yield_1D;
        this.realyield_1D = realyield_1D;
        this.sign = sign;
    }

    public double getYield_1D() {
        return yield_1D;
    }

    public void setYield_1D(double yield_1D) {
        this.yield_1D = yield_1D;
    }

    public double getRealyield_1D() {
        return realyield_1D;
    }

    public void setRealyield_1D(double realyield_1D) {
        this.realyield_1D = realyield_1D;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public static double calculYield(double p, double  v) {
        return (p-v)/v;
    }

    public static MLPerformance calculYields(double prediction, double  value, double currentValue) {
        //sign : value - currentValue
        boolean sign =(int) Math.signum(value - currentValue) == (int) Math.signum(prediction - currentValue);
        double yield_1D = calculYield(prediction, currentValue);
        double realyield_1D = calculYield(value, currentValue);

        return new MLPerformance(yield_1D, realyield_1D, sign);

    }


}

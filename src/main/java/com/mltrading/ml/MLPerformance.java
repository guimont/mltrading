package com.mltrading.ml;

import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 19/01/2016.
 */
public class MLPerformance implements Serializable{
    private double yield;
    private double realyield;
    private boolean sign;
    private String date;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public MLPerformance(String date, double yield, double realyield, boolean sign) {
        this.yield = yield;
        this.realyield = realyield;
        this.sign = sign;
        this.date = date;
    }

    public double getYield() {
        return yield;
    }

    public void setYield(double yield) {
        this.yield = yield;
    }

    public double getRealyield() {
        return realyield;
    }

    public void setRealyield(double realyield) {
        this.realyield = realyield;
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

    public static MLPerformance calculYields(String date, double prediction, double  value, double currentValue) {
        //sign : value - currentValue
        boolean sign =(int) Math.signum(value - currentValue) == (int) Math.signum(prediction - currentValue);
        double yield_1D = calculYield(prediction, currentValue);
        double realyield_1D = calculYield(value, currentValue);

        return new MLPerformance(date, yield_1D, realyield_1D, sign);

    }

    public void savePerformance(BatchPoints bp, String code) {
        Point pt = Point.measurement(code).time(new DateTime(date).getMillis() + 3600000, TimeUnit.MILLISECONDS)
            .field("sign", sign)
            .field("yield", yield)
            .field("realyield", realyield)
            .build();
        bp.point(pt);
    }

}

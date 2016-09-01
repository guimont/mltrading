package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;
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
    private double prediction;

    /** value in Dday.. depend on period*/
    private double realvalue;

    /** day value same of all vlue in MLPerformances*/
    private double value;


    private double error;


    public String getDate() {
        return date;
    }

    public static MLPerformance generateEmptyMLPerformance(String date) {
        return new MLPerformance(date,0,0,0,0,0,true);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MLPerformance() {}

    public MLPerformance(String date, double prediction, double  value, double realvalue,double yield, double realyield, boolean sign) {
        this.yield = yield;
        this.realyield = realyield;
        this.sign = sign;
        this.date = date;
        this.prediction = prediction;
        this.realvalue = realvalue;
        this.value = value;
        this.error = realvalue - prediction;
    }


    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public double getPrediction() {
        return prediction;
    }

    public void setPrediction(double prediction) {
        this.prediction = prediction;
    }

    public double getRealvalue() {
        return realvalue;
    }

    public void setRealvalue(double realvalue) {
        this.realvalue = realvalue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double currentValue) {
        this.value = currentValue;
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

    public static MLPerformance calculYields(String date, double prediction, double  realvalue, double currentValue) {
        //sign : value - currentValue
        boolean sign =(int) Math.signum(realvalue - currentValue) == (int) Math.signum(prediction - currentValue);
        double yield_1D = calculYield(prediction, currentValue);
        double realyield_1D = calculYield(realvalue, currentValue);

        return new MLPerformance(date,prediction, realvalue, currentValue, yield_1D, realyield_1D, sign);

    }

    public void savePerformance(BatchPoints bp, String code) {
        Point pt = Point.measurement(code).time(new DateTime(date).getMillis() + 3600000, TimeUnit.MILLISECONDS)
            .field("sign", sign)
            .field("yield", yield)
            .field("realyield", realyield)
            .field("prediction", prediction)
            .field("realvalue", realvalue)
            .field("value", value)
            .field("error", error)
            .build();
        bp.point(pt);
        InfluxDaoConnector.writePoints(bp);
    }

}

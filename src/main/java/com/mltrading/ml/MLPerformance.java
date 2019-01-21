package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.io.Serializable;


/**
 * Created by gmo on 19/01/2016.
 */
public class MLPerformance implements Serializable{
    private double yield;
    private double realyield;
    private boolean sign;
    private String date;
    private double prediction;

    private String currentDate;

    /** currentValue in Dday.. depend on period*/
    private double realValue;

    /** day currentValue same of all vlue in MLPerformances*/
    private double currentValue;


    private double error;


    public String getDate() {
        return date;
    }

    public static MLPerformance generateEmptyMLPerformance(String date, String currentDate) {
        return new MLPerformance(date, currentDate , 0,0,0,0,0,true);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MLPerformance() {}

    public MLPerformance(String date, String currentDate, double prediction, double realValue, double  currentValue, double yield, double realyield, boolean sign) {
        this.yield = yield;
        this.realyield = realyield;
        this.sign = sign;
        this.date = date;
        this.currentDate = currentDate;
        this.prediction = prediction;
        this.realValue = realValue;
        this.currentValue = currentValue;
        if (realValue != -1)
            this.error = realValue - prediction;
        else
            this.error = 0.;
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
        return realValue;
    }

    public void setRealvalue(double realvalue) {
        this.realValue = realvalue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
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

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public static double calculYield(double p, double  v) {
        return (p-v)/v;
    }

    public static MLPerformance calculYields(String date, String currentDate ,double prediction, double  realvalue, double currentValue) {
        //sign : currentValue - currentValue

        if (currentValue == 0) {
            System.out.println("unexpected error go to evil");
            return new MLPerformance(date, currentDate, prediction, realvalue, currentValue, 0, 0, false);
        }

        boolean sign =(int) Math.signum(realvalue - currentValue) == (int) Math.signum(prediction - currentValue);
        double yield_1D = calculYield(prediction, currentValue);
        double realyield_1D = calculYield(realvalue, currentValue);

        return new MLPerformance(date, currentDate, prediction, realvalue, currentValue, yield_1D, realyield_1D, sign);
    }


    /**
     * use for
     * @param date
     * @param prediction
     * @return
     */
    public static MLPerformance calculOnlyYields(String date, String currentDate, double prediction,double yield) {
        //sign : currentValue - currentValue
        boolean sign =(int) Math.signum(prediction) == (int) Math.signum(yield);


        return new MLPerformance(date, currentDate, prediction, yield, yield, prediction, yield, sign);
    }




    public void savePerformance(BatchPoints bp, String code) throws InterruptedException {
        Point pt = Point.measurement(code)
            .field("datePred",date)
            .field("currentDate",currentDate)
            .field("sign", sign)
            .field("yield", yield)
            .field("realyield", realyield)
            .field("prediction", prediction)
            .field("realvalue", realValue)
            .field("currentValue", currentValue)
            .field("error", error)
            .build();
        bp.point(pt);

    }


    public MLPerformance clone() {
        MLPerformance cloneObject = new MLPerformance();
        cloneObject.date = this.date;
        cloneObject.currentDate = this.currentDate;
        cloneObject.sign = this.sign;
        cloneObject.yield = this.yield;
        cloneObject.realyield = this.realyield;
        cloneObject.prediction = this.prediction;
        cloneObject.realValue = this.realValue;
        cloneObject.currentValue = this.currentValue;
        cloneObject.error = this.error;

        return cloneObject;
    }

}

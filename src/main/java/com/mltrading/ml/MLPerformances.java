package com.mltrading.ml;

import java.io.Serializable;

/**
 * Created by gmo on 10/02/2016.
 */
public class MLPerformances  implements Serializable, Comparable<MLPerformances> {

    private String date;
    private MLPerformance mlD1;
    private MLPerformance mlD5;
    private MLPerformance mlD20;

    private double avgD1 = 0;
    private double avgD5 = 0;
    private double avgD20 = 0;


    public double getAvgD1() {
        return avgD1;
    }

    public void setAvgD1(double avgD1) {
        this.avgD1 = avgD1;
    }

    public double getAvgD5() {
        return avgD5;
    }

    public void setAvgD5(double avgD5) {
        this.avgD5 = avgD5;
    }

    public double getAvgD20() {
        return avgD20;
    }

    public void setAvgD20(double avgD20) {
        this.avgD20 = avgD20;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MLPerformances(String date) {
        this.date = date;
    }

    public MLPerformance getMlD1() {
        return mlD1;
    }

    public void setMlD1(MLPerformance mlD1) {
        this.mlD1 = mlD1;
    }

    public MLPerformance getMlD5() {
        return mlD5;
    }

    public void setMlD5(MLPerformance mlD5) {
        this.mlD5 = mlD5;
    }

    public MLPerformance getMlD20() {
        return mlD20;
    }

    public void setMlD20(MLPerformance mlD20) {
        this.mlD20 = mlD20;
    }


    @Override
    public int compareTo(MLPerformances o) {
        return o.getDate().compareTo(this.date)*-1;
    }
}

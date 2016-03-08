package com.mltrading.models.stock;

import java.io.Serializable;

/**
 * Created by gmo on 08/03/2016.
 */
public class DetailData implements Serializable {
    private String date;
    private double value;
    private double predD1;
    private double predD5;
    private double predD20;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getPredD1() {
        return predD1;
    }

    public void setPredD1(double predD1) {
        this.predD1 = predD1;
    }

    public double getPredD5() {
        return predD5;
    }

    public void setPredD5(double predD5) {
        this.predD5 = predD5;
    }

    public double getPredD20() {
        return predD20;
    }

    public void setPredD20(double predD20) {
        this.predD20 = predD20;
    }
}

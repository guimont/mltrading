package com.mltrading.models.stock;

import java.io.Serializable;

/**
 * Created by gmo on 08/03/2016.
 */
public class DetailData implements Serializable {
    private String date;
    private double value;
    private double opening;
    private double volume;
    private double high;
    private double low;
    private double predD1;
    private double predD5;
    private double valueD5;
    private boolean signD5;

    private double predD40;
    private double valueD40;
    private boolean signD40;

    private double predD20;
    private double valueD20;
    private boolean signD20;

    public double getOpening() {
        return opening;
    }

    public void setOpening(double opening) {
        this.opening = opening;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public boolean isSignD20() {
        return signD20;
    }

    public void setSignD20(boolean signD20) {
        this.signD20 = signD20;
    }

    public double getValueD20() {
        return valueD20;
    }

    public void setValueD20(double valueD20) {
        this.valueD20 = valueD20;
    }

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

    public double getValueD5() {
        return valueD5;
    }

    public void setValueD5(double valueD5) {
        this.valueD5 = valueD5;
    }

    public boolean isSignD5() {
        return signD5;
    }

    public void setSignD5(boolean signD5) {
        this.signD5 = signD5;
    }

    public double getPredD40() {
        return predD40;
    }

    public void setPredD40(double predD40) {
        this.predD40 = predD40;
    }

    public double getValueD40() {
        return valueD40;
    }

    public void setValueD40(double valueD40) {
        this.valueD40 = valueD40;
    }

    public boolean isSignD40() {
        return signD40;
    }

    public void setSignD40(boolean signD40) {
        this.signD40 = signD40;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }
}

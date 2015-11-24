package com.mltrading.ml;

import com.mltrading.models.stock.StockHistory;

/**
 * Created by gmo on 24/11/2015.
 */
public class FeaturesStock {

    private double predictionValue;

    private double value;

    private double volume;

    private double mm20;

    private double mma50;

    private double mme12;

    private double mme26;

    private double momentum;

    private double stdDev;

    public double getPredictionValue() {
        return predictionValue;
    }

    public void setPredictionValue(double predictionValue) {
        this.predictionValue = predictionValue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getMm20() {
        return mm20;
    }

    public void setMm20(double mm20) {
        this.mm20 = mm20;
    }

    public double getMma50() {
        return mma50;
    }

    public void setMma50(double mma50) {
        this.mma50 = mma50;
    }

    public double getMme12() {
        return mme12;
    }

    public void setMme12(double mme12) {
        this.mme12 = mme12;
    }

    public double getMme26() {
        return mme26;
    }

    public void setMme26(double mme26) {
        this.mme26 = mme26;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public double getStdDev() {
        return stdDev;
    }

    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }


    public FeaturesStock transform(StockHistory sh) {
        this.setMm20(sh.getAnalyse_tech().getMma20());
        this.setMma50(sh.getAnalyse_tech().getMma50());
        this.setMme12(sh.getAnalyse_tech().getMme12());
        this.setMme26(sh.getAnalyse_tech().getMme26());
        this.setMomentum(sh.getAnalyse_tech().getMomentum());
        this.setStdDev(sh.getAnalyse_tech().getStdDev());
        this.setValue(sh.getValue());
        this.setVolume(sh.getVolume());
        return this;
    }

    public double[] vectorize() {
        return new double[8];
    }
}

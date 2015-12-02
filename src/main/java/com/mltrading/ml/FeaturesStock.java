package com.mltrading.ml;

import com.mltrading.models.stock.StockHistory;
import scala.Serializable;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 24/11/2015.
 */
public class FeaturesStock implements Serializable {

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


    public static FeaturesStock transform(StockHistory sh,double value) {
        FeaturesStock fs = new FeaturesStock();

        fs.setPredictionValue(value);
        fs.setMm20(sh.getAnalyse_tech().getMma20());
        fs.setMma50(sh.getAnalyse_tech().getMma50());
        fs.setMme12(sh.getAnalyse_tech().getMme12());
        fs.setMme26(sh.getAnalyse_tech().getMme26());
        fs.setMomentum(sh.getAnalyse_tech().getMomentum());
        fs.setStdDev(sh.getAnalyse_tech().getStdDev());
        fs.setValue(sh.getValue());
        fs.setVolume(sh.getVolume());
        return fs;
    }

    public double[] vectorize() {
        double[] vector = new double[9];
        vector[0] = this.getValue();
        vector[1] = this.getVolume();
        vector[3] = 0;
        vector[4] = 0;
        vector[5] = 0;
        vector[6] = 0;
        vector[7] = 0;
        vector[8] = 0;

        return vector;
    }

    public  static List<FeaturesStock> transformList(List<StockHistory> shL) {
        List<FeaturesStock> fsL = new ArrayList<>();
        StockHistory feature = null;

        for (StockHistory sh:shL) {
            if (feature != null)
                fsL.add(transform(feature,sh.getValue()));
            try {
                feature = (StockHistory) sh.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return fsL;
    }
}

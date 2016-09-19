package com.mltrading.models.stock;

import java.io.Serializable;

/**
 * Created by gmo on 29/02/2016.
 */
public class StockPrediction implements Serializable{

    private String codif;

    private double PredictionD1 = 0;
    private double PredictionD5 = 0;
    private double PredictionD20 = 0;
    private double PredictionD40 = 0;

    private double confidenceD1 = 0;
    private double confidenceD5 = 0;
    private double confidenceD20 = 0;
    private double confidenceD40 = 0;

    public StockPrediction(String codif) {
        this.codif = codif;
    }

    public String getCodif() {
        return codif;
    }

    public void setCodif(String codif) {
        this.codif = codif;
    }

    public double getPredictionD1() {
        return PredictionD1;
    }

    public void setPredictionD1(double predictionD1) {
        PredictionD1 = predictionD1;
    }

    public double getPredictionD5() {
        return PredictionD5;
    }

    public void setPredictionD5(double predictionD5) {
        PredictionD5 = predictionD5;
    }

    public double getPredictionD20() {
        return PredictionD20;
    }

    public void setPredictionD20(double predictionD20) {
        PredictionD20 = predictionD20;
    }

    public double getConfidenceD1() {
        return confidenceD1;
    }

    public void setConfidenceD1(double confidenceD1) {
        this.confidenceD1 = confidenceD1;
    }

    public double getConfidenceD5() {
        return confidenceD5;
    }

    public void setConfidenceD5(double confidenceD5) {
        this.confidenceD5 = confidenceD5;
    }

    public double getConfidenceD20() {
        return confidenceD20;
    }

    public void setConfidenceD20(double confidenceD20) {
        this.confidenceD20 = confidenceD20;
    }

    public void setPredictionD40(double predictionD40) {
        PredictionD40 = predictionD40;
    }

    public void setConfidenceD40(double confidenceD40) {
        this.confidenceD40 = confidenceD40;
    }
}

package com.mltrading.assetmanagement;

import java.io.Serializable;

public class AssetProperties implements Serializable {

    private String name;
    private double threshold;
    private boolean fixedcosts;
    private double commissionFix;
    private double commissionPercent;
    private double stopLose = 0.95;
    private double stopWin;
    private double part = 2000;



    public AssetProperties(String name, double threshold,boolean fixedcosts, double commissionValue) {
        this.name = name;
        this.threshold = threshold;

        this.fixedcosts = fixedcosts;
        if (fixedcosts) {
            this.commissionFix = commissionValue;
        } else {
            this.commissionPercent = commissionValue;
        }

    }

    public AssetProperties(AssetProperties properties) {
        this.name = properties.name;
        this.threshold =  properties.threshold;
        this.commissionFix =  properties.commissionFix;
        this.commissionPercent =  properties.commissionPercent;
        this.part = properties.part;
        this.fixedcosts = properties.fixedcosts;
        this.stopLose = properties.stopLose;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getCommissionFix() {
        return commissionFix;
    }

    public void setCommissionFix(double commissionFix) {
        this.commissionFix = commissionFix;
    }

    public double getCommissionPercent() {
        return commissionPercent;
    }

    public void setCommissionPercent(double commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public double getStopLose() {
        return stopLose;
    }

    public void setStopLose(double stopLose) {
        this.stopLose = stopLose;
    }

    public double getStopWin() {
        return stopWin;
    }

    public void setStopWin(double stopWin) {
        this.stopWin = stopWin;
    }

    public double getPart() {
        return part;
    }

    public void setPart(double part) {
        this.part = part;
    }

    public boolean isFixedcosts() {
        return fixedcosts;
    }

    public void setFixedcosts(boolean fixedcosts) {
        this.fixedcosts = fixedcosts;
    }
}

package com.mltrading.assetmanagement;

import com.mltrading.models.stock.StockGeneral;

import java.io.Serializable;
import java.util.Date;

public class AssetStock extends AssetProperties implements Serializable {

    private String code;
    private String sector;
    /*date buy in*/
    private String DateBuyIn;
    /*date buy in*/
    private String DateBuyOut;

    /*stock number Bought*/
    private int Volume;

    /*price where lose is efficient*/
    private double priceStopLose;
    /*price where win is efficient*/
    private double priceStopWin;


    private double priceBuyIn;
    private double priceBuyOut;

    /*expected increase if true, else false*/
    private boolean increase;

    /*difference between buy and sell*/
    private double margin;

    private double yieldExpected;

    private double yieldExpectedLong;
    private double yieldExpectedShort;
    private double yieldExpectedGlobal;


    public AssetStock(String code, String sector, AssetProperties properties) {
        super(properties);
        this.code = code;
        this.sector = sector;
    }


    /**
     * change objective performance
     * @param sg
     */
    public void changeValue(StockGeneral sg) {

        this.setPriceStopWin(sg.getValue() * (sg.getPerformanceEstimate()*0.01+1));
        this.setYieldExpected(sg.getPerformanceEstimate());
        this.setYieldExpectedLong(sg.getPrediction().getYieldD20());
        this.setYieldExpectedShort(sg.getPredictionShort().getYieldD20());
    }


    /**
     * buy stock
     * @param sg
     * @return
     */
    public double buyIt(StockGeneral sg) {
        this.setDateBuyIn(sg.getDay());
        this.setIncrease(sg.getPrediction().isIncrease());
        this.setPriceBuyIn(sg.getValue());
        double stopLose =  this.getStopLose();
        if (!this.isIncrease()) stopLose = 2 - this.getStopLose(); // we expected stock down revert stop lose 0,95 give 1.05
        this.setPriceStopLose(sg.getValue() *  stopLose);
        this.setPriceStopWin(sg.getValue() * (sg.getPerformanceEstimate()*0.01+1));
        this.setVolume((int) (this.getPart()/sg.getValue()));

        this.setYieldExpected(sg.getPerformanceEstimate());


        this.setYieldExpectedLong(sg.getPrediction().getYieldD20());
        this.setYieldExpectedShort(sg.getPredictionShort().getYieldD20());


        return this.getVolume()*sg.getValue();
    }

    public boolean sellIt(double value, String date) {
        priceBuyOut = value;
        DateBuyOut = date;
        if (!increase)
            margin = priceBuyIn - priceBuyOut;
        else
            margin = priceBuyOut - priceBuyIn;


        return true;
    }


    /**
     * check if buy value (lose or win) is reach
     * @param value
     * @return
     */
    public boolean makeAction(Double value) {

        if (increase) {
            if (value <= priceStopLose) return true;
            if (value >= priceStopWin) return true;
        } else {
            if (value >= priceStopLose) return true;
            if (value <= priceStopWin) return true;
        }

        return false;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDateBuyIn() {
        return DateBuyIn;
    }

    public void setDateBuyIn(String dateBuyIn) {
        DateBuyIn = dateBuyIn;
    }

    public String getDateBuyOut() {
        return DateBuyOut;
    }

    public void setDateBuyOut(String dateBuyOut) {
        DateBuyOut = dateBuyOut;
    }

    public int getVolume() {
        return Volume;
    }

    public void setVolume(int volume) {
        Volume = volume;
    }

    public double getPriceStopLose() {
        return priceStopLose;
    }

    public void setPriceStopLose(double priceStopLose) {
        this.priceStopLose = priceStopLose;
    }

    public double getPriceStopWin() {
        return priceStopWin;
    }

    public void setPriceStopWin(double priceStopWin) {
        this.priceStopWin = priceStopWin;
    }

    public double getPriceBuyIn() {
        return priceBuyIn;
    }

    public void setPriceBuyIn(double priceBuyIn) {
        this.priceBuyIn = priceBuyIn;
    }

    public double getPriceBuyOut() {
        return priceBuyOut;
    }

    public void setPriceBuyOut(double priceBuyOut) {
        this.priceBuyOut = priceBuyOut;
    }

    public boolean isIncrease() {
        return increase;
    }

    public void setIncrease(boolean increase) {
        this.increase = increase;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }


    public double getYieldExpected() {
        return yieldExpected;
    }

    public void setYieldExpected(double yieldExpected) {
        this.yieldExpected = yieldExpected;
    }

    public double getYieldExpectedLong() {
        return yieldExpectedLong;
    }

    public void setYieldExpectedLong(double yieldExpectedLong) {
        this.yieldExpectedLong = yieldExpectedLong;
    }

    public double getYieldExpectedShort() {
        return yieldExpectedShort;
    }

    public void setYieldExpectedShort(double yieldExpectedShort) {
        this.yieldExpectedShort = yieldExpectedShort;
    }

    public double getYieldExpectedGlobal() {
        return yieldExpectedGlobal;
    }

    public void setYieldExpectedGlobal(double yieldExpectedGlobal) {
        this.yieldExpectedGlobal = yieldExpectedGlobal;
    }
}

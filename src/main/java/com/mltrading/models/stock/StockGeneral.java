package com.mltrading.models.stock;

import java.io.Serializable;

/**
 * Created by gmo on 17/06/2015.
 */
public class StockGeneral implements Serializable {

    private String code;

    private String name;

    private String place;

    private String codif;

    private String placeCodif;

    private String sector;

    private double performanceEstimate;

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getPlaceCodif() {
        return placeCodif;
    }

    public void setPlaceCodif(String placeCodif) {
        this.placeCodif = placeCodif;
    }

    /**
     * last price of stock
     */
    private Float value;

    /**
     * current variation with opening price
     */
    private Float variation;

    /**
     * opening price
     */
    private Float opening;

    /**
     * highest price
     */

    private Float highest;

    /**
     * lowest price
     */
    private Float lowest;

    /**
     * january variation
     */
    private Float firstJanuaryVariation;

    /**
     * volume
     */
    private Integer volume;


    private StockPrediction prediction;


    public StockPrediction getPrediction() {
        return prediction;
    }

    public void setPrediction(StockPrediction prediction) {
        this.prediction = prediction;
    }

    public String getCodif() {
        return codif;
    }

    public String getRealCodif() {
        if (codif.equalsIgnoreCase("OREAL"))
            return "OR";
        return codif;
    }

    public void setCodif(String codif) {
        this.codif = codif;
    }

    public StockGeneral() {
    }

    public StockGeneral(String code, String name,  String codif, String place, String placeCodif) {
        this.code = code;
        this.name = name;
        this.place = place;
        this.codif = codif;
        this.placeCodif = placeCodif;
    }
    public StockGeneral(String code, String name,  String codif, String place, String placeCodif, String sector, String subsector) {
        this.code = code;
        this.name = name;
        this.place = place;
        this.codif = codif;
        this.placeCodif = placeCodif;
        this.sector = sector;
    }

    public StockGeneral(Float value, Float variation, Float opening, Float highest, Float lowest, Float firstJanuaryVaraition, Integer volume) {
        this.value = value;
        this.variation = variation;
        this.opening = opening;
        this.highest = highest;
        this.lowest = lowest;
        this.firstJanuaryVariation = firstJanuaryVaraition;
        this.volume = volume;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Float getVariation() {
        return variation;
    }

    public void setVariation(Float variation) {
        this.variation = variation;
    }

    public Float getOpening() {
        return opening;
    }

    public void setOpening(Float opening) {
        this.opening = opening;
    }

    public Float getHighest() {
        return highest;
    }

    public void setHighest(Float highest) {
        this.highest = highest;
    }

    public Float getLowest() {
        return lowest;
    }

    public void setLowest(Float lowest) {
        this.lowest = lowest;
    }

    public Float getFirstJanuaryVariation() {
        return firstJanuaryVariation;
    }

    public void setFirstJanuaryVariation(Float firstJanuaryVariation) {
        this.firstJanuaryVariation = firstJanuaryVariation;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }


    public double getPerformanceEstimate() {
        return performanceEstimate;
    }

    public void setPerformanceEstimate(double performanceEstimate) {
        this.performanceEstimate = performanceEstimate;
    }
}

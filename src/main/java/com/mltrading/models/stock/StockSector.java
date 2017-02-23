package com.mltrading.models.stock;



/**
 * Created by gmo on 15/12/2015.
 */
public class StockSector extends StockHistory{

    /**
     * current variation with opening price
     */
    private Float variation;

    private String urlInvestir;

    private StockPrediction prediction;

    private double performanceEstimate;

    public double getPerformanceEstimate() {
        return performanceEstimate;
    }

    public void setPerformanceEstimate(double performanceEstimate) {
        this.performanceEstimate = performanceEstimate;
    }


    public StockPrediction getPrediction() {
        return prediction;
    }

    public void setPrediction(StockPrediction prediction) {
        this.prediction = prediction;
    }



    public StockSector(String code, String name, String place, String url, int row) {
        this.setCode(code);
        this.setCodif(code);
        this.setPlace(place);
        this.setName(name);
        this.setUrlInvestir(url);
        this.setRow(row);
    }


    static public String translate(String investir) {

        return null;
    }


    public void setUrlInvestir(String urlInvestir) {
        this.urlInvestir = urlInvestir;
    }


    public Float getVariation() {
        return variation;
    }

    public void setVariation(Float variation) {
        this.variation = variation;
    }
}

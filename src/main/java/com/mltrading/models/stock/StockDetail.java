package com.mltrading.models.stock;

import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MLPerformances;
import com.mltrading.ml.MLStocks;
import com.mltrading.ml.Validator;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gmo on 03/03/2016.
 */
public class StockDetail implements Serializable{
    private String code;
    private Stock stock;
    private List<StockHistory> history;
    private List<StockSector> sector;
    private Validator validatorD1;
    private Validator validatorD5;
    private Validator validatorD20;
    private StockPrediction prediction;
    private List<MLPerformances> perf;


    public static StockDetail populate(Stock s) {
        StockDetail detail = new StockDetail();
        MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodeif());
        detail.setCode(s.getCodeif());
        detail.setStock(s);
        detail.setPrediction(CacheStockGeneral.getCache().get(s.getCode()).getPrediction());
        detail.setValidatorD1(mls.getMlD1().getValidator());
        detail.setValidatorD5(mls.getMlD5().getValidator());
        detail.setValidatorD20(mls.getMlD20().getValidator());
        detail.setHistory(StockHistory.getStockHistoryLast(s.getCode(), 20));
        int size =  mls.getStatus().getPerfList().size();
        //detail.setSector();
        detail.setPerf(mls.getStatus().getPerfList().subList(size - 20, size));
        return detail;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public List<StockHistory> getHistory() {
        return history;
    }

    public void setHistory(List<StockHistory> history) {
        this.history = history;
    }

    public List<StockSector> getSector() {
        return sector;
    }

    public void setSector(List<StockSector> sector) {
        this.sector = sector;
    }

    public Validator getValidatorD1() {
        return validatorD1;
    }

    public void setValidatorD1(Validator validatorD1) {
        this.validatorD1 = validatorD1;
    }

    public Validator getValidatorD5() {
        return validatorD5;
    }

    public List<MLPerformances> getPerf() {
        return perf;
    }

    public void setPerf(List<MLPerformances> perf) {
        this.perf = perf;
    }

    public void setValidatorD5(Validator validatorD5) {
        this.validatorD5 = validatorD5;
    }

    public Validator getValidatorD20() {
        return validatorD20;
    }

    public void setValidatorD20(Validator validatorD20) {
        this.validatorD20 = validatorD20;
    }

    public StockPrediction getPrediction() {
        return prediction;
    }

    public void setPrediction(StockPrediction prediction) {
        this.prediction = prediction;
    }
}

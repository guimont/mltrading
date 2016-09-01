package com.mltrading.models.stock;

import com.mltrading.ml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 03/03/2016.
 */
public class StockDetail implements Serializable{

    private static final Logger log = LoggerFactory.getLogger(StockDetail.class);
    private String code;
    private Stock stock;

    private List<StockSector> sector;
    private MatrixValidator validatorD1;
    private MatrixValidator validatorD5;
    private MatrixValidator validatorD20;
    private StockPrediction prediction;
    private List<DetailData> data;



    public static StockDetail populate(Stock s) {
        StockDetail detail = new StockDetail();
        if (s != null)
            log.info(s.getCodif());
        else
            log.info("s null error");

        log.info("cache size:" + CacheMLStock.getMLStockCache().size());
        MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
        if (mls == null )
            log.info("mls null error");
        detail.setCode(s.getCodif());
        detail.setStock(s);
        detail.setPrediction(CacheStockGeneral.getCache().get(s.getCode()).getPrediction());
        detail.setValidatorD1(mls.getMlD1().getValidator());
        detail.setValidatorD5(mls.getMlD5().getValidator());
        detail.setValidatorD20(mls.getMlD20().getValidator());
        //detail.setSector();
        detail.setData(populateData(s));
        return detail;
    }


    private static double findPredD1(List<MLPerformances> perfList, String date) {
        for (MLPerformances p: perfList) {
            if (p.getMlD1().getDate().equals(date))
                return p.getMlD1().getPrediction();
        }

        return 0; //not found not normal
    }

    private static double findPredD5(List<MLPerformances> perfList, String date) {
        for (MLPerformances p: perfList) {
            if (p.getMlD5().getDate().equals(date))
                return p.getMlD5().getPrediction();
        }
        return 0; //not found not normal
    }

    private static MLPerformance findPredD20(List<MLPerformances> perfList, String date) {
        for (MLPerformances p: perfList) {
            if (p.getMlD20().getDate().equals(date))
                return p.getMlD20();
        }
        return null; //not found not normal
    }

    private static List<DetailData> populateData(Stock s) {
        List<DetailData> data = new ArrayList<>();
        List<StockHistory> h = StockHistory.getStockHistoryLast(s.getCodif(), 40);
        MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());

        for (StockHistory he:h) {
            DetailData d = new DetailData();
            d.setDate(he.getDay());
            d.setValue(he.getValue());
            d.setPredD1(findPredD1(mls.getStatus().getPerfList(),he.getDay()));
            d.setPredD5(findPredD5(mls.getStatus().getPerfList(), he.getDay()));
            MLPerformance perf20 = findPredD20(mls.getStatus().getPerfList(), he.getDay());
            d.setPredD20(perf20.getPrediction());
            d.setSignD20(perf20.isSign());
            data.add(d);
        }

        int size = mls.getStatus().getPerfList().size();
        for (int i=1; i<20; i++) {
            DetailData d = new DetailData();
            d.setDate("J+"+i);
            if (i < 5) d.setPredD5(mls.getStatus().getPerfList().get(size - 5 + i).getMlD5().getPrediction());
            d.setPredD20(mls.getStatus().getPerfList().get(size-20+i).getMlD20().getPrediction());
            data.add(d);
        }



        return data;
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


    public List<StockSector> getSector() {
        return sector;
    }

    public void setSector(List<StockSector> sector) {
        this.sector = sector;
    }

    public MatrixValidator getValidatorD1() {
        return validatorD1;
    }

    public void setValidatorD1(MatrixValidator validatorD1) {
        this.validatorD1 = validatorD1;
    }

    public MatrixValidator getValidatorD5() {
        return validatorD5;
    }


    public void setValidatorD5(MatrixValidator validatorD5) {
        this.validatorD5 = validatorD5;
    }

    public MatrixValidator getValidatorD20() {
        return validatorD20;
    }

    public void setValidatorD20(MatrixValidator validatorD20) {
        this.validatorD20 = validatorD20;
    }

    public StockPrediction getPrediction() {
        return prediction;
    }

    public void setPrediction(StockPrediction prediction) {
        this.prediction = prediction;
    }

    public List<DetailData> getData() {
        return data;
    }

    public void setData(List<DetailData> data) {
        this.data = data;
    }
}

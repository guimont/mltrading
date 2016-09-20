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
        detail.setValidatorD1(mls.getValidator(PredictionPeriodicity.D1));
        detail.setValidatorD5(mls.getValidator(PredictionPeriodicity.D5));
        detail.setValidatorD20(mls.getValidator(PredictionPeriodicity.D20));
        //detail.setSector();
        detail.setData(populateData(s));
        return detail;
    }


    private static double findPredD1(List<MLPerformances> perfList, String date) {
        date = date.substring(0,10);
        for (MLPerformances p: perfList) {
            if (p.getMlD1().getDate().substring(0, 10).compareTo(date) == 0) {
                return p.getMlD1().getPrediction();
            }
        }

        return 0; //not found not normal
    }

    private static MLPerformance findPredD5(List<MLPerformances> perfList, String date) {
        date = date.substring(0,10);
        for (MLPerformances p: perfList) {
            if (p.getMlD5() != null && (p.getMlD5().getDate().substring(0, 10).compareTo(date) == 0))
                return p.getMlD5();
        }
        return null; //not found not normal
    }

    private static MLPerformance findPredD20(List<MLPerformances> perfList, String date) {
        date = date.substring(0,10);
        for (MLPerformances p: perfList) {
            if (p.getMlD20() != null && p.getMlD20().getDate().substring(0, 10).compareTo(date) == 0)
                return p.getMlD20();
        }
        return null; //not found not normal
    }


    private static MLPerformance findPredD40(List<MLPerformances> perfList, String date) {
        date = date.substring(0,10);
        for (MLPerformances p: perfList) {
            if (p.getMlD40() != null && p.getMlD40().getDate().substring(0, 10).compareTo(date) == 0)
                return p.getMlD40();
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

            MLPerformance perf5 = findPredD5(mls.getStatus().getPerfList(), he.getDay());
            d.setPredD5(perf5.getPrediction());
            d.setSignD5(perf5.isSign());


            MLPerformance perf20 = findPredD20(mls.getStatus().getPerfList(), he.getDay());
            d.setPredD20(perf20.getPrediction());
            d.setSignD20(perf20.isSign());

            MLPerformance perf40 = findPredD40(mls.getStatus().getPerfList(), he.getDay());
            d.setPredD40(perf40.getPrediction());
            d.setSignD40(perf40.isSign());
            data.add(d);

        }

        int size = mls.getStatus().getPerfList().size();
        for (int i=1; i<40; i++) {
            DetailData d = new DetailData();
            d.setDate("J+"+i);
            /*if (i < 5) //TODO ugly code
                if (mls.getStatus().getPerfList().get(size - 5 + i).getMlD5() != null)
                    d.setPredD5(mls.getStatus().getPerfList().get(size - 5 + i).getMlD5().getPrediction());
            */
            if ( i - 5 < 0) d.setPredD5(mls.getStatus().getPerfList().get(size - 5 + i).getMlD5().getPrediction());
            if ( i - 20 < 0) d.setPredD20(mls.getStatus().getPerfList().get(size - 20 + i).getMlD20().getPrediction());
            d.setPredD40(mls.getStatus().getPerfList().get(size-40+i).getMlD40().getPrediction());
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

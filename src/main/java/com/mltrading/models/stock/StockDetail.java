package com.mltrading.models.stock;

import com.mltrading.ml.*;
import com.mltrading.models.stock.cache.CacheStockGeneral;
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
    private String name;
    private Stock stock;
    private Double value;

    private List<StockHistory> sector;
    private List<StockHistory> indice;
    private StockPrediction prediction;
    private List<DetailData> data;

    private static int PERIOD = 40;


    public static StockDetail populate(Stock s, String codif) {
        StockDetail detail = new StockDetail();
        if (s != null)
            log.info(codif);
        else
            log.info("s null error");

        log.info("cache size:" + CacheMLStock.getMLStockCache().size());
        MLStocks mls = CacheMLStock.getMLStockCache().get(codif);
        if (mls == null )
            log.info("mls null error");
        detail.setCode(codif);
        detail.setStock(s);
        detail.setPrediction(CacheStockGeneral.getCache().get(s.getCode()).getPrediction());
        detail.setName(CacheStockGeneral.getCache().get(s.getCode()).getName());
        //detail.setSector();
        detail.setData(populateData(codif));
        detail.setValue(CacheStockGeneral.getCache().get(s.getCode()).getOpening());

        detail.sector = StockHistory.getStockHistoryLast(s.getSector(), PERIOD);
        detail.indice = StockHistory.getStockHistoryLast("PX1", PERIOD); // code cac => use transform to match indice


        return detail;
    }


    private static MLPerformance findPred(List<MLPerformances> perfList, String date, PredictionPeriodicity periodicity) {

        try {
            date = date.substring(0,10);
            for (MLPerformances p : perfList) {
                String datePerf = p.getMl(periodicity).getDate();
                if (!datePerf.contains("J+"))
                    if (datePerf.substring(0, 10).compareTo(date) == 0) {
                        return p.getMl(periodicity);
                    }
            }
        } catch (Exception e) {
            log.error(e.toString());
        }

        return null; //not found
    }




    private static List<DetailData> populateData(String codif) {
        List<DetailData> data = new ArrayList<>();
        List<StockHistory> h = StockHistory.getStockHistoryLast(codif, PERIOD);
        MLStocks mls = CacheMLStock.getMLStockCache().get(codif);

        for (StockHistory he:h) {
            DetailData d = new DetailData();
            d.setDate(he.getDay().substring(5,10));
            d.setValue(he.getValue());
            if (he.getOpening() != null) d.setOpening(he.getOpening());
            else d.setOpening(he.getValue());


            MLPerformance perf = findPred(mls.getStatus().getPerfList(), he.getDay(), PredictionPeriodicity.D5);
            if (perf != null) {
                d.setPredD1(perf.getPrediction());
            }



            MLPerformance perf5 = findPred(mls.getStatus().getPerfList(), he.getDay(), PredictionPeriodicity.D5);
            if (perf5 != null) {
                d.setPredD5(perf5.getPrediction());
                d.setSignD5(perf5.isSign());
            }


            MLPerformance perf20 = findPred(mls.getStatus().getPerfList(), he.getDay(), PredictionPeriodicity.D20);
            if (perf20 != null) {
                d.setPredD20(perf20.getPrediction());
                d.setSignD20(perf20.isSign());
            }

            MLPerformance perf40 = findPred(mls.getStatus().getPerfList(), he.getDay(), PredictionPeriodicity.D40);
            if (perf40 != null) {
                d.setPredD40(perf40.getPrediction());
                d.setSignD40(perf40.isSign());
            }
            data.add(d);

        }

        int size = mls.getStatus().getPerfList().size();
        for (int i=1; i<40; i++) {
            DetailData d = new DetailData();
            d.setDate("J+"+i);
            /*if (i < 5) //TODO ugly codes
                if (mls.getStatus().getPerfList().get(size - 5 + i).getMlD5() != null)
                    d.setPredD5(mls.getStatus().getPerfList().get(size - 5 + i).getMlD5().getPrediction());
            */
            if ( i - 5 < 0) d.setPredD5(mls.getStatus().getPerfList().get(size - 5 + i).getMl(PredictionPeriodicity.D5).getPrediction());
            if ( i - 20 < 0) d.setPredD20(mls.getStatus().getPerfList().get(size - 20 + i).getMl(PredictionPeriodicity.D20).getPrediction());
            d.setPredD40(mls.getStatus().getPerfList().get(size-40+i).getMl(PredictionPeriodicity.D40).getPrediction());
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StockHistory> getSector() {
        return sector;
    }

    public void setSector(List<StockHistory> sector) {
        this.sector = sector;
    }

    public List<StockHistory> getIndice() {
        return indice;
    }

    public void setIndice(List<StockHistory> indice) {
        this.indice = indice;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}

package com.mltrading.models.stock;

import com.mltrading.ml.*;
import com.mltrading.ml.model.ModelType;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by gmo on 03/03/2016.
 */
public class StockDetail implements Serializable{

    private static final Logger log = LoggerFactory.getLogger(StockDetail.class);
    private String code;
    private String name;
    private Stock stock;
    private Double value;

    private Double variation;
    private Double open;
    private Double high;
    private Double close;
    private Double low;

    private Double volume;

    private String indiceRef;



    private List<StockHistory> sector;
    private List<StockHistory> indice;
    private StockPrediction prediction;
    private List<DetailData> data;
    private List<StockDocument> documents;
    private List<StockDocument> dailys;

    private static int PERIOD = 80;
    private String sectorCode;

    private Double RFD20[];
    private Double GBTD20[];


    public static StockDetail populate(Stock s, StockHistory sh) {
        StockDetail detail = new StockDetail();

        log.info("cache size:" + CacheMLStock.getMLStockCache().size());

        detail.setCode(sh.getCodif());
        detail.setStock(s);
        detail.setPrediction(sh.getPrediction());
        detail.setName(sh.getName());
        detail.setData(populateData(sh.getCodif()));

        StockGeneral sg = CacheStockGeneral.getCache().get(sh.getCode());
        detail.setVariation(sg.getVariation());
        detail.setHigh(sg.getHighest());
        detail.setOpen(sg.getOpening());
        detail.setLow(sg.getLowest());
        detail.setVolume(sg.getVolume());
        detail.setValue(sg.getValue());
        detail.setSectorCode(sg.getSector());

        detail.setPrediction(sg.getPrediction());

        detail.documents = new ArrayList<>();

        if (sg != null)
            detail.sector = StockHistory.getStockHistoryLast(sg.getSector(), PERIOD);
        else
            detail.sector = StockHistory.getStockHistoryLast(sh.getCodif(), PERIOD); //itself
        detail.indice = StockHistory.getStockHistoryLast("PX1", PERIOD); // code cac => use transform to match indice

        List<StockDocument> preList = StockDocument.getStockHistoryLastInvert(sh.getCodif(),StockDocument.TYPE_ARTICLE, PERIOD);
        preList.forEach(d -> {
            //don't take html ref .. not an article
            if (!d.getRef().toLowerCase().contains("html")) {
                String[] split = d.getRef().split("/");
                d.setRef(split[split.length - 1].replaceAll("-", " ").replaceAll(".php", "").replaceAll("bref", ""));
                detail.documents.add(d);
            }
        });


        detail.dailys = StockDocument.getStockHistoryLastInvert(sh.getCodif(),StockDocument.TYPE_DIARY, PERIOD);


        String date = StockHistory.getLastDateHistory(sh.getCodif());
        MLStocks mlStocks = CacheMLStock.getMLStockCache().get(sh.getCodif());

        if (mlStocks != null) {
            MatrixValidator matrixValidator = mlStocks.getModel(PredictionPeriodicity.D20, ModelType.RANDOMFOREST).getValidator();
            FeaturesStock featuresStock = FeaturesStock.createRT(sh.getCodif(), matrixValidator, date);
            detail.setRFD20(featuresStock.getVector());

            matrixValidator = mlStocks.getModel(PredictionPeriodicity.D20, ModelType.GRADIANTBOOSTTREE).getValidator();
            featuresStock = FeaturesStock.createRT(sh.getCodif(), matrixValidator, date);
            detail.setGBTD20(featuresStock.getVector());
        }

        //preList.sort(Comparator.comparing(d -> d.getDate()));

        return detail;
    }

    public static StockDetail populateLight(StockGeneral sh) {
        StockDetail detail = new StockDetail();

        log.info("cache size:" + CacheMLStock.getMLStockCache().size());

        detail.setCode(sh.getCodif());
        detail.setPrediction(sh.getPrediction());
        detail.setName(sh.getName());
        detail.setSectorCode(sh.getSector());
        //detail.setSector();
        detail.setData(populateData(sh.getCodif()));
        detail.setValue(sh.getOpening());

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

        ModelType type = ModelType.RANDOMFOREST;

        for (StockHistory he:h) {
            DetailData d = new DetailData();
            d.setDate(he.getDay().substring(5,10));
            d.setValue(he.getValue());
            if (he.getOpening() != null) d.setOpening(he.getOpening());
            else d.setOpening(he.getValue());


            MLPerformance perf = findPred(mls.getStatus(type).getPerfList(), he.getDay(), PredictionPeriodicity.D5);
            if (perf != null) {
                d.setPredD1(perf.getPrediction());
            }



            MLPerformance perf5 = findPred(mls.getStatus(type).getPerfList(), he.getDay(), PredictionPeriodicity.D5);
            if (perf5 != null) {
                d.setPredD5(perf5.getPrediction());
                d.setSignD5(perf5.isSign());
            }


            MLPerformance perf20 = findPred(mls.getStatus(type).getPerfList(), he.getDay(), PredictionPeriodicity.D20);
            if (perf20 != null) {
                d.setPredD20(perf20.getPrediction());
                d.setSignD20(perf20.isSign());
            }

            MLPerformance perf40 = findPred(mls.getStatus(type).getPerfList(), he.getDay(), PredictionPeriodicity.D40);
            if (perf40 != null) {
                d.setPredD40(perf40.getPrediction());
                d.setSignD40(perf40.isSign());
            }
            data.add(d);

        }

        int size = mls.getStatus(type).getPerfList().size();
        for (int i=1; i<40; i++) {
            DetailData d = new DetailData();
            d.setDate("J+"+i);
            /*if (i < 5) //TODO ugly codes
                if (mls.getStatus().getPerfList().get(size - 5 + i).getMlD5() != null)
                    d.setPredD5(mls.getStatus().getPerfList().get(size - 5 + i).getMlD5().getPrediction());
            */
            if ( i - 5 < 0) d.setPredD5(mls.getStatus(type).getPerfList().get(size - 5 + i).getMl(PredictionPeriodicity.D5).getPrediction());
            if ( i - 20 < 0) d.setPredD20(mls.getStatus(type).getPerfList().get(size - 20 + i).getMl(PredictionPeriodicity.D20).getPrediction());
            d.setPredD40(mls.getStatus(type).getPerfList().get(size-40+i).getMl(PredictionPeriodicity.D40).getPrediction());
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

    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    public List<StockDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<StockDocument> documents) {
        this.documents = documents;
    }


    public Double getVariation() {
        return variation;
    }

    public void setVariation(Double variation) {
        this.variation = variation;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getIndiceRef() {
        return indiceRef;
    }

    public void setIndiceRef(String indiceRef) {
        this.indiceRef = indiceRef;
    }

    public List<StockDocument> getDailys() {
        return dailys;
    }

    public void setDailys(List<StockDocument> dailys) {
        this.dailys = dailys;
    }

    public Double[] getRFD20() {
        return RFD20;
    }

    public void setRFD20(Double[] RFD20) {
        this.RFD20 = RFD20;
    }

    public Double[] getGBTD20() {
        return GBTD20;
    }

    public void setGBTD20(Double[] GBTD20) {
        this.GBTD20 = GBTD20;
    }
}

package com.mltrading.models.parser;

import com.mltrading.dao.InfluxDaoConnector;

import com.mltrading.dao.TimeSeriesDao.impl.TimeSeriesDaoInfluxImpl;
import com.mltrading.models.stock.*;
import com.mltrading.models.stock.cache.CacheRawMaterial;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockIndice;
import com.mltrading.models.stock.cache.CacheStockSector;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 13/10/2015.
 */
public class Analyse {

    private static final Logger log = LoggerFactory.getLogger(Analyse.class);
    HashMap<String,List<Container>> list = new HashMap<>();

    private static String VALUE = "value";
    private static String MM20 = "mm20";
    private static String MM50 = "mm50";
    private static String MME12 = "mme12";
    private static String MME26 = "mme26";
    private static String STDDEV = "stddev";
    private static String MOMENTUM = "momentum";

    private static int columnValue = TimeSeriesDaoInfluxImpl.VALUE_COLUMN_HIST;



    public void processAll() {

        for (StockGeneral g: CacheStockGeneral.getIsinCache().values()) {
            processAnalysisAll(g.getCodif());
        }

        for (StockIndice g : CacheStockIndice.getIndiceCache().values()) {
            processAnalysisAll(g.getCode());
        }

        for (StockSector g : CacheStockSector.getSectorCache().values()) {
            processAnalysisAll(g.getCode());
        }

        for (StockRawMat g : CacheRawMaterial.getCache().values()) {
            processAnalysisAll(g.getCode());
        }

        processAnalysisAll("VCAC");
    }


    static int MARGIN = 4;

    public void processDaily(int period) {
        List<String> dateList = null;

        log.info("Start analyse for period: " + period);

        for (StockGeneral g: CacheStockGeneral.getIsinCache().values()) {
            dateList = StockHistory.getDateHistoryListOffsetLimit(g.getCodif(),50,period);
            if (dateList != null) {
                for (String date : dateList) {
                    processAnalysisSpecific(g.getCodif(), date);
                }
            }
        }

        /**
         * not all french indice so date could be different .. add an error margin
         */
        for (StockIndice g : CacheStockIndice.getIndiceCache().values()) {
            dateList = StockHistory.getDateHistoryListOffsetLimit(g.getCodif(),50,period+MARGIN);
            if (dateList != null) {
                for (String date : dateList) {
                    processAnalysisSpecific(g.getCode(), date);
                }
            }
        }

        for (StockSector g : CacheStockSector.getSectorCache().values()) {

            dateList = StockHistory.getDateHistoryListOffsetLimit(g.getCodif(),50,period);
            if (dateList != null) {
                for (String date : dateList) {
                    processAnalysisSpecific(g.getCode(), date);
                }
            }
        }

        /**
         * not all french indice so date could be different .. add an error margin
         */
        for (StockRawMat g : CacheRawMaterial.getCache().values()) {

            dateList = StockHistory.getDateHistoryListOffsetLimit(g.getCodif(),50,period+MARGIN);
            if (dateList != null) {
                for (String date : dateList) {
                    processAnalysisSpecific(g.getCode(), date);
                }
            }
        }


        dateList = StockHistory.getDateHistoryListOffsetLimit("VCAC",50,period);
        if (dateList != null) {
            for (String date:dateList) {
                processAnalysisSpecific("VCAC", date);
            }
        }

        log.info("End analyse for period: " + period);
    }



    public void processAllRaw() {

        for (StockRawMat g : CacheRawMaterial.getCache().values()) {
            processAnalysisAll(g.getCode());
        }
    }


    public void processSectorAll() {

        for (StockSector g : CacheStockSector.getSectorCache().values()) {
            processAnalysisAll(g.getCode());
        }

    }

    public void processVcacAll() {

        processAnalysisAll("VCAC");
    }



    public void processIndiceAll() {

        for (StockIndice g : CacheStockIndice.getIndiceCache().values()) {
            processAnalysisAll(g.getCode());
        }

    }

    public void processAnalysisSpecific(String code, String date) {

        List<Container> cList = new ArrayList<>();

        QueryResult res = InfluxDaoConnector.getPoints("SELECT * FROM " + code + " where time > '" + date +"' - 120d and time <= '" + date +"'",StockHistory.dbName);

        if (res.getResults().get(0).getSeries() == null || res.getResults().get(0).getSeries().get(0).getValues() == null) return; //resultat empry

        int len = res.getResults().get(0).getSeries().get(0).getValues().size();
        int index = len-1;

        if (len < 50) {
            log.warn("Not enough element in code "+ code +". Cannot launch AT parser");
            return;
        }

        double ref_mme26 = 0;
        double ref_mme12 = 0;

        Container c = new Container(res.getResults().get(0).getSeries().get(0).getValues().get(index).get(0).toString());
        c.indice.put(MM20,mmRange(res, code, index, 20));
        c.indice.put(MM50,mmRange(res, code, index, 50));
        c.indice.put(STDDEV,stddevRange(res, code, index, 20));
        ref_mme26 = mmeRange(res, index, 26, 0.075, columnValue);
        ref_mme12 = mmeRange(res, index, 12, 0.15, columnValue);
        c.indice.put(MME12, Double.toString(ref_mme12));
        c.indice.put(MME26, Double.toString(ref_mme26));
        c.indice.put(MOMENTUM, Double.toString(momentum(res,index, columnValue)));
        saveAnalysis(code, c);
        cList.add(c);

        list.put(code,cList);
    }




    public void processAnalysisAll(String code) {

        List<Container> cList = new ArrayList<>();

        QueryResult res = InfluxDaoConnector.getPoints("SELECT * FROM " + code,StockHistory.dbName);

        if (res.getResults().get(0).getSeries() == null || res.getResults().get(0).getSeries().get(0).getValues() == null) return; //resultat empry

        int len = res.getResults().get(0).getSeries().get(0).getValues().size();

        if (len < 51) {
            log.warn("Not enough element in code "+ code +". Cannot launch AT parser");
            return;
        }

        double ref_mme12 = Double.parseDouble(mmRange(res, code, 50, 12));
        double ref_mme26 = Double.parseDouble(mmRange(res, code, 50, 26));


        //TODO not always 1 for value => consensus or other
        for (int index = 50; index <len;index++ ) {
            Container c = new Container(res.getResults().get(0).getSeries().get(0).getValues().get(index).get(0).toString());
            c.indice.put(VALUE, res.getResults().get(0).getSeries().get(0).getValues().get(index).get(columnValue).toString());
            c.indice.put(MM20,mmRange(res, code, index, 20));
            c.indice.put(MM50,mmRange(res, code, index, 50));
            c.indice.put(STDDEV,stddevRange(res, code, index, 20));
            ref_mme26 = mmeRange(res, index, 26, 0.075, columnValue);
            ref_mme12 = mmeRange(res, index, 12, 0.15, columnValue);
            c.indice.put(MME12, Double.toString(ref_mme12));
            c.indice.put(MME26, Double.toString(ref_mme26));
            c.indice.put(MOMENTUM, Double.toString(momentum(res,index, columnValue)));
            saveAnalysis(code, c);
            cList.add(c);
        }
        list.put(code,cList);
    }


    public void saveAnalysis(String code, Container c) {

        BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);

        Point pt = Point.measurement(code+"T").time(new DateTime(c.getDate()).getMillis(), TimeUnit.MILLISECONDS)
            //.field(VALUE, c.getIndice().get(VALUE))
            .field(MM20, c.getIndice().get(MM20))
            .field(MM50, c.getIndice().get(MM50))
            .field(STDDEV, c.getIndice().get(STDDEV))
            .field(MME12, c.getIndice().get(MME12))
            .field(MME26, c.getIndice().get(MME26))
            .field(MOMENTUM, c.getIndice().get(MOMENTUM))
            .build();
        bp.point(pt);

        InfluxDaoConnector.writePoints(bp);

    }


    /**
     * cours du jour - cours du jour-12=> donne la vitesse de changement
     * @param res
     * @param index
     * @return
     */
    public Double momentum(QueryResult res, int index, int column) {
        double val = Double.parseDouble(res.getResults().get(0).getSeries().get(0).getValues().get(index).get(column).toString());
        double valPast = Double.parseDouble(res.getResults().get(0).getSeries().get(0).getValues().get(index - 12).get(column).toString());
        return val-valPast;
    }

    public Double mmeRange(QueryResult res, int index, int range, double constant, int column) {
        double num = 0, denum =0;
        double c = (1-constant);
        for (int i=0; i<range;i++) {
            double val = Double.parseDouble(res.getResults().get(0).getSeries().get(0).getValues().get(index-i).get(column).toString());
            double coef = Math.pow(c, i);
            num += val * coef;
            denum += coef;
        }

        return num/denum;
    }


    /**
     * ecart type sur une periode donné
     * @param res
     * @param code
     * @param index
     * @param range
     * @return
     */
    public String stddevRange(QueryResult res,String code, int index, int range) {
        List<Object> lStart = res.getResults().get(0).getSeries().get(0).getValues().get(index - range);
        List<Object> lEnd = res.getResults().get(0).getSeries().get(0).getValues().get(index);

        if (code == "GOLD") {
            int breakPoint = 1;
        }

        String query = "SELECT stddev(value)*2 FROM "+code+" where time > '" + lStart.get(0) + "' and time < '"+ lEnd.get(0) + "'";
        QueryResult meanQ = InfluxDaoConnector.getPoints(query, StockHistory.dbName);

        return meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString();
    }


    public String mmRange(QueryResult res, String code, int index, int range) {

        if (index < range) {
            log.error("index could not be less than range");
            return null;
        }

        List<Object> lStart = res.getResults().get(0).getSeries().get(0).getValues().get(index - range);
        List<Object> lEnd = res.getResults().get(0).getSeries().get(0).getValues().get(index);

        System.out.print(lStart.get(0).toString());

        String query = "SELECT mean(value) FROM "+code+" where time > '" + lStart.get(0) + "' and time < '"+ lEnd.get(0) + "'";
        QueryResult meanQ = InfluxDaoConnector.getPoints(query, StockHistory.dbName);

        return meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString();
    }



    private class Container {
        String date;
        Map<String,String> indice;

        public Container(String date) {
            indice = new HashMap<>();
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Map<String, String> getIndice() {
            return indice;
        }

        public void setIndice(Map<String, String> indice) {
            this.indice = indice;
        }
    }



}

package com.mltrading.models.parser;

import com.mltrading.dao.InfluxDaoConnector;

import com.mltrading.dao.TimeSeriesDao.impl.TimeSeriesDaoInfluxImpl;
import com.mltrading.models.stock.*;
import com.mltrading.models.stock.cache.*;
import net.finmath.timeseries.models.parametric.GARCH;
import org.apache.commons.lang3.ArrayUtils;
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

    private static String VALUE = "value";
    private static String MM20 = "mm20";
    private static String MM50 = "mm50";
    private static String MME12 = "mme12";
    private static String MME26 = "mme26";
    private static String STDDEV = "stddev";
    private static String MOMENTUM = "momentum";
    private static String GARCH_20 = "garch20";
    private static String GARCHVOL_20 = "garchvol20";
    private static String GARCH_50 = "garch50";
    private static String GARCHVOL_50 = "garchvol50";
    private static String GARCH_100 = "garch100";
    private static String GARCHVOL_100 = "garchvol100";


    private static int columnValue = TimeSeriesDaoInfluxImpl.VALUE_COLUMN_HIST;



    public void processAll() throws InterruptedException {

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

    public void processDaily(int period) throws InterruptedException {
        List<String> dateList = null;

        log.info("Start analyse for period: " + period);

        for (StockGeneral g: CacheStockGeneral.getIsinCache().values()) {
            dateList = StockHistory.getDateHistoryListOffsetLimit(g.getCodif(),period);
            if (dateList != null) {
                for (String date : dateList) {
                    if (CacheStockAnalyse.CacheStockAnalyseHolder().isInStockAanlyse(g.getCodif(), date))
                        continue;
                    processAnalysisSpecific(g.getCodif(), date);
                }
            }
        }

        for (StockGeneral g: CacheStockGeneral.getIsinExCache().values()) {
            dateList = StockHistory.getDateHistoryListOffsetLimit(g.getCodif(),period);
            if (dateList != null) {
                for (String date : dateList) {
                    if (CacheStockAnalyse.CacheStockAnalyseHolder().isInStockAanlyse(g.getCodif(), date))
                        continue;
                    processAnalysisSpecific(g.getCodif(), date);
                }
            }
        }

        /**
         * not all french indice so date could be different .. add an error margin
         */
        for (StockIndice g : CacheStockIndice.getIndiceCache().values()) {
            dateList = StockHistory.getDateHistoryListOffsetLimit(g.getCodif(),period+MARGIN);
            if (dateList != null) {
                for (String date : dateList) {
                    if (CacheStockAnalyse.CacheStockAnalyseHolder().isInStockAanlyse(g.getCodif(), date))
                        continue;
                    processAnalysisSpecific(g.getCode(), date);
                }
            }
        }

        for (StockSector g : CacheStockSector.getSectorCache().values()) {

            dateList = StockHistory.getDateHistoryListOffsetLimit(g.getCodif(), period);
            if (dateList != null) {
                for (String date : dateList) {
                    if (CacheStockAnalyse.CacheStockAnalyseHolder().isInStockAanlyse(g.getCodif(), date))
                        continue;
                    processAnalysisSpecific(g.getCode(), date);
                }
            }
        }

        /**
         * not all french indice so date could be different .. add an error margin
         */
        for (StockRawMat g : CacheRawMaterial.getCache().values()) {

            dateList = StockHistory.getDateHistoryListOffsetLimit(g.getCodif(),period+MARGIN);
            if (dateList != null) {
                for (String date : dateList) {
                    if (CacheStockAnalyse.CacheStockAnalyseHolder().isInStockAanlyse(g.getCodif(), date))
                        continue;
                    processAnalysisSpecific(g.getCode(), date);
                }
            }
        }


        dateList = StockHistory.getDateHistoryListOffsetLimit("VCAC",period);
        if (dateList != null) {
            for (String date:dateList) {
                if (CacheStockHistory.CacheStockHistoryHolder().isInStockHistory("VCAC", date))
                    break;
                processAnalysisSpecific("VCAC", date);
            }
        }

        log.info("End analyse for period: " + period);
    }



    public void processAllRaw() throws InterruptedException {

        for (StockRawMat g : CacheRawMaterial.getCache().values()) {
            processAnalysisAll(g.getCode());
        }
    }


    public void processSectorAll() throws InterruptedException {

        for (StockSector g : CacheStockSector.getSectorCache().values()) {
            processAnalysisAll(g.getCode());
        }

    }

    public void processVcacAll() throws InterruptedException {

        processAnalysisAll("VCAC");
    }



    public void processIndiceAll() throws InterruptedException {

        for (StockIndice g : CacheStockIndice.getIndiceCache().values()) {
            processAnalysisAll(g.getCode());
        }

    }

    public void processAnalysisSpecific(String code, String date) throws InterruptedException {

        List<Container> cList = new ArrayList<>();

        try {

            QueryResult res = InfluxDaoConnector.getPoints("SELECT * FROM " + code + " where time > '" + date + "' - 180d and time <= '" + date + "'", StockHistory.dbName);

            if (res.getResults().get(0).getSeries() == null || res.getResults().get(0).getSeries().get(0).getValues() == null)
                return; //resultat empry

            int len = res.getResults().get(0).getSeries().get(0).getValues().size();
            int index = len - 1;

            if (len <= 100) {
                log.warn("Not enough element in code " + code + ". Cannot launch AT parser");
                return;
            }


            Container c = new Container(res.getResults().get(0).getSeries().get(0).getValues().get(index).get(0).toString());
            c.indice.put(MM20, mmRange(res, code, index, 20));
            c.indice.put(MM50, mmRange(res, code, index, 50));
            c.indice.put(STDDEV, stddevRange(res, code, index, 20));
            double ref_mme26 = mmeRange(res, index, 26, 0.075, columnValue);
            double ref_mme12 = mmeRange(res, index, 12, 0.15, columnValue);
            c.indice.put(MME12, Double.toString(ref_mme12));
            c.indice.put(MME26, Double.toString(ref_mme26));
            c.indice.put(MOMENTUM, Double.toString(momentum(res, index, columnValue)));


            /**
             *GARCH */
            double[] vector = new double[100];
            int currentVectorPos = 0;
            for (int gindex = 100; gindex > 0; gindex--) {
                vector[currentVectorPos++] = Double.parseDouble(res.getResults().get(0).getSeries().get(0).getValues().get(index - gindex).get(columnValue).toString());
            }
            GARCH garch = new GARCH(vector);
            Map<String, Object> params = garch.getBestParameters();

            c.indice.put(GARCH_100, params.get("Likelihood").toString());
            c.indice.put(GARCHVOL_100, params.get("Vol").toString());

            GARCH garch50 = new GARCH(vector, 50, 99);
            params = garch50.getBestParameters();
            c.indice.put(GARCH_50, params.get("Likelihood").toString());
            c.indice.put(GARCHVOL_50, params.get("Vol").toString());

            GARCH garch20 = new GARCH(vector, 80, 99);
            params = garch20.getBestParameters();
            c.indice.put(GARCH_20, params.get("Likelihood").toString());
            c.indice.put(GARCHVOL_20, params.get("Vol").toString());

            saveAnalysis(code, c);
            cList.add(c);
        }catch (Exception e) {
            System.out.println("error in analyse for " + code +" and date: " +date);
        }

    }


    /**
     * GARCH static method
     * Not use because params have to be store for specific analysis
     *
        double[] vectorGlobal = new double[values.size()];
        int currentVectorPos = 0;
            for (StockHistory sh : values) {
            vectorGlobal[currentVectorPos++] = sh.getCurrentValue();
        }
        GARCH garchGlobal = new GARCH(vectorGlobal);
        Map<String, Object> bestParams = garchGlobal.getBestParameters();

        double omega = Double.parseDouble(bestParams.get("Omega").toString());
        double alpha = Double.parseDouble(bestParams.get("Alpha").toString());
        double beta = Double.parseDouble(bestParams.get("Beta").toString());
     * @param code
     */

    public void processAnalysisAll(String code) throws InterruptedException {

        List<Container> cList = new ArrayList<>();

        QueryResult res = InfluxDaoConnector.getPoints("SELECT * FROM " + code,StockHistory.dbName);

        if (res.getResults().get(0).getSeries() == null || res.getResults().get(0).getSeries().get(0).getValues() == null) return; //resultat empry

        int len = res.getResults().get(0).getSeries().get(0).getValues().size();

        if (len < 100) {
            log.warn("Not enough element in code "+ code +". Cannot launch AT parser");
            return;
        }


        //TODO not always 1 for value => consensus or other
        for (int index = 100; index <len;index++ ) {
            Container c = new Container(res.getResults().get(0).getSeries().get(0).getValues().get(index).get(0).toString());
            if (CacheStockAnalyse.CacheStockAnalyseHolder().isInStockAanlyse(code, c.getDate()))
                continue;
            c.indice.put(VALUE, res.getResults().get(0).getSeries().get(0).getValues().get(index).get(columnValue).toString());
            c.indice.put(MM20,mmRange(res, code, index, 20));
            c.indice.put(MM50,mmRange(res, code, index, 50));
            c.indice.put(STDDEV,stddevRange(res, code, index, 20));
            double ref_mme26 = mmeRange(res, index, 26, 0.075, columnValue);
            double ref_mme12 = mmeRange(res, index, 12, 0.15, columnValue);
            c.indice.put(MME12, Double.toString(ref_mme12));
            c.indice.put(MME26, Double.toString(ref_mme26));
            c.indice.put(MOMENTUM, Double.toString(momentum(res,index, columnValue)));


            /**
             *GARCH */
            double[] vector = new double[100];
            int currentVectorPos = 0;
            for (int gindex = 100 ; gindex> 0; gindex --) {
                vector[currentVectorPos++] = Double.parseDouble(res.getResults().get(0).getSeries().get(0).getValues().get(index-gindex).get(columnValue).toString());
            }
            GARCH garch = new GARCH(vector);
            Map<String, Object> params = garch.getBestParameters();
            c.indice.put(GARCH_100, params.get("Likelihood").toString());
            c.indice.put(GARCHVOL_100, params.get("Vol").toString());


            GARCH garch50 = new GARCH(vector,50,99);
            params = garch50.getBestParameters();
            c.indice.put(GARCH_50, params.get("Likelihood").toString());
            c.indice.put(GARCHVOL_50, params.get("Vol").toString());

            GARCH garch20 = new GARCH(vector,80,99);
            params = garch20.getBestParameters();
            c.indice.put(GARCH_20, params.get("Likelihood").toString());
            c.indice.put(GARCHVOL_20, params.get("Vol").toString());


            saveAnalysis(code, c);
            cList.add(c);


        }

    }


    public void saveAnalysis(String code, Container c) throws InterruptedException {

        BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);

        Point pt = Point.measurement(code+"T").time(new DateTime(c.getDate()).getMillis(), TimeUnit.MILLISECONDS)
            //.field(VALUE, c.getIndice().get(VALUE))
            .field(MM20, c.getIndice().get(MM20))
            .field(MM50, c.getIndice().get(MM50))
            .field(STDDEV, c.getIndice().get(STDDEV))
            .field(MME12, c.getIndice().get(MME12))
            .field(MME26, c.getIndice().get(MME26))
            .field(MOMENTUM, c.getIndice().get(MOMENTUM))
            .field(GARCH_20, c.getIndice().get(GARCH_20))
            .field(GARCHVOL_20, c.getIndice().get(GARCHVOL_20))
            .field(GARCH_50, c.getIndice().get(GARCH_50))
            .field(GARCHVOL_50, c.getIndice().get(GARCHVOL_50))
            .field(GARCH_100, c.getIndice().get(GARCH_100))
            .field(GARCHVOL_100, c.getIndice().get(GARCHVOL_100))
            .build();
        bp.point(pt);

        InfluxDaoConnector.writePoints(bp);

    }

    public static void saveAnalysis(String code, StockAnalyse sa) throws InterruptedException {

        BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);

        Point pt = Point.measurement(code+"T").time(new DateTime(sa.getDay()).getMillis(), TimeUnit.MILLISECONDS)
            //.field(VALUE, c.getIndice().get(VALUE))
            .field(MM20, sa.getMma20())
            .field(MM50, sa.getMma50())
            .field(STDDEV, sa.getStdDev())
            .field(MME12, sa.getMme12())
            .field(MME26, sa.getMme26())
            .field(MOMENTUM, sa.getMomentum())
            .field(GARCH_20, sa.getGarch20())
            .field(GARCHVOL_20, sa.getGarch_vol_20())
            .field(GARCH_50, sa.getGarch50())
            .field(GARCHVOL_50, sa.getGarch_vol_50())
            .field(GARCH_100, sa.getGarch100())
            .field(GARCHVOL_100, sa.getGarch_vol_100())
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
        try {
        List<Object> lStart = res.getResults().get(0).getSeries().get(0).getValues().get(index - range);
        List<Object> lEnd = res.getResults().get(0).getSeries().get(0).getValues().get(index);

        if (code == "EURI1Y") {
            if (index == 1362) {
                int breakPoint = 1;
            }
        }

        String query = "SELECT stddev(value)*2 FROM "+code+" where time > '" + lStart.get(0) + "' and time < '"+ lEnd.get(0) + "'";
        QueryResult meanQ = InfluxDaoConnector.getPoints(query, StockHistory.dbName);

        return meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString();
        }catch (Exception e) {
            System.out.println("code/index/range: " +code +" / "+ index+" / "+range +" " +e);
            return  null;
        }
    }


    public String mmRange(QueryResult res, String code, int index, int range) {

        try {
            if (index < range) {
                log.error("index could not be less than range");
                return null;
            }

            List<Object> lStart = res.getResults().get(0).getSeries().get(0).getValues().get(index - range);
            List<Object> lEnd = res.getResults().get(0).getSeries().get(0).getValues().get(index);

            System.out.print(lStart.get(0).toString());

            String query = "SELECT mean(value) FROM " + code + " where time > '" + lStart.get(0) + "' and time < '" + lEnd.get(0) + "'";
            QueryResult meanQ = InfluxDaoConnector.getPoints(query, StockHistory.dbName);

            return meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString();
        }catch (Exception e) {
            System.out.println("code/index/range: " +code +" / "+ index+" / "+range +" " +e);
            return  null;
        }
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

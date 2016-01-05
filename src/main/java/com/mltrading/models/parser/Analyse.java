package com.mltrading.models.parser;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.influxdb.dto.QueryResult;
import com.mltrading.models.stock.*;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 13/10/2015.
 */
public class Analyse {

    HashMap<String,List<Container>> list = new HashMap<>();

    private static String MM20 = "mm20";
    private static String MM50 = "mm50";
    private static String MME12 = "mme12";
    private static String MME26 = "mme26";
    private static String STDDEV = "stddev";
    private static String MOMENTUM = "momentum";

    private static int columnSector = 2;
    private static int columnStock = 5;
    private static int columnIndice = 5;


    public void processAll() {

        for (StockGeneral g: CacheStockGeneral.getCache().values()) {
            processAnalysisAll(g.getCode(), columnStock);
        }

        for (StockIndice g : CacheStockIndice.getIndiceCache().values()) {
            processAnalysisAll(g.getCode(), columnIndice);
        }

        for (StockSector g : CacheStockSector.getSectorCache().values()) {
            processAnalysisAll(g.getCode(), columnSector);
        }
    }

    public void processAnalysisSpecific(String code, String date, int column) {

        List<Container> cList = new ArrayList<>();

        QueryResult res = InfluxDaoConnector.getPoints("SELECT * FROM " + code+" where date = "+date +" - 20d limit 12");
        QueryResult resTech = InfluxDaoConnector.getPoints("SELECT * FROM " + code+"T where date = "+date);

        int len = res.getResults().get(0).getSeries().get(0).getValues().size();
        int index = len-1;

        double ref_mme26 = 0;
        double ref_mme12 = 0;

        Container c = new Container(res.getResults().get(0).getSeries().get(0).getValues().get(index).get(0).toString());
        c.indice.put(MM20,mmRange(res, code, index, 20));
        c.indice.put(MM50,mmRange(res, code, index, 50));
        c.indice.put(STDDEV,stddevRange(res, code, index, 20));
        ref_mme26 = mmeRange(res, index, ref_mme26, 0.075, column);
        ref_mme12 = mmeRange(res, index, ref_mme12, 0.15, column);
        c.indice.put(MME12, Double.toString(ref_mme12));
        c.indice.put(MME26, Double.toString(ref_mme26));
        c.indice.put(MOMENTUM, Double.toString(momentum(res,index, column)));
        saveAnalysis(code, c);
        cList.add(c);

        list.put(code,cList);
    }



    public void processAnalysisAll(String code, int column) {

        List<Container> cList = new ArrayList<>();

        QueryResult res = InfluxDaoConnector.getPoints("SELECT * FROM " + code);

        if (res.getResults().get(0).getSeries() == null || res.getResults().get(0).getSeries().get(0).getValues() == null) return; //resultat empry

        int len = res.getResults().get(0).getSeries().get(0).getValues().size();

        double ref_mme12 = Double.parseDouble(mmRange(res, code, 50, 12));
        double ref_mme26 = Double.parseDouble(mmRange(res, code, 50, 26));


        for (int index = 50; index <len;index++ ) {
            Container c = new Container(res.getResults().get(0).getSeries().get(0).getValues().get(index).get(0).toString());
            c.indice.put(MM20,mmRange(res, code, index, 20));
            c.indice.put(MM50,mmRange(res, code, index, 50));
            c.indice.put(STDDEV,stddevRange(res, code, index, 20));
            ref_mme26 = mmeRange(res, index, ref_mme26, 0.075, column);
            ref_mme12 = mmeRange(res, index, ref_mme12, 0.15, column);
            c.indice.put(MME12, Double.toString(ref_mme12));
            c.indice.put(MME26, Double.toString(ref_mme26));
            c.indice.put(MOMENTUM, Double.toString(momentum(res,index, column)));
            saveAnalysis(code, c);
            cList.add(c);
        }
        list.put(code,cList);
    }


    public void saveAnalysis(String code, Container c) {

        BatchPoints bp = InfluxDaoConnector.getBatchPoints();

        Point pt = Point.measurement(code+"T").time(new DateTime(c.getDate()).getMillis(), TimeUnit.MILLISECONDS)
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

    public Double mmeRange(QueryResult res, int index, double mme, double constant, int column) {
        double val = Double.parseDouble(res.getResults().get(0).getSeries().get(0).getValues().get(index).get(column).toString());
        return val*(1-constant) + mme * constant;
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

        String query = "SELECT stddev(value)*2 FROM "+code+" where time > '" + lStart.get(0) + "' and time < '"+ lEnd.get(0) + "'";
        QueryResult meanQ = InfluxDaoConnector.getPoints(query);

        return meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString();
    }


    public String mmRange(QueryResult res, String code, int index, int range) {

        List<Object> lStart = res.getResults().get(0).getSeries().get(0).getValues().get(index - range);
        List<Object> lEnd = res.getResults().get(0).getSeries().get(0).getValues().get(index);

        System.out.print(lStart.get(0).toString());

        String query = "SELECT mean(value) FROM "+code+" where time > '" + lStart.get(0) + "' and time < '"+ lEnd.get(0) + "'";
        QueryResult meanQ = InfluxDaoConnector.getPoints(query);

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

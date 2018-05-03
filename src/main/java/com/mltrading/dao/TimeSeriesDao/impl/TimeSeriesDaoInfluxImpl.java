package com.mltrading.dao.TimeSeriesDao.impl;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.dao.Requester;
import com.mltrading.dao.TimeSeriesDao.DaoChecker;
import com.mltrading.dao.TimeSeriesDao.TimeSeriesDao;
import com.mltrading.influxdb.dto.QueryRequest;

import com.mltrading.models.stock.StockAnalyse;
import com.mltrading.models.stock.StockHistory;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gmo on 08/03/2017.
 */
public class TimeSeriesDaoInfluxImpl implements TimeSeriesDao,DaoChecker {

    public static String dbName = "history";

    private static final Logger log = LoggerFactory.getLogger(TimeSeriesDaoInfluxImpl.class);



    static public int DATE_COLUMN = 0;
    static public int CONSENSUS_COLUMN_HIST = 1;
    static public int HIGHEST_COLUMN_HIST = 2;
    static public int LOWEST_COLUMN_HIST = 3;
    static public int OPENING_COLUMN_HIST = 4;
    static public int VALUE_COLUMN_HIST = 5;
    static public int VOLUME_COLUMN_HIST = 6;

    /**
     * populate stockHistory with influxdb response
     * @param sh
     * @param data
     */
    private static void populate(StockHistory sh, List data) {
        sh.setDay((String) data.get(DATE_COLUMN));
        sh.setConsensusNote((Double)  data.get(CONSENSUS_COLUMN_HIST));
        sh.setHighest((Double)  data.get(HIGHEST_COLUMN_HIST));
        sh.setLowest((Double)  data.get(LOWEST_COLUMN_HIST));
        sh.setOpening((Double)  data.get(OPENING_COLUMN_HIST));
        sh.setValue((Double)  data.get(VALUE_COLUMN_HIST));
        sh.setVolume((Double)  data.get(VOLUME_COLUMN_HIST));
    }


    /**
     * populate stockHistory with influxdb response
     * @param sh
     * @param meanQ
     * @param i
     */
    private static void populate(StockHistory sh, QueryResult meanQ, int i) {
        sh.setDay((String) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(DATE_COLUMN));
        sh.setConsensusNote((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(CONSENSUS_COLUMN_HIST));
        sh.setHighest((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(HIGHEST_COLUMN_HIST));
        sh.setLowest((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(LOWEST_COLUMN_HIST));
        sh.setOpening((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(OPENING_COLUMN_HIST));
        sh.setValue((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(VALUE_COLUMN_HIST));
        sh.setVolume((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(VOLUME_COLUMN_HIST));
    }

    /**
     * Extract TimeSeries for specific code in @param dbName table without cache
     * @param code
     * @return
     */
    @Override
    public List<StockHistory> extract(final String code) {
        return extract(code, null, null);
    }


    /**
     * Extract TimeSeries for specific code in @param dbName table with cache
     * @param code
     * @param indexCache
     * @param historyCache
     * @return
     */
    @Override
    public List<StockHistory> extract(final String code, final Map<String,Map<String, Integer>> indexCache ,final  Map<String,List<StockHistory>> historyCache ) {
        String query = "SELECT * FROM "+code +" where time > '2010-01-01T00:00:00Z' ORDER BY ASC";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        if (checker(list) == false)
            return null;


        List<StockHistory> stockList = new ArrayList<>();

        Map<String, Integer> indexMap = new HashMap<>();

        for (List data :list.getResults().get(0).getSeries().get(0).getValues()) {
            StockHistory sh = new StockHistory();
            sh.setCode(code);
            populate(sh, data);
            stockList.add(sh);
            indexMap.put(sh.getDay().substring(0,10),stockList.indexOf(sh));
        }


        if (historyCache != null) historyCache.put(code,stockList);
        if (indexCache != null) indexCache.put(code, indexMap);

        return stockList;
    }

    /**
     * Extract TimeSeries for specific code and date in @param dbName table
     * @param code
     * @param date
     * @return
     */
    @Override
    public  StockHistory extractSpecific(final String code, final String date) {
        StockHistory sh = new StockHistory();

        String query = "SELECT * FROM "+code+" where time = '" + date + "'";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        sh.setCode(code);
        populate(sh, list, 0);

        return sh;
    }

    /**
     * Extract timeSeries just after date
     * @param code
     * @param date
     * @return
     */
    @Override
    public  StockHistory extractSpecificAfter(final String code, String date) {

        StockHistory sh = new StockHistory();

        String query = "SELECT * FROM " + code + " where time > '" + date + "' limit 1";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        sh.setCode(code);
        populate(sh, list, 0);

        return sh;
    }

    @Override
    public StockHistory extractSpecificBefore(final String code, final String date) {
        return null;
    }

    @Override
    public StockHistory extractSpecificOffset(final String code, final String date, int offset) {
        StockHistory sh = new StockHistory();

        String query = "SELECT * FROM "+code+" where time >= '" + date + "' limit " + offset;
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        if (checker(list) == false)
            return null;

        if (list.getResults().get(0).getSeries().get(0).getValues().size() < offset)
            return null;

        sh.setCode(code);
        populate(sh, list, offset-1);
        return sh;
    }

    @Override
    public List<StockHistory>  extractLasts(String code, int count) {

        List<StockHistory> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM "+code +" where time > '2015-06-01T00:00:00Z'";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        if (size < count)
            return null;

        for (int i = size-count; i < size; i++) {
            StockHistory sh = new StockHistory();
            sh.setCode(code);
            populate(sh, list, i);
            stockList.add(sh);
        }

        return stockList;
    }

    @Override
    public List<StockHistory> extractDateInvert(String code, String date, int offset) {
        List<StockHistory> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM " + code + " where time <= '" + date + "' and time > '"+ date + "' - "+  Integer.toString(offset*4) +"d";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        if (size < offset)
            return null;

        for (int i = size-1; stockList.size() < offset; i--) {
            StockHistory sh = new StockHistory();
            sh.setCode(code);
            populate(sh, list, i);
            stockList.add(sh);
        }

        return stockList;
    }

    @Override
    public StockHistory extractLastHistory(String code) {
        String query = "SELECT * FROM "+code +" where time > '2015-06-01T00:00:00Z'";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        StockHistory sh = new StockHistory();
        populate(sh, list, size-1);

        return sh;
    }

    @Override
    public List<StockAnalyse> extractAnalyse(String code) {
        return extractAnalyse(code, null, null);
    }

    @Override
    public StockAnalyse extractAnalyseSpecific(String code, String date) {
        return null;
    }

    @Override
    public List<StockAnalyse> extractAnalyse(String code, Map<String, Map<String, Integer>> indexCache, Map<String, List<StockAnalyse>> historyCache) {
        String query = "SELECT * FROM " + code + "T where time > '2010-01-01T00:00:00Z'";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        log.info("extract AT for code: " + code);

        if (checker(list) == false)
            return null;


        List<StockAnalyse> stockList = new ArrayList<>();

        Map<String, Integer> indexMap = new HashMap<>();

        int index = 0;

        for (List data :list.getResults().get(0).getSeries().get(0).getValues()) {
            index ++;

            /*if (code.equals("GOLD")) {
                log.info("index is: " + index);
                if (index == 216)
                    log.info("crash");
            }*/
            StockAnalyse a = new StockAnalyse();
            a.setDay((String)data.get(DATE_COLUMN));
            a.setMma20(new Double(data.get(MMA20_COLUMN).toString()));
            a.setMma50(new Double(data.get(MMA50_COLUMN).toString()));
            a.setMme12(new Double(data.get(MME12_COLUMN).toString()));
            a.setMme26(new Double(data.get(MME26_COLUMN).toString()));
            a.setMomentum(new Double(data.get(MOMENTUM_COLUMN).toString()));
            a.setStdDev(new Double(data.get(STDDEV_COLUMN).toString()));
            a.setMacd(a.getMme26() - a.getMme12());
            a.setGarch20(new Double(data.get(GARCH20_COLUMN).toString()));
            a.setGarch_vol_20(new Double(data.get(GARCHVOL20_COLUMN).toString()));
            a.setGarch50(new Double(data.get(GARCH50_COLUMN).toString()));
            a.setGarch_vol_50(new Double(data.get(GARCHVOL50_COLUMN).toString()));
            a.setGarch100(new Double(data.get(GARCH100_COLUMN).toString()));
            a.setGarch_vol_100(new Double(data.get(GARCHVOL100_COLUMN).toString()));
            stockList.add(a);
            indexMap.put(a.getDay().substring(0,10),stockList.indexOf(a));
        }


        if (historyCache != null) historyCache.put(code,stockList);
        if (indexCache != null) indexCache.put(code, indexMap);

        return stockList;
    }





    static private int MMA20_COLUMN = 7;
    static private int MMA50_COLUMN = 8;
    static private int MME12_COLUMN = 9;
    static private int MME26_COLUMN = 10;
    static private int MOMENTUM_COLUMN = 11;
    static private int STDDEV_COLUMN = 12;
    static private int GARCH20_COLUMN = 2;
    static private int GARCHVOL20_COLUMN = 5;
    static private int GARCH50_COLUMN = 3;
    static private int GARCHVOL50_COLUMN = 6;
    static private int GARCH100_COLUMN = 1;
    static private int GARCHVOL100_COLUMN = 4;

    public StockAnalyse getAnalyse(String code, String date) {
        StockAnalyse a = new StockAnalyse();

        String query = "SELECT * FROM " + code + "T where time = '" + date + "'";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));
        a.setDay((String) list.getResults().get(0).getSeries().get(0).getValues().get(0).get(DATE_COLUMN));
        a.setMma20(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MMA20_COLUMN).toString()));
        a.setMma50(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MMA50_COLUMN).toString()));
        a.setMme12(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MME12_COLUMN).toString()));
        a.setMme26(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MME26_COLUMN).toString()));
        a.setMomentum(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MOMENTUM_COLUMN).toString()));
        a.setStdDev(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(STDDEV_COLUMN).toString()));
        a.setMacd(a.getMme26()-a.getMme12());
        a.setGarch20(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(GARCH20_COLUMN).toString()));
        a.setGarch_vol_20(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(GARCHVOL20_COLUMN).toString()));
        a.setGarch50(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(GARCH50_COLUMN).toString()));
        a.setGarch_vol_50(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(GARCHVOL50_COLUMN).toString()));
        a.setGarch100(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(GARCH100_COLUMN).toString()));
        a.setGarch_vol_100(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(GARCHVOL100_COLUMN).toString()));

        return a;
    }


}

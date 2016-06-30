package com.mltrading.models.stock;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryResult;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 06/12/2015.
 */
public class StockRawMat extends StockHistory {

    private String url;

    public StockRawMat(String code, String name, String url) {
        this.url =  url;
        this.setCode(code);
        this.setName(name);
    }

    public StockRawMat(String code) {
        this.setCode(code);
    }

    public StockRawMat(StockRawMat r) {
        this.url =  r.getUrl();
        this.setCode(r.getCode());
        this.setName(r.getName());
    }

    public void setDayInvest(String day) {

        this.day = day;
        String DD = day.substring(0, 2);
        String MM = day.substring(3,5);
        String YY = day.substring(6,10);
        timeInsert = new DateTime( YY + "-" + MM + "-" + DD);
    }


    public String getUrl() {
        return url;
    }

    static public int DATE_COLUMN = 0;
    static public int HIGHEST_COLUMN_RAW = 2;
    static public int LOWEST_COLUMN_RAW = 3;
    static public int OPENING_COLUMN_RAW = 4;
    static public int VALUE_COLUMN_RAW = 5;

    public static void populate(StockHistory sh, QueryResult meanQ, int i) {
        sh.setDay((String) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(DATE_COLUMN));
        sh.setHighest((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(HIGHEST_COLUMN_RAW));
        sh.setLowest((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(LOWEST_COLUMN_RAW));
        sh.setOpening((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(OPENING_COLUMN_RAW));
        sh.setValue((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(VALUE_COLUMN_RAW));
    }

    public static List<StockRawMat> getStockRawDateInvert(final String code, final String date, int offset) {

        List<StockRawMat> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        /**
         * bug in data miss too much so increase coeff offset but temporary solution
         */
        String query = "SELECT * FROM " + code + " where time <= '" + date + "' and time > '"+ date + "' - "+  Integer.toString(offset*10) +"d";
        QueryResult list = InfluxDaoConnector.getPoints(query);

        if (list.getResults().get(0).getSeries().get(0).getValues().size()< offset)
            return null;

        for (int i = offset; stockList.size() < offset; i--) {
            StockRawMat sh = new StockRawMat(code);
            sh.setCode(code);
            populate(sh, list, i);
            stockList.add(sh);
        }

        return stockList;

    }


    /**
     * return last max StockHistory
     * @param code
     * @param max
     * @return O or max last StockHistory
     */
    public static List<StockRawMat> getStockIndiceLastInvert(final String code, int max) {

        List<StockRawMat> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM "+code +" where time > '2014-06-01T00:00:00Z'";
        QueryResult list = InfluxDaoConnector.getPoints(query);

        if (list == null || list.getResults() == null || list.getResults().get(0).getSeries() == null)
            return null;

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        int current = size < max ?  0 : size-max-1;


        for (int i = size-1; i > current ; i--) {
            StockRawMat sr = new StockRawMat(code);
            populate(sr, list, i);
            stockList.add(sr);
        }

        return stockList;

    }


}

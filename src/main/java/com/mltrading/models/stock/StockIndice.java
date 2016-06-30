package com.mltrading.models.stock;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 06/12/2015.
 */
public class StockIndice extends StockHistory {

    public StockIndice(String code, String name) {
        this.setCode(code);
        this.setName(name);
    }

    public StockIndice(String code) {
        this.setCode(code);
    }

//TODO update this for real indice
    //probleme with belgium indice
    static public String translate(String investir) {

        for (StockIndice si:CacheStockIndice.getIndiceCache().values()) {
            if ("cac 40".equalsIgnoreCase(si.getName()))
                return si.getCode();
        }

        return null;
    }

    static public int DATE_COLUMN = 0;
    static public int HIGHEST_COLUMN_IND = 2;
    static public int LOWEST_COLUMN_IND = 3;
    static public int OPENING_COLUMN_IND = 4;
    static public int VALUE_COLUMN_IND = 5;

    public static void populate(StockIndice sh, QueryResult meanQ, int i) {
        sh.setDay((String) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(DATE_COLUMN));
        sh.setHighest((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(HIGHEST_COLUMN_IND));
        sh.setLowest((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(LOWEST_COLUMN_IND));
        sh.setOpening((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(OPENING_COLUMN_IND));
        sh.setValue((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(VALUE_COLUMN_IND));
    }


    public static List<StockIndice>getStockIndiceDateInvert(final String code, final String date, int offset) {

        List<StockIndice> stockList = new ArrayList<>();
        int decal = offset < 3 ? offset*6 : offset*4;
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM " + code + " where time <= '" + date + "' and time > '"+ date + "' - "+ Integer.toString(decal)  +"d";
        QueryResult list = InfluxDaoConnector.getPoints(query);

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();
        if (size < offset)
            return null;

        for (int i = size-1; stockList.size() < offset; i--) {
            StockIndice si = new StockIndice(code);
            populate(si, list, i);
            stockList.add(si);
        }

        return stockList;
    }


    /**
     * return last max StockHistory
     * @param code
     * @param max
     * @return O or max last StockHistory
     */
    public static List<StockIndice> getStockIndiceLastInvert(final String code, int max) {

        List<StockIndice> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM "+code +" where time > '2014-06-01T00:00:00Z'";
        QueryResult list = InfluxDaoConnector.getPoints(query);

        if (list == null || list.getResults() == null || list.getResults().get(0).getSeries() == null)
            return null;

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        int current = size < max ?  0 : size-max-1;


        for (int i = size-1; i > current ; i--) {
            StockIndice si = new StockIndice(code);
            populate(si, list, i);
            stockList.add(si);
        }

        return stockList;

    }

}

package com.mltrading.models.stock;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 15/12/2015.
 */
public class StockSector extends StockHistory{



    private String urlInvestir;


    public StockSector(String code) {
        this.setCode(code);
    }


    public StockSector(String code, String name, String place) {
        this.setCode(code);
        this.setPlace(place);
        this.setName(name);
    }

    public StockSector(String code, String name, String place, String url) {
        this.setCode(code);
        this.setPlace(place);
        this.setName(name);
        this.setUrlInvestir(url);
    }


    static public String translate(String investir) {

        return null;
    }

    public String getUrlInvestir() {
        return urlInvestir;
    }

    public void setUrlInvestir(String urlInvestir) {
        this.urlInvestir = urlInvestir;
    }

    static public int DATE_COLUMN = 0;
    static public int OPENING_COLUMN_SECT = 2;
    static public int VALUE_COLUMN_SECT = 3;

    public static void populate(StockSector sh, QueryResult meanQ, int i) {
        sh.setDay((String) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(DATE_COLUMN));
        sh.setOpening((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(OPENING_COLUMN_SECT));
        sh.setValue((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(VALUE_COLUMN_SECT));
    }


    public static List<StockSector> getStockSectorDateInvert(final String code, final String date, int offset) {

        List<StockSector> stockList = new ArrayList<>();
        int decal = offset < 3 ? offset*6 : offset*4;
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM " + code + " where time <= '" + date + "' and time > '"+ date + "' - "+ Integer.toString(decal)  +"d";
        QueryResult list = InfluxDaoConnector.getPoints(query);

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();
        if (size < offset)
            return null;

        for (int i = size-1; stockList.size() < offset; i--) {
            StockSector ss = new StockSector(code);
            populate(ss, list, i);
            stockList.add(ss);
        }

        return stockList;

    }

    /**
     * return last max StockHistory
     * @param code
     * @param max
     * @return O or max last StockHistory
     */
    public static List<StockSector> getStockSectorLastInvert(final String code, int max) {

        List<StockSector> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM "+code +" where time > '2014-06-01T00:00:00Z'";
        QueryResult list = InfluxDaoConnector.getPoints(query);

        if (list == null || list.getResults() == null || list.getResults().get(0).getSeries() == null)
            return null;

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        int current = size < max ?  0 : size-max-1;


        for (int i = size-1; i > current ; i--) {
            StockSector ss = new StockSector(code);
            populate(ss, list, i);
            stockList.add(ss);
        }

        return stockList;

    }


}

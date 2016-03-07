package com.mltrading.models.stock;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 15/12/2015.
 */
public class StockSector extends StockHistory{

    public StockSector(String code) {
        this.setCode(code);
    }


    public StockSector(String code, String name, String place) {
        this.setCode(code);
        this.setPlace(place);
        this.setName(name);
    }


    static public String translate(String investir) {

        return null;
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

}

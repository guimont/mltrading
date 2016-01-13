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


    public static List<StockSector> getStockSectorDateInvert(final String code, final String date, int offset) {

        List<StockSector> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM " + code + " where time <= '" + date + "' and time > '"+ date + "' - "+ Integer.toString(offset*2)  +"d";
        QueryResult list = InfluxDaoConnector.getPoints(query);

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();
        if (size < offset)
            return null;

        for (int i = size-1; stockList.size() < offset; i--) {
            StockSector ss = new StockSector(code);
            ss.setDay((String) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(0));
            ss.setOpening((Double) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(1));
            ss.setValue((Double) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(2));
            stockList.add(ss);
        }

        return stockList;

    }

}

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


    static public String translate(String investir) {

        for (StockIndice si:CacheStockIndice.getIndiceCache().values()) {
            if (investir.equalsIgnoreCase(si.getName()))
                return si.getCode();
        }

        return null;
    }


    public static List<StockIndice> getStockIndiceDateInvert(final String code, final String date, int offset) {

        List<StockIndice> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM " + code + "where time <= '" + date + "' and time > '"+ date + "' - "+ Integer.toString(offset*2)  +"d";
        QueryResult list = InfluxDaoConnector.getPoints(query);

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();
        if (size < offset)
            return null;

        for (int i = size-1; stockList.size() < offset; i--) {
            StockIndice si = new StockIndice(code);
            si.setDay((String) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(0));
            si.setHighest((Double) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(1));
            si.setLowest((Double) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(2));
            si.setOpening((Double) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(3));
            si.setValue((Double) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(4));
            stockList.add(si);
        }

        return stockList;

    }
}

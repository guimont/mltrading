package com.mltrading.dao.TimeSeriesDao.impl;

import com.mltrading.dao.Requester;
import com.mltrading.dao.TimeSeriesDao.DaoChecker;
import com.mltrading.dao.TimeSeriesDao.DataSeriesDao;
import com.mltrading.influxdb.dto.QueryRequest;
import com.mltrading.models.stock.StockDocument;
import org.influxdb.dto.QueryResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSeriesDaoInfluxImpl implements DataSeriesDao, DaoChecker {


    static public int DATE_COLUMN = 0;
    static public int REF = 1;
    static public int SOURCE = 2;

    /**
     * populate stockHistory with influxdb response
     * @param sh
     * @param data
     */
    private static void populate(StockDocument sh, List data) {
        sh.setDay((String) data.get(DATE_COLUMN));
        sh.setRef((String)  data.get(REF));
    }



    @Override
    public List<StockDocument> extract(String code, Map<String, Map<String, Integer>> indexCache, Map<String, List<StockDocument>> historyCache) {
        String query = "SELECT * FROM "+code +" where time > '2010-01-01T00:00:00Z' ORDER BY ASC";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, StockDocument.dbName));

        if (!checker(list))
            return null;


        List<StockDocument> dataList = new ArrayList<>();

        Map<String, Integer> indexMap = new HashMap<>();

        for (List data :list.getResults().get(0).getSeries().get(0).getValues()) {
            StockDocument sh = new StockDocument();
            sh.setCode(code);
            populate(sh, data);
            dataList.add(sh);
            indexMap.put(sh.getDay(),dataList.indexOf(sh));
        }


        if (historyCache != null) historyCache.put(code,dataList);
        if (indexCache != null) indexCache.put(code, indexMap);

        return dataList;
    }


}

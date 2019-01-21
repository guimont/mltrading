package com.mltrading.dao;

import com.mltrading.dao.impl.InfluxDaoImpl;

import com.mltrading.influxdb.dto.QueryRequest;

import com.mltrading.ml.CacheMLStock;
import com.mltrading.models.stock.HistogramDocument;
import com.mltrading.models.stock.StockDocument;
import com.mltrading.models.stock.StockHistory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

/**
 * Created by gmo on 29/06/2015.
 */
public class InfluxDaoConnector {

    private InfluxDao dao;
    private static final Logger log = LoggerFactory.getLogger(InfluxDaoConnector.class);

    private static int ERROR_RATE = 100;

    public InfluxDaoConnector() {
        dao = new InfluxDaoImpl();
        dao.createConnection();

        List<String> repo = dao.getDB().describeDatabases();

        if (!repo.contains(StockHistory.dbName)) dao.createDB(StockHistory.dbName);
        if (!repo.contains(CacheMLStock.dbNameModel)) dao.createDB(CacheMLStock.dbNameModel);
        if (!repo.contains(CacheMLStock.dbNameModelPerf)) dao.createDB(CacheMLStock.dbNameModelPerf);

        if (!repo.contains(CacheMLStock.dbNameModelShort)) dao.createDB(CacheMLStock.dbNameModelShort);
        if (!repo.contains(CacheMLStock.dbNameModelShortPerf)) dao.createDB(CacheMLStock.dbNameModelShortPerf);
        if (!repo.contains(StockDocument.dbName)) dao.createDB(StockDocument.dbName);
        if (!repo.contains(HistogramDocument.dbName)) dao.createDB(HistogramDocument.dbName);



    }

    private static class InfluxDaoConnectorHolder {
        /** Instance unique non pre-initialise */
        private final static InfluxDaoConnector instance = new InfluxDaoConnector();
    }

    public static InfluxDaoConnector getInstance() {
        return InfluxDaoConnectorHolder.instance;
    }

    public static void writePoints(final BatchPoints batchPoints) throws InterruptedException {
        writePoints(batchPoints, 0);
    }


    public static void writePoints(final BatchPoints batchPoints, int loop) {
        try {
            getInstance().dao.getDB().write(batchPoints);
        } catch (Exception e) {
            if (loop < ERROR_RATE) {
                loop++;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                writePoints(batchPoints, loop);
            }else {
                log.error("InfluxDaoConnector writePoints error:" + e);
            }
        }
    }

    public static QueryResult getPoints(final String queryString, String dbName) {
        return getPoints(queryString, dbName, 0);
    }


    public static QueryResult getPoints(final String queryString, String dbName, int loop) {
        QueryResult result = null;
        try {
            Query query = new Query(queryString, dbName);
            result = getInstance().dao.getDB().query(query);
        } catch (Exception e) {
            if (loop < ERROR_RATE) {
                loop++;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                return getPoints(queryString,dbName,loop);
            } else {
                log.error("InfluxDaoConnector getPoints error:" + e);
            }
        }
        return result;

    }

    public QueryResult getAkkaPoints(QueryRequest queryRequest) {
        return getPoints(queryRequest.getQuery(), queryRequest.getName());

    }

    public static void deleteDB(String name) {
        List<String> repo = getInstance().dao.getDB().describeDatabases();
        if (repo.contains(name)) {
            getInstance().dao.deleteDB(name);
            getInstance().dao.createDB(name);
        }
    }

    public static BatchPoints getBatchPointsV1(String dbName) {
        return BatchPoints
            .database(dbName)
            .retentionPolicy("autogen")
            .build();
    }


    public static BatchPoints getBatchPoints(String dbName) {
        return getBatchPointsV1(dbName);
    }

    public void close() {

    }


}

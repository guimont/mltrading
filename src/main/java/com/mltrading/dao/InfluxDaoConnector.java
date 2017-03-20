package com.mltrading.dao;

import com.mltrading.dao.impl.InfluxDaoImpl;

import com.mltrading.influxdb.dto.QueryRequest;

import com.mltrading.ml.MatrixValidator;
import com.mltrading.models.stock.StockHistory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.List;

/**
 * Created by gmo on 29/06/2015.
 */
public class InfluxDaoConnector {

    private InfluxDao dao;


    public InfluxDaoConnector() {
        dao = new InfluxDaoImpl();
        dao.createConnection();

        List<String> repo = dao.getDB().describeDatabases();

        if (!repo.contains(StockHistory.dbName)) dao.createDB(StockHistory.dbName);
        if (!repo.contains(MatrixValidator.dbNamePerf)) dao.createDB(MatrixValidator.dbNamePerf);
        if (!repo.contains(MatrixValidator.dbNameModel)) dao.createDB(MatrixValidator.dbNameModel);

    }

    private static class InfluxDaoConnectorHolder {
        /** Instance unique non pre-initialise */
        private final static InfluxDaoConnector instance = new InfluxDaoConnector();
    }

    public static InfluxDaoConnector getInstance() {
        return InfluxDaoConnectorHolder.instance;
    }

    public static void writePoints(final BatchPoints batchPoints) {
        getInstance().dao.getDB().write(batchPoints);
    }

    public static QueryResult getPoints(final String queryString, String dbName) {
        Query query = new Query(queryString, dbName);
        QueryResult result = getInstance().dao.getDB().query(query);
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
        return BatchPoints
                .database(dbName)
                .retentionPolicy("default")
                .build();
    }

    public void close() {

    }


}

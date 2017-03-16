package com.mltrading.dao;

import com.mltrading.dao.impl.InfluxDaoImpl;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;


import java.util.List;

/**
 * Created by gmo on 29/06/2015.
 */
public class InfluxDaoConnectorNotation {

    private InfluxDao dao;
    static String dbName = "notation";

    private InfluxDaoConnectorNotation() {
        dao = new InfluxDaoImpl();
        dao.createConnection();
       // dao.createDB(dbName);

        List<String> repo = dao.getDB().describeDatabases();
        if (!repo.contains(dbName))
            dao.createDB(dbName);

    }

    private static class InfluxDaoConnectorHolder {
        /** Instance unique non pre-initialise */
        private final static InfluxDaoConnectorNotation instance = new InfluxDaoConnectorNotation();
    }

    public static InfluxDaoConnectorNotation getInstance() {
        return InfluxDaoConnectorHolder.instance;
    }

    public static void writePoints(final BatchPoints batchPoints) {
        getInstance().dao.getDB().write(batchPoints);
    }

    public static QueryResult getPoints(final String queryString) {
        Query query = new Query(queryString, dbName);
        QueryResult result = getInstance().dao.getDB().query(query);
        return result;

    }


    public static BatchPoints getBatchPoints() {
        return BatchPoints
                .database(dbName)
                .retentionPolicy("default")
                .build();
    }


}

package com.mltrading.dao.dispatcherPool;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryRequest;
import org.influxdb.dto.QueryResult;

public class WorkerInfluxPool {

    private InfluxDaoConnector connector;

    public WorkerInfluxPool() {
        this.connector = new InfluxDaoConnector();
    }

    public QueryResult process(QueryRequest queryRequest) {

        return connector.getAkkaPoints(queryRequest);
    }
}

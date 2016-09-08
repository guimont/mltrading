package com.mltrading.influxdb.dto;

/**
 * Created by gmo on 08/09/2016.
 */
public class QueryRequest {

    private final  String name;
    private final  String query;

    public QueryRequest(String query, String name) {
        this.name = name;
        this.query = query;
    }

    public String getName() {
        return name;
    }

    public String getQuery() {
        return query;
    }
}

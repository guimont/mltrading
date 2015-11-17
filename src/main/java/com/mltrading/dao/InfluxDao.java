package com.mltrading.dao;

import com.mltrading.influxdb.InfluxDB;

/**
 * Created by gmo on 29/06/2015.
 */
public interface InfluxDao {

    String host="localhost";
    Integer port=8086;

    String login="root";
    String pwd="root";


    public void createConnection();
    public void createDB(String name);
    public void deleteDB(String name);

    public InfluxDB getDB();

}

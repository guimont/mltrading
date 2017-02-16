package com.mltrading.dao;

import com.mltrading.influxdb.InfluxDB;

/**
 * Created by gmo on 29/06/2015.
 */
public interface InfluxDao {



    String login="root";
    String pwd="root";


    public void createConnection();
    public void createConnection(String hostDist);
    public void createDB(String name);
    public void deleteDB(String name);

    public InfluxDB getDB();

}

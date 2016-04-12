package com.mltrading.dao.impl;

import com.mltrading.dao.InfluxDao;
import com.mltrading.influxdb.InfluxDB;
import com.mltrading.influxdb.InfluxDBFactory;

/**
 * Created by gmo on 29/06/2015.
 */
public class InfluxDaoImpl implements InfluxDao{

    private InfluxDB influxDB;

    @Override
    public void createConnection() {
        this.influxDB = InfluxDBFactory.connect("http://"+host+":"+port, login, pwd);
    }


    public void createConnection(String hostDist) {
        this.influxDB = InfluxDBFactory.connect("http://"+hostDist+":"+port, login, pwd);
    }

    @Override
    public void createDB(String name) {
        influxDB.createDatabase(name);
    }

    @Override
    public void deleteDB(String name) {
        influxDB.deleteDatabase(name);
    }

    @Override
    public InfluxDB getDB() {
        return influxDB;
    }
}

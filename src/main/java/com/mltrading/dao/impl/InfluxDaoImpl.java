package com.mltrading.dao.impl;

import com.mltrading.config.MLProperties;
import com.mltrading.dao.InfluxDao;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;


/**
 * Created by gmo on 29/06/2015.
 */
public class InfluxDaoImpl implements InfluxDao{

    private InfluxDB influxDB;

    @Override
    public void createConnection() {
        this.influxDB = InfluxDBFactory.connect("http://" + MLProperties.getProperty("influxdb") + ":" + new Integer(MLProperties.getProperty("influxdbPort")), login, pwd);
    }


    public void createConnection(String hostDist) {
        this.influxDB = InfluxDBFactory.connect("http://"+hostDist+":"+new Integer(MLProperties.getProperty("influxdbPort")), login, pwd);
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

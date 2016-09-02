package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.QueryResult;
import com.mltrading.models.parser.HistoryParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 10/02/2016.
 */
public class MLPerformances  implements Serializable, Comparable<MLPerformances> {

    private String date;
    private MLPerformance mlD1;
    private MLPerformance mlD5;
    private MLPerformance mlD20;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MLPerformances() {
        mlD1 = new MLPerformance();
        mlD5 = new MLPerformance();
        mlD20 = new MLPerformance();
    }

    public MLPerformances(String date) {
        this.date = date;
    }

    public MLPerformance getMlD1() {
        return mlD1;
    }

    public void setMlD1(MLPerformance mlD1) {
        this.mlD1 = mlD1;
    }

    public MLPerformance getMlD5() {
        return mlD5;
    }

    public void setMlD5(MLPerformance mlD5) {
        this.mlD5 = mlD5;
    }

    public MLPerformance getMlD20() {
        return mlD20;
    }

    public void setMlD20(MLPerformance mlD20) {
        this.mlD20 = mlD20;
    }


    @Override
    public int compareTo(MLPerformances o) {
        return o.getDate().compareTo(this.date)*-1;
    }


    void save(String code) {
        BatchPoints bp = InfluxDaoConnector.getBatchPoints(MatrixValidator.dbNameModel);
        mlD1.savePerformance(bp,code+"PD1");

        if (mlD5 != null) {
            mlD5.savePerformance(bp,code+"PD5");
        } /*else
            MLPerformance.generateEmptyMLPerformance(date).savePerformance(bp,code+"PD5");*/

        if (mlD20 != null)
            mlD20.savePerformance(bp,code+"PD20");
        /*else
            MLPerformance.generateEmptyMLPerformance(date).savePerformance(bp, code + "PD20");*/

    }


}

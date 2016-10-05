package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gmo on 10/02/2016.
 */
public class MLPerformances  implements Serializable, Comparable<MLPerformances> {

    private String date;

    private Map<PredictionPeriodicity,MLPerformance> container;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPerf(PredictionPeriodicity period, MLPerformance perf) {
        container.put(period,perf);
    }

    public MLPerformances() {
        container = new HashMap<>();
        container.put(PredictionPeriodicity.D1,new MLPerformance());
        container.put(PredictionPeriodicity.D5,new MLPerformance());
        container.put(PredictionPeriodicity.D20,new MLPerformance());
        container.put(PredictionPeriodicity.D40,new MLPerformance());
    }

    public MLPerformances(String date) {
        container = new HashMap<>();
        this.date = date;
    }


    @Override
    public int compareTo(MLPerformances o) {
        return o.getDate().compareTo(this.date)*-1;
    }


    void save(String code) {
        BatchPoints bp = InfluxDaoConnector.getBatchPoints(MatrixValidator.dbNameModel);
        for (Map.Entry<PredictionPeriodicity, MLPerformance> entry : container.entrySet()) {
            entry.getValue().savePerformance(bp,code+"P"+entry.getKey());
        }
    }


    public MLPerformances clone() {
        MLPerformances cloneObject = new MLPerformances();
        cloneObject.setDate(this.date);
        for (Map.Entry<PredictionPeriodicity, MLPerformance> entry : container.entrySet()) {
            cloneObject.setPerf(entry.getKey(), entry.getValue().clone());
        }

        return cloneObject;
    }


    public MLPerformance getMl(PredictionPeriodicity periodicity) {
        return container.get(periodicity);
    }

    public void setMl(MLPerformance mlPerf, PredictionPeriodicity periodicity) {
        container.put(periodicity,mlPerf);
    }

}

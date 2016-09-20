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

    public MLPerformance getMlD1() {
        return container.get(PredictionPeriodicity.D1);
    }

    public void setMlD1(MLPerformance mlPerf) {
        container.put(PredictionPeriodicity.D1,mlPerf);
    }

    public MLPerformance getMlD5() {
        return container.get(PredictionPeriodicity.D5);
    }

    public void setMlD5(MLPerformance mlPerf) {

        container.put(PredictionPeriodicity.D5,mlPerf);
    }

    public MLPerformance getMlD40() {
        return  container.get(PredictionPeriodicity.D40);
    }

    public void setMlD40(MLPerformance mlPerf) {
        container.put(PredictionPeriodicity.D40,mlPerf);
    }

    public MLPerformance getMlD20() {
        return  container.get(PredictionPeriodicity.D20);
    }

    public void setMlD20(MLPerformance mlPerf) {
        container.put(PredictionPeriodicity.D20,mlPerf);
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


}

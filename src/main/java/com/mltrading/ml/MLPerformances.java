package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;
import org.influxdb.dto.BatchPoints;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gmo on 10/02/2016.
 */
public class MLPerformances  implements Serializable, Comparable<MLPerformances> {

    private String date;

    private Map<PredictionPeriodicity,MLPerformance> container;

    private MLPerformance mlD1,mlD5,mlD20,mlD40;

    public Map<PredictionPeriodicity, MLPerformance> getContainer() {
        return container;
    }

    public void setContainer(Map<PredictionPeriodicity, MLPerformance> container) {
        this.container = container;
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

    public MLPerformance getMlD40() {
        return mlD40;
    }

    public void setMlD40(MLPerformance mlD40) {
        this.mlD40 = mlD40;
    }

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


    /**
     * convert for web ui
     */
    public void convertUI() {

        mlD1 = container.get(PredictionPeriodicity.D1);
        mlD5 = container.get(PredictionPeriodicity.D5);
        mlD20 = container.get(PredictionPeriodicity.D20);
        mlD40 = container.get(PredictionPeriodicity.D40);
    }
}

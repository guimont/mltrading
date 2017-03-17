package com.mltrading.models.stock;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.ml.*;
import org.influxdb.dto.QueryResult;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gmo on 17/03/2017.
 */
public class StockPerformance implements Serializable{
    String name;
    String modelType;

    String lastUpdate;
    int iteration;

    private Map<PredictionPeriodicity,PerfModel> container;

    public StockPerformance() {
        container = new HashMap<>();
        PeriodicityList.periodicity.forEach(p ->
            container.put(p, new PerfModel(p))
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }



    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }



    private class PerfModel {
        PredictionPeriodicity p;
        double error;
        double yield;
        int vectorSize;

        public PerfModel(PredictionPeriodicity p) {
            this.p = p;
        }

        public double getError() {
            return error;
        }

        public void setError(double error) {
            this.error = error;
        }

        public double getYield() {
            return yield;
        }

        public void setYield(double yield) {
            this.yield = yield;
        }

        public int getVectorSize() {
            return vectorSize;
        }

        public void setVectorSize(int vectorSize) {
            this.vectorSize = vectorSize;
        }
    }


    public void load(String code) {

        try {
            MLStocks s = CacheMLStock.getMLStockCache().get(code);
            PeriodicityList.periodicity.forEach(p-> {
                String query = "SELECT count(vectorSize) FROM " + code + "V" + p.toString();
                QueryResult result = InfluxDaoConnector.getPoints(query, MatrixValidator.dbNamePerf);
                MLStatus l = s.getStatus();
                PerfModel pm = container.get(p);
                MatrixValidator mv = s.getValidator(p);
                pm.setVectorSize(mv.getVectorSize());
                pm.setError(l.getErrorRate(p));
                pm.setYield(l.getAvg(p));
            });


        } catch (Exception e) {

        }
    }
}

package com.mltrading.models.stock;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.ml.*;
import com.mltrading.ml.model.ModelType;
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
        PeriodicityList.periodicityLong.forEach(p ->
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


    public Map<PredictionPeriodicity, PerfModel> getContainer() {
        return container;
    }

    public void setContainer(Map<PredictionPeriodicity, PerfModel> container) {
        this.container = container;
    }

    private class PerfModel implements Serializable{
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

        public PredictionPeriodicity getP() {
            return p;
        }

        public void setP(PredictionPeriodicity p) {
            this.p = p;
        }
    }


    /**
     * not use akka
     * @param code
     * @return
     */
    public StockPerformance load(String code) {

        try {
            MLStocks s = CacheMLStock.getMLStockCache().get(code);

            if (s != null) {

                this.setName(code);
                this.setModelType("RandomForest");

                String query = "SELECT (vectorSize) FROM " + code + "V" + PredictionPeriodicity.D20 + " ORDER BY DESC limit 1";
                QueryResult result = InfluxDaoConnector.getPoints(query, CacheMLStock.dbNameModelPerf);
                this.setLastUpdate((String) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(0));

                /* considering iteration is same for all period*/
                query = "SELECT count(vectorSize) FROM " + code + "V" + PredictionPeriodicity.D20;
                result = InfluxDaoConnector.getPoints(query, CacheMLStock.dbNameModelPerf);
                this.setIteration (new Double((Double)result.getResults().get(0).getSeries().get(0).getValues().get(0).get(1)).intValue());
                PeriodicityList.periodicityLong.forEach(p -> {
                    MLStatus l = s.getStatus(ModelType.RANDOMFOREST);
                    PerfModel pm = container.get(p);
                    MatrixValidator mv = s.getModel(p).getValidator(ModelType.RANDOMFOREST);
                    pm.setVectorSize(mv.getVectorSize());
                    pm.setError(l.getErrorRate(p));
                    pm.setYield(l.getAvg(p));
                });
                return this;
            }

            return null;


        } catch (Exception e) {

            return null;
        }

    }
}

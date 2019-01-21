package com.mltrading.ml;


import com.mltrading.ml.model.ModelType;
import com.mltrading.ml.ranking.FeaturesRank;
import com.mltrading.ml.util.MLMath;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockPrediction;
import org.apache.spark.mllib.linalg.Vectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;


/**
 * Created by gmo on 29/02/2016.
 */
public class MLPredictor  implements Serializable {


    private static final Logger log = LoggerFactory.getLogger(MLPredictor.class);


    /**
     * Predict value for model long
     * @param codif stock id
     * @param value current value
     * @return prediction
     */
    public StockPrediction prediction(String codif, double value) {

        MLStocks s = CacheMLStock.getMLStockCache().get(codif);
        if (s != null)
            return commonPrediction(codif, value, s);
        else
            return null;

    }

    /**
     * Predict value for model short
     * @param codif stock id
     * @param value current value
     * @return prediction
     */
    public StockPrediction predictionShort(String codif, double value) {

        MLStocks s = CacheMLStock.getMLStockShortCache().get(codif);
        if (s != null)
            return commonPrediction(codif, value, s);
        else
            return null;
    }


    /**
     * Predict value for model rank
     * @param codif
     * @return prediction
     */
    public StockPrediction predictionRank(String codif) {
        MLStocks s = CacheMLStock.getMLStockCache().get(codif);
        if (s != null) {
            try {
                StockPrediction sp = new StockPrediction(codif);
                String date = StockHistory.getLastDateHistory(codif);

                if (CacheMLStock.getMlRankCache().getModel() != null) {
                    FeaturesRank fr = FeaturesRank.createRT(codif, date, sp);
                    sp.setYieldD20(CacheMLStock.getMlRankCache().getModel().predict(Vectors.dense(fr.vectorize())));
                    sp.setConfidence(100 - (s.getStatus(ModelType.RANDOMFOREST).getErrorRate(PredictionPeriodicity.D20) * 100
                        / s.getStatus(ModelType.RANDOMFOREST).getCount(PredictionPeriodicity.D20)), PredictionPeriodicity.D20);
                }

                return sp;
            }catch (Exception e) {
                System.out.print(e.toString());
                return null;
            }
        }
        else {
            return null;
        }
    }


    /**
     * calculate prediction for standard model short and long
     * @param codif stock id
     * @param value current value
     * @param s model
     * @return prediction
     */
    private StockPrediction commonPrediction(String codif, double value, MLStocks s) {
        try {
            StockPrediction sp = new StockPrediction(codif);
            String date = StockHistory.getLastDateHistory(codif);
            PeriodicityList.periodicityLong.forEach(p -> {
                sp.setPrediction(s.getModel(p).aggregate( s, date),p);
                sp.setConfidence(MLMath.PERCENT - (s.getStatus(ModelType.ENSEMBLE).getErrorRate(p) * MLMath.PERCENT / s.getStatus(ModelType.ENSEMBLE).getCount(p)), p);

            });
            sp.setYieldD20(MLMath.yield(sp.getPredictionD20(), value));
            List<MLPerformances> perfs = s.getStatus(ModelType.ENSEMBLE).getPerfList();

            /* calculate error sum weight*/
            double count = 0.;
            for (int i = 0; i < 20; i++) {
                if (perfs.get(perfs.size()-40+i).getMl(PredictionPeriodicity.D20).isSign() == true)
                    count += i;
            }
            count = count/MLMath.SUM20; //sum of 1 to 20
            sp.setLogConfidenceD20(count);

            return sp;
        }catch (Exception e) {
            System.out.print(e.toString());
            return null;
        }
    }
}

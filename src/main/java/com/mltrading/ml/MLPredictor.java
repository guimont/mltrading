package com.mltrading.ml;


import com.mltrading.ml.model.ModelType;
import com.mltrading.ml.ranking.FeaturesRank;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockPrediction;
import org.apache.spark.mllib.linalg.Vectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;


/**
 * Created by gmo on 29/02/2016.
 */
public class MLPredictor  implements Serializable {


    private static final Logger log = LoggerFactory.getLogger(MLPredictor.class);


    /**
     * Predict value for stock
     * @param codif
     * @return
     */
    public StockPrediction prediction(String codif, ModelType type) {


        MLStocks s = CacheMLStock.getMLStockCache().get(codif);
        if (s != null) {
            try {
                StockPrediction sp = new StockPrediction(codif);
                String date = StockHistory.getLastDateHistory(codif);

                PeriodicityList.periodicity.forEach(p -> {

                    sp.setPrediction(s.getModel(p).aggregate( s, date),p);
                    sp.setConfidence(100 - (s.getStatus(type).getErrorRate(p) * 100 / s.getStatus(type).getCount(p)), p);
                });

                if (CacheMLStock.getMlRankCache().getModel() != null) {
                    FeaturesRank fr = FeaturesRank.createRT(codif, date, sp);
                    sp.setYieldD20(CacheMLStock.getMlRankCache().getModel().predict(Vectors.dense(fr.vectorize())));
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




}

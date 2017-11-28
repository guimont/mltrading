package com.mltrading.ml;


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


    private static final Logger log = LoggerFactory.getLogger(MlForecast.class);


    /**
     * Predict value for stock
     * @param codif
     * @return
     */
    public StockPrediction prediction(String codif) {


        MLStocks s = CacheMLStock.getMLStockCache().get(codif);
        if (s != null) {
            try {
                StockPrediction sp = new StockPrediction(codif);
                String date = StockHistory.getLastDateHistory(codif);

                PeriodicityList.periodicity.forEach(p -> {
                    FeaturesStock fs = FeaturesStock.createRT(codif, s.getValidator(p), date);
                    if (fs.currentVectorPos != s.getValidator(p).getVectorSize()) {
                        log.error("model broken!!!!!: " + fs.currentVectorPos +" not equal " +s.getValidator(p).getVectorSize());
                        System.exit(100);
                    }
                    sp.setPrediction(s.getModel(p).predict(Vectors.dense(fs.vectorize())), p);
                    sp.setConfidence(100 - (s.getStatus().getErrorRate(p) * 100 / s.getStatus().getCount(p)), p);

                    FeaturesRank fr = FeaturesRank.createRT(codif, date);
                    sp.setYieldD20(CacheMLStock.getMlRankCache().getModel().predict(Vectors.dense(fr.vectorize())));
                });


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

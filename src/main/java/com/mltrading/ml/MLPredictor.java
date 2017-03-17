package com.mltrading.ml;


import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockPrediction;
import org.apache.spark.mllib.linalg.Vectors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gmo on 29/02/2016.
 */
public class MLPredictor  implements Serializable {

    private static final List<PredictionPeriodicity> periodicity = Arrays.asList(PredictionPeriodicity.D1, PredictionPeriodicity.D5, PredictionPeriodicity.D20, PredictionPeriodicity.D40);



    public StockPrediction prediction(String codif) {


        MLStocks s = CacheMLStock.getMLStockCache().get(codif);
        if (s != null) {
            try {
                StockPrediction sp = new StockPrediction(codif);
                String date = StockHistory.getLastDateHistory(codif);

                periodicity.forEach(p -> {
                    FeaturesStock fs = FeaturesStock.createRT(codif, s.getValidator(p), date);
                    sp.setPrediction(s.getModel(p).predict(Vectors.dense(fs.vectorize())), p);
                    sp.setConfidence(100 - (s.getStatus().getErrorRate(p) * 100 / s.getStatus().getCount(p)),p);
                });

                /*
                FeaturesStock fs = FeaturesStock.createRT(codif, s.getValidator(PredictionPeriodicity.D1), date);
                sp.setPredictionD1(s.getModel(PredictionPeriodicity.D1).predict(Vectors.dense(fs.vectorize())));
                sp.setConfidenceD1(100 - (s.getStatus().getErrorRateD1() * 100 / s.getStatus().getCountD1()));

                fs = FeaturesStock.createRT(codif, s.getValidator(PredictionPeriodicity.D5), date);
                sp.setPredictionD5(s.getModel(PredictionPeriodicity.D5).predict(Vectors.dense(fs.vectorize())));
                sp.setConfidenceD5(100 - (s.getStatus().getErrorRateD5() * 100 / s.getStatus().getCountD5()));

                fs = FeaturesStock.createRT(codif, s.getValidator(PredictionPeriodicity.D20), date);
                sp.setPredictionD20(s.getModel(PredictionPeriodicity.D20).predict(Vectors.dense(fs.vectorize())));
                sp.setConfidenceD20(100 - (s.getStatus().getErrorRateD20() * 100 / s.getStatus().getCountD20()));

                fs = FeaturesStock.createRT(codif, s.getValidator(PredictionPeriodicity.D40), date);
                sp.setPredictionD40(s.getModel(PredictionPeriodicity.D40).predict(Vectors.dense(fs.vectorize())));
                sp.setConfidenceD40(100 - (s.getStatus().getErrorRate(PredictionPeriodicity.D40) * 100 / s.getStatus().getCountD40()));*/

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

package com.mltrading.ml;

import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockPrediction;
import org.apache.spark.mllib.linalg.Vectors;

import java.io.Serializable;

/**
 * Created by gmo on 29/02/2016.
 */
public class MLPredictor  implements Serializable {

    public StockPrediction prediction(StockGeneral stock) {


        MLStocks s = CacheMLStock.getMLStockCache().get(stock.getCodif());
        if (s != null) {
            try {
                StockPrediction sp = new StockPrediction(stock.getCodif());
                String date = StockHistory.getLastDateHistory(stock.getCodif());
                FeaturesStock fs = FeaturesStock.createRT(stock, s.getMlD1().getValidator(), date);
                sp.setPredictionD1(s.getMlD1().getModel().predict(Vectors.dense(fs.vectorize())));
                sp.setConfidenceD1(100 - (s.getStatus().getErrorRateD1() * 100 / 90));

                fs = FeaturesStock.createRT(stock, s.getMlD5().getValidator(), date);
                sp.setPredictionD5(s.getMlD5().getModel().predict(Vectors.dense(fs.vectorize())));
                sp.setConfidenceD5(100 - (s.getStatus().getErrorRateD5() * 100 / 85));

                fs = FeaturesStock.createRT(stock, s.getMlD20().getValidator(), date);
                sp.setPredictionD20(s.getMlD20().getModel().predict(Vectors.dense(fs.vectorize())));
                sp.setConfidenceD20(100 - (s.getStatus().getErrorRateD20() * 100 / 70));

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

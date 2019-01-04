package com.mltrading.assetmanagement;

import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MLStocks;
import com.mltrading.ml.PeriodicityList;
import com.mltrading.ml.model.ModelType;
import com.mltrading.ml.ranking.FeaturesRank;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockPrediction;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import org.apache.spark.mllib.linalg.Vectors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * create simulation asset Management
 */
public class Simulation {

    /**
     * run simulation to last 100 day to today
     * for each day, predict value
     */
    public void run(List<AssetManagement> assetToSims) {

        //Take ORA as reference
        List<String> rangeDate = StockHistory.getDateHistoryListOffsetLimit("ORA", 100);

        assetToSims.forEach(assetToSim -> rangeDate.forEach(d -> {
            Map<String, StockGeneral> mapSim = new HashMap<>();
            CacheStockGeneral.getCache().values().forEach(sg -> {

                StockGeneral sgSim = new StockGeneral(StockHistory.getStockHistory(sg.getCodif(), d), sg);
                StockPrediction prediction = prediction(sgSim.getCodif(), d);
                sgSim.setPrediction(prediction);

                mapSim.put(sgSim.getCode(), sgSim);


            });
            assetToSim.evaluate(mapSim);
            assetToSim.decision(mapSim);
        }));

    }


    /**
     * Predict value for stock
     * @param codif
     * @return
     */
    private StockPrediction prediction(String codif, String date) {


        MLStocks s = CacheMLStock.getMLStockCache().get(codif);
        if (s != null) {
            try {
                StockPrediction sp = new StockPrediction(codif);

                PeriodicityList.periodicityLong.forEach(p -> {

                    sp.setPrediction(s.getModel(p).aggregate( s, date),p);
                    sp.setConfidence(100 - (s.getStatus(ModelType.ENSEMBLE).getErrorRate(p) * 100 / s.getStatus(ModelType.ENSEMBLE).getCount(p)), p);
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


    public void cleanAsset(AssetManagement assetToSim) {
        assetToSim.curentAssetStock.values().forEach( assetStock -> {
            StockGeneral sg  = CacheStockGeneral.getCache().get(CacheStockGeneral.getCode(assetStock.getCode()));
            assetStock.buyIt(sg);
            assetToSim.setMargin(assetStock);
        });
    }
}

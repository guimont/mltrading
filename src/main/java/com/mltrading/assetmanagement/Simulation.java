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

import static com.mltrading.ml.MlForecast.updatePredictor;


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
                updatePredictor(sgSim,true);

                mapSim.put(sgSim.getCode(), sgSim);

            });
            assetToSim.evaluate(mapSim);
            assetToSim.decision(mapSim);
        }));

    }




    public void cleanAsset(AssetManagement assetToSim) {
        assetToSim.curentAssetStock.values().forEach( assetStock -> {
            StockGeneral sg  = CacheStockGeneral.getCache().get(CacheStockGeneral.getCode(assetStock.getCode()));
            assetStock.sellIt(sg.getValue());
            assetToSim.setMargin(assetStock);
            assetToSim.assetStockList.add(assetStock);
        });
    }
}

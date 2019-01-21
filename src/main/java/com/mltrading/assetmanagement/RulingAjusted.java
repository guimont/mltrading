package com.mltrading.assetmanagement;

import akka.japi.Pair;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockPrediction;
import com.mltrading.models.stock.cache.CacheStockGeneral;

import java.util.*;

public class RulingAjusted implements Ruling {


    public double process( Map<String,AssetStock> assetStockMap, AssetProperties properties, double invest) {
        return process(CacheStockGeneral.getCache(), assetStockMap, properties, invest);
    }


    private int getEquityTransaction(Collection<AssetStock> assets) {
        final int[] equity = {0};
        assets.stream().map( a-> a.isIncrease()? equity[0]++: equity[0]--);

        return equity[0];
    }


    public double evaluateAsset(StockGeneral sg) {
        return sg.getPerformanceEstimate()*sg.getPrediction().getLogConfidenceD20()* sg.getPredictionShort().getConfidenceD20();
    }


    public double process(Map<String,StockGeneral> stockMap, Map<String,AssetStock> assetStockMap, AssetProperties properties, double invest) {


        List<StockGeneral> l = new ArrayList<>(stockMap.values());

        List<Pair<Double, String>> predState = new ArrayList();

        l.forEach(s -> {
            if (s.getPrediction() != null)
                predState.add(new Pair<>(Math.abs(evaluateAsset(s)), s.getCodif()));
        });

        predState.sort(Collections.reverseOrder(Comparator.comparingDouble(Pair::first)));


        int equityTransaction = getEquityTransaction(assetStockMap.values());


        Iterator iterator = predState.iterator();
        Pair<Double, String> codeSelected = (Pair<Double, String>) iterator.next();
        while (codeSelected != null && invest > properties.getPart()) {

            if (assetStockMap.containsKey(codeSelected.second())) {
                if (iterator.hasNext())
                    codeSelected = (Pair<Double, String>) iterator.next();
                else
                    codeSelected = null;
                continue;
            }

            StockGeneral sg = stockMap.get(CacheStockGeneral.getCode(codeSelected.second()));


            /* if always 2 stock with incresae expected, need decrease invest to equilibrate asset*/
            if (equityTransaction >= 2 && !sg.getPrediction().isIncrease()) {

                AssetStock assetStock = new AssetStock(sg.getCodif(), sg.getSector(), properties);
                invest -= assetStock.buyIt(sg);
                assetStockMap.put(sg.getCodif(), assetStock);
            }
            else  if (equityTransaction <= 2 && sg.getPrediction().isIncrease()) {
                AssetStock assetStock = new AssetStock(sg.getCodif(), sg.getSector(), properties);
                invest -= assetStock.buyIt(sg);
                assetStockMap.put(sg.getCodif(), assetStock);
            }

            else  {
                AssetStock assetStock = new AssetStock(sg.getCodif(), sg.getSector(), properties);
                invest -= assetStock.buyIt(sg);
                assetStockMap.put(sg.getCodif(), assetStock);
            }


            if (iterator.hasNext())
                codeSelected = (Pair<Double, String>) iterator.next();
            else
                codeSelected = null;

        }

        return invest;

    }

}

package com.mltrading.assetmanagement.ruleimpl;


import akka.japi.Pair;
import com.mltrading.assetmanagement.AssetManagement;
import com.mltrading.assetmanagement.AssetStock;
import com.mltrading.assetmanagement.AssetProperties;
import com.mltrading.assetmanagement.Ruling;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.cache.CacheStockGeneral;

import java.util.*;
import java.util.stream.Collectors;

public class RulingSimple implements Ruling {


    public double process( AssetManagement assetManagement ) {
        double invest = process(CacheStockGeneral.getCache(), assetManagement);
        invest += process(CacheStockGeneral.getCacheEx(), assetManagement);
        return invest;
    }



    public double evaluateAsset(StockGeneral sg) {
        return sg.getPerformanceEstimate()*sg.getPrediction().getLogConfidenceD20()* sg.getPredictionShort().getConfidenceD20();
        //return sg.getPrediction().getYieldD20()* sg.getPrediction().getConfidenceD20();
    }


    public double process(Map<String,StockGeneral> stockMap, AssetManagement assetManagement ) {
        Map<String, AssetStock> assetStockMap = assetManagement.getCurentAssetStock();
        AssetProperties properties = assetManagement.getProperties();
        double invest =  assetManagement.getAssetValue();
        List<String> notUse = assetManagement.getAssetStockList().stream().map(a -> a.getCode()).collect(Collectors.toList());


        List<StockGeneral> l = new ArrayList<>(stockMap.values());

        List<Pair<Double, String>> predState = new ArrayList();

        l.forEach(s -> {
            if (s.getPrediction() != null)
                predState.add(new Pair<>((Math.abs(evaluateAsset(s))), s.getCodif()));
        });

        predState.sort(Collections.reverseOrder(Comparator.comparingDouble(Pair::first)));




        Iterator iterator = predState.iterator();
        Pair<Double, String> codeSelected = (Pair<Double, String>) iterator.next();
        while (codeSelected != null && invest > properties.getPart()) {

            if (assetStockMap.containsKey(codeSelected.second()) || notUse.contains(codeSelected.second())) {
                if (iterator.hasNext())
                    codeSelected = (Pair<Double, String>) iterator.next();
                else
                    codeSelected = null;
                continue;
            }

            StockGeneral sg = stockMap.get(CacheStockGeneral.getCode(codeSelected.second()));

            AssetStock assetStock = new AssetStock(sg.getCodif(),sg.getSector(),properties);
            invest -= assetStock.buyIt(sg);
            assetStockMap.put(sg.getCodif(), assetStock);

            if (iterator.hasNext())
                codeSelected = (Pair<Double, String>) iterator.next();
            else
                codeSelected = null;

        }

        return invest;

    }

}

package com.mltrading.assetmanagement;

import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.cache.CacheStockGeneral;

import java.util.Map;
import java.util.stream.Collectors;

public class EvaluateSimple implements Evaluate{


    @Override
    public void evaluate(Map<String,StockGeneral> stockMap,  AssetManagement assetManagement) {

        Map<String,AssetStock> copy = assetManagement.curentAssetStock.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue));

        copy.values().forEach( a-> {

            String code = a.getCode();
            StockGeneral sg =stockMap.get(CacheStockGeneral.getCode(code));

            if (a.makeAction(sg.getValue())) {
                a.sellIt(sg.getValue(), sg.getDay());
                assetManagement.assetStockList.add(a);
                assetManagement.curentAssetStock.remove(a.getCode());
                assetManagement.setMargin(a);
            }

        });

    }
}

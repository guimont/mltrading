package com.mltrading.assetmanagement.evaluateimpl;

import com.mltrading.assetmanagement.AssetManagement;
import com.mltrading.assetmanagement.AssetStock;
import com.mltrading.assetmanagement.Evaluate;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.cache.CacheStockGeneral;

import java.util.Map;
import java.util.stream.Collectors;

public class EvaluateAdjusted implements Evaluate {


    @Override
    public void evaluate(Map<String, StockGeneral> stockMap, AssetManagement assetManagement) {

        Map<String, AssetStock> copy = assetManagement.getCurentAssetStock().entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue));

        copy.values().forEach( a-> {

            String code = a.getCode();
            StockGeneral sg =stockMap.get(CacheStockGeneral.getCode(code));

            a.changeValue(sg);

            if (a.makeAction(sg.getValue())) {
                a.sellIt(sg.getValue(), sg.getDay());
                assetManagement.getAssetStockList().add(a);
                assetManagement.getCurentAssetStock().remove(a.getCode());
                assetManagement.setMargin(a);
            }

        });

    }
}

package com.mltrading.assetmanagement;

import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockPrediction;

import java.util.Map;

public interface Ruling{

    double evaluateAsset(StockGeneral sg);
    double process(Map<String, StockGeneral> stockMap, AssetManagement assetManagement);
    double process(AssetManagement assetManagement);

}

package com.mltrading.assetmanagement;

import com.mltrading.models.stock.StockGeneral;

import java.util.Map;

public interface Ruling {


    double process(Map<String, StockGeneral> stockMap,  Map<String,AssetStock> assetStockMap, AssetProperties properties, double invest);

}

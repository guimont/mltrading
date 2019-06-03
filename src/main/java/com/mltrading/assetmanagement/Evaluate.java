package com.mltrading.assetmanagement;

import com.mltrading.models.stock.StockGeneral;

import java.util.Map;

public interface Evaluate {

    void evaluate(Map<String, StockGeneral> stockMap, AssetManagement assetManagement);

}

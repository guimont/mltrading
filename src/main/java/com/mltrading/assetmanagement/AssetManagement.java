package com.mltrading.assetmanagement;

import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.cache.CacheStockGeneral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AssetManagement implements Serializable {

    Map<String,AssetStock> curentAssetStock = new HashMap<String, AssetStock>();
    List<AssetStock> assetStockList = new ArrayList<>();
    private double assetValue;
    private AssetProperties properties;
    private int id;
    private Ruling rule;
    private Evaluate evaluate;


    private static final AtomicInteger sequence = new AtomicInteger();

    public static int next() {
        return sequence.incrementAndGet();
    }


    void decision() {
        assetValue = rule.process(this);
    }

    void decision(Map<String,StockGeneral>  stockGeneralMap) {
        assetValue = rule.process(stockGeneralMap, this);
    }

    public void evaluate(Map<String, StockGeneral> stockGeneralMap) {
        evaluate.evaluate(stockGeneralMap,this);
    }




    public AssetManagement(Ruling rule,Evaluate evaluate, double assetValue) {
        this(rule, evaluate,assetValue, new AssetProperties("bink", 0, true, 9));
    }

    public AssetManagement(Ruling rule,Evaluate evaluate, double assetValue, AssetProperties properties) {
        this.rule = rule;
        this.evaluate = evaluate;
        this.assetValue = assetValue;
        this.properties = properties;
        id = next();

    }

    public void setMargin(AssetStock assetStock) {

        if (properties.isFixedcosts())
            assetValue += assetStock.getPriceBuyIn()*assetStock.getVolume()
                        + assetStock.getMargin()*assetStock.getVolume()
                        - properties.getCommissionFix();
        else
            assetValue += assetStock.getPriceBuyIn()*assetStock.getVolume()*properties.getCommissionPercent()
                        + assetStock.getMargin()*assetStock.getVolume();
    }




    public Map<String, AssetStock> getCurentAssetStock() {
        return curentAssetStock;
    }

    public void setCurentAssetStock(Map<String, AssetStock> curentAssetStock) {
        this.curentAssetStock = curentAssetStock;
    }

    public List<AssetStock> getAssetStockList() {
        return assetStockList;
    }

    public void setAssetStockList(List<AssetStock> assetStockList) {
        this.assetStockList = assetStockList;
    }

    public double getAssetValue() {
        return assetValue;
    }

    public void setAssetValue(double assetValue) {
        this.assetValue = assetValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AssetProperties getProperties() {
        return properties;
    }

    public void setProperties(AssetProperties properties) {
        this.properties = properties;
    }
}

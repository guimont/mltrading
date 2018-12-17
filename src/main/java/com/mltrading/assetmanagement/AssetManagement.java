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


    private static final AtomicInteger sequence = new AtomicInteger();

    public static int next() {
        return sequence.incrementAndGet();
    }


    void decision() {
        RulingSimple rule = new RulingSimple();
        assetValue = rule.process(curentAssetStock, properties, assetValue);
    }

    void decision(Map<String,StockGeneral>  stockGeneralMap) {
        RulingSimple rule = new RulingSimple();
        assetValue = rule.process(stockGeneralMap, curentAssetStock, properties, assetValue);
    }

    public AssetManagement(double assetValue) {
        this.assetValue = assetValue;
        properties = new AssetProperties("bink", 0, true, 9);
        id = next();
    }

    public AssetManagement(double assetValue, AssetProperties properties) {
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



    public void evaluate() {
        evaluate(CacheStockGeneral.getCache());
    }



    public void evaluate(Map<String,StockGeneral> stockMap) {

        Map<String,AssetStock> copy = curentAssetStock.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue));

        copy.values().forEach( a-> {

            String code = a.getCode();
            StockGeneral sg =stockMap.get(CacheStockGeneral.getCode(code));

            if (a.makeAction(sg.getValue())) {
                a.sellIt(sg.getValue());
                assetStockList.add(a);
                curentAssetStock.remove(a.getCode());
                setMargin(a);
            }

        });

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
}

package com.mltrading.models.stock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gmo on 17/06/2015.
 */
public class CacheRawMaterial {


    private static final Map<String, StockRawMat> RawMap;

    static {
        Map<String, StockRawMat> aMap = new HashMap<>();
        aMap.put("DTOE", new StockRawMat("DTOE", "dollar/euro", "http://localhost:8090/raw/dollar.html"));
        RawMap = Collections.unmodifiableMap(aMap);
    }

    private CacheRawMaterial() {
    }

    private static class CacheStockGeneralHolder {
        /** Instance unique non pre-initialise */
        private final static CacheRawMaterial instance = new CacheRawMaterial();
    }


    public static Map<String,StockRawMat> getCache() {
        return CacheStockGeneralHolder.instance.RawMap;
    }

    public static void removeCache() {
        CacheStockGeneralHolder.instance.RawMap.clear();
    }

}

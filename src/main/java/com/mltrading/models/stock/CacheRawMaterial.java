package com.mltrading.models.stock;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gmo on 17/06/2015.
 */
public class CacheRawMaterial {

    private Map<String,StockGeneral> CSCache = new TreeMap<>();

    private CacheRawMaterial() {
    }

    private static class CacheStockGeneralHolder {
        /** Instance unique non pre-initialise */
        private final static CacheRawMaterial instance = new CacheRawMaterial();
    }


    public static Map<String,StockGeneral> getCache() {
        return CacheStockGeneralHolder.instance.CSCache;
    }

    public static void removeCache() {
        CacheStockGeneralHolder.instance.CSCache.clear();
    }

}

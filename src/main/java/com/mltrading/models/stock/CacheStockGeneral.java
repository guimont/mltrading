package com.mltrading.models.stock;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gmo on 17/06/2015.
 */
public class CacheStockGeneral {

    private Map<String,StockGeneral> CSCache = new TreeMap<>();

    private CacheStockGeneral() {
    }

    private static class CacheStockGeneralHolder {
        /** Instance unique non pre-initialise */
        private final static CacheStockGeneral instance = new CacheStockGeneral();
    }


    public static Map<String,StockGeneral> getCache() {
        return CacheStockGeneralHolder.instance.CSCache;
    }

    public static void removeCache() {
        CacheStockGeneralHolder.instance.CSCache.clear();
    }

}

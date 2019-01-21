package com.mltrading.ml;

public class MLStocksShort extends MLStocks{


    public MLStocksShort(String codif) {
        super(codif, CacheMLStock.dbNameModelShort, CacheMLStock.dbNameModelShortPerf, CacheMLStock.SHORT_EXTENDED, CacheMLStock.RANGE_MIN, CacheMLStock.RENDERING_SHORT);
    }
}

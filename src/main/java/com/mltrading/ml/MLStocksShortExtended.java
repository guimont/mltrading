package com.mltrading.ml;

public class MLStocksShortExtended  extends MLStocks{


    public MLStocksShortExtended(String codif) {
        super(codif, CacheMLStock.dbNameModelExShort, CacheMLStock.dbNameModelExShortPerf, CacheMLStock.SHORT_EXTENDED, CacheMLStock.RANGE_MIN, CacheMLStock.RENDERING_SHORT);

    }
}

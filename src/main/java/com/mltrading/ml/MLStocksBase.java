package com.mltrading.ml;

public class MLStocksBase extends MLStocks{


    public MLStocksBase(String codif) {
        super(codif, CacheMLStock.dbNameModel, CacheMLStock.dbNameModelPerf, CacheMLStock.NO_EXTENDED, CacheMLStock.RANGE_MAX, CacheMLStock.RENDERING);

    }
}

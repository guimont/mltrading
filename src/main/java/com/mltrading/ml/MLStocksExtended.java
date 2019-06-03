package com.mltrading.ml;

public class MLStocksExtended extends MLStocks{


    public MLStocksExtended(String codif) {
        super(codif, CacheMLStock.dbNameModelEx, CacheMLStock.dbNameModelPerfEx, CacheMLStock.EXTENDED, CacheMLStock.RANGE_MAX, CacheMLStock.RENDERING);

    }
}

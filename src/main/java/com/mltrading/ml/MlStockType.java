package com.mltrading.ml;


public enum MlStockType {

    BASE,

    SHORT,

    EXTENDED,

    EXTENDED_SHORT;

    public static MlStockType Convert(String modelType) {

        if (modelType.equalsIgnoreCase("BASE")) return BASE;
        if (modelType.equalsIgnoreCase("SHORT")) return SHORT;

        throw  new UnsupportedOperationException("modelType Unknow");
    }
}

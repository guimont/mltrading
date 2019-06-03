package com.mltrading.ml;


public enum MlStockType {

    BASE,

    SHORT,

    EXTENDED,

    EXTENDED_SHORT;

    public static MlStockType Convert(String modelType) {

        if (modelType.equalsIgnoreCase("BASE")) return BASE;
        if (modelType.equalsIgnoreCase("SHORT")) return SHORT;
        if (modelType.equalsIgnoreCase("EXTENDED")) return EXTENDED;
        if (modelType.equalsIgnoreCase("EXTENDED_SHORT")) return EXTENDED_SHORT;


        throw  new UnsupportedOperationException("modelType Unknow");
    }
}

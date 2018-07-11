package com.mltrading.ml.model;

import java.io.Serializable;

public enum ModelType implements Serializable {



    RANDOMFOREST,
    GRADIANTBOOSTTREE,
    ENSEMBLE;

    private static String RANDOMFORESTCODE = "V";
    private static String GRADIANTBOOSTTREECODE = "G";
    private static String ENSEMBLECODE = "E";

    public static ModelType get(String modelType) {
        if (modelType.equalsIgnoreCase("RANDOMFOREST")) return ModelType.RANDOMFOREST;
        if (modelType.equalsIgnoreCase("GRADIANTBOOSTTREE")) return ModelType.GRADIANTBOOSTTREE;
        if (modelType.equalsIgnoreCase("ENSEMBLE")) return ModelType.ENSEMBLE;
        return null;
    }

    public static String code(ModelType type) {
        if (type == RANDOMFOREST) return RANDOMFORESTCODE;
        if (type == GRADIANTBOOSTTREE) return GRADIANTBOOSTTREECODE;
        if (type == ENSEMBLE) return ENSEMBLECODE;


        throw new UnsupportedOperationException();
    }
}

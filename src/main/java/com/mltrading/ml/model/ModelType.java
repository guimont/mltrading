package com.mltrading.ml.model;

import java.io.Serializable;

public enum ModelType implements Serializable {



    RANDOMFOREST,
    GRADIANTBOOSTTREE;

    private static String RANDOMFORESTCODE = "V";
    private static String GRADIANTBOOSTTREECODE = "G";

    public static ModelType get(String modelType) {
        if (modelType.equalsIgnoreCase("RANDOMFOREST")) return ModelType.RANDOMFOREST;
        if (modelType.equalsIgnoreCase("GRADIANTBOOSTTREE")) return ModelType.GRADIANTBOOSTTREE;

        return null;
    }

    public static String code(ModelType type) {
        if (type == RANDOMFOREST) return RANDOMFORESTCODE;
        if (type == GRADIANTBOOSTTREE) return GRADIANTBOOSTTREECODE;

        throw new UnsupportedOperationException();
    }
}

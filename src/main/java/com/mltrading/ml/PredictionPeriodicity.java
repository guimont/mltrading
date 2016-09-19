package com.mltrading.ml;

/**
 * Created by gmo on 31/08/2016.
 */
public enum PredictionPeriodicity {
    /** 1 day */
    D1,
    /** 5 days    mid range */
    D5,
    /** 20 days   long range */
    D20,
    /** 20 days   long range */
    D40;

    public static int convert(PredictionPeriodicity period) {
        if (period == PredictionPeriodicity.D1) return 1;
        if (period == PredictionPeriodicity.D5) return 5;
        if (period == PredictionPeriodicity.D20) return 20;
        if (period == PredictionPeriodicity.D40) return 40;

        return 0;
    }
}

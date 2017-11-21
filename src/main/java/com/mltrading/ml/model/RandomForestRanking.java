package com.mltrading.ml.model;

import com.mltrading.ml.MLStocks;
import com.mltrading.ml.MLRank;
import com.mltrading.ml.PredictionPeriodicity;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;

import java.util.HashMap;
import java.util.Map;

public class RandomForestRanking extends MLModelRanking<RandomForestModel>{



    public void rank(MLStocks mls,  PredictionPeriodicity period) {
        mls.getModel(period);
    }

    @Override
    protected RandomForestModel trainModel(JavaRDD<LabeledPoint> trainingData) {
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();

        String impurity = "variance";

        String featureSubsetStrategy = "auto"; // Let the algorithm choose.
        final RandomForestModel model = RandomForest.trainRegressor(trainingData,
            categoricalFeaturesInfo,500, featureSubsetStrategy, impurity,
            5, 32, 32345);

        return model;
    }

    @Override
    protected void setModel(MLRank mlr, RandomForestModel model) {
        mlr.setModel(model);
    }
}

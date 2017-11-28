package com.mltrading.ml.ranking;


import java.util.*;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;

public class RandomForestRanking extends MLModelRanking<RandomForestModel>{


    @Override
    protected RandomForestModel trainModel(JavaRDD<LabeledPoint> trainingData) {
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();

        String impurity = "variance";

        String featureSubsetStrategy = "auto"; // Let the algorithm choose.
        final RandomForestModel model = RandomForest.trainRegressor(trainingData,
            categoricalFeaturesInfo,500, featureSubsetStrategy, impurity,
            5, 32, 100);

        return model;
    }

    @Override
    protected void setModel(MLRank mlr, RandomForestModel model) {
        mlr.setModel(model);
    }



    @Override
    protected double predict(MLRank mlr,  Vector vector) {
        return mlr.getModel().predict(vector);
    }

}

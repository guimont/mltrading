package com.mltrading.ml.model;

import java.util.*;


import com.mltrading.ml.*;
import org.apache.spark.mllib.linalg.Vector;


import org.apache.spark.api.java.JavaRDD;

import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;

/**
 * Created by gmo on 14/11/2015.
 */
public class RandomForestStock extends MlModelGeneric<RandomForestModel> {

    @Override
    protected void setModel(MLStocks mls, PredictionPeriodicity period, RandomForestModel model) {
        mls.setModel(period, model);
    }


    @Override
    protected RandomForestModel trainModel(JavaRDD<LabeledPoint> trainingData, MatrixValidator validator) {
        // Train a RandomForest model.
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();

        String impurity = "variance";

        String featureSubsetStrategy = "auto"; // Let the algorithm choose.
        final RandomForestModel model = RandomForest.trainRegressor(trainingData,
            categoricalFeaturesInfo, validator.getNumTrees(), featureSubsetStrategy, impurity,
            validator.getMaxDepth(), validator.getMaxBins(),
            validator.getSeed());

        return model;
    }


    @Override
    protected double predict(MLStocks mls, PredictionPeriodicity period, Vector vector) {
        return mls.getModel(period).predict(vector);
    }





}

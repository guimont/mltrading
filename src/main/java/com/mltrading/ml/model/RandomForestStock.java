package com.mltrading.ml.model;

import java.util.*;


import com.mltrading.ml.*;
import org.apache.spark.mllib.linalg.Vector;


import org.apache.spark.api.java.JavaRDD;

import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import scala.Serializable;

/**
 * Created by gmo on 14/11/2015.
 */
public class RandomForestStock extends MlModelGeneric<MLRandomForestModel> implements Serializable {

    @Override
    protected void setModel(MLStocks mls, PredictionPeriodicity period, MLRandomForestModel model) {
        mls.setModel(period, model, ModelType.RANDOMFOREST);
    }



    @Override
    protected MLRandomForestModel trainModel(JavaRDD<LabeledPoint> trainingData, MatrixValidator validator) {
        // Train a RandomForest model.
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();

        String impurity = "variance";

        String featureSubsetStrategy = "auto"; // Let the algorithm choose.
        final RandomForestModel model = RandomForest.trainRegressor(trainingData,
            categoricalFeaturesInfo, validator.getNumTrees(), featureSubsetStrategy, impurity,
            validator.getMaxDepth(), validator.getMaxBins(),
            validator.getSeed());

        MLRandomForestModel mlRandomForestModel = new MLRandomForestModel(model);
        mlRandomForestModel.setValidator(validator);

        return mlRandomForestModel;
    }


    @Override
    protected double predict(MLStocks mls, PredictionPeriodicity period, Vector vector) {
        return mls.getModel(period,ModelType.RANDOMFOREST).predict(vector);
    }

    @Override
    protected double predict(Vector vector,  MLRandomForestModel model) {
        return model.predict(vector);
    }

    @Override
    protected MatrixValidator getValidator(MLStocks mls, PredictionPeriodicity period) {
        return mls.getModel(period,ModelType.RANDOMFOREST).getValidator();
    }

    @Override
    protected MLStatus getStatus(MLStocks mls) {

        return mls.getStatus(ModelType.RANDOMFOREST);
    }


}

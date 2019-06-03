package com.mltrading.ml.ranking;


import java.util.*;

import com.mltrading.ml.MLStatus;
import com.mltrading.ml.model.ModelType;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import scala.Serializable;

public class RandomForestRanking extends MLModelRanking<MLRandomForestRanking> implements Serializable {


    @Override
    protected MLRandomForestRanking trainModel(JavaRDD<LabeledPoint> trainingData) {
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();

        String impurity = "variance";

        String featureSubsetStrategy = "auto"; // Let the algorithm choose.
        final RandomForestModel model = RandomForest.trainRegressor(trainingData,
            categoricalFeaturesInfo,500, featureSubsetStrategy, impurity,
            5, 32, 100);

        MLRandomForestRanking mlRandomForestModel = new MLRandomForestRanking(model);

        return mlRandomForestModel;
    }



    @Override
    protected void setModel(MLRank mlr, MLRandomForestRanking model) {
        mlr.setModel(model, ModelType.RANDOMFOREST);
    }

    @Override
    protected double predict(MLRank mlr,  Vector vector) {
        return mlr.getModel(ModelType.RANDOMFOREST).predict(vector);
    }

    @Override
    protected double predict(Vector vector, MLRandomForestRanking model) {
        return model.predict(vector);
    }

    @Override
    protected MLStatus getStatus(MLRank mls) {
        return mls.getStatus(ModelType.RANDOMFOREST);
    }


}

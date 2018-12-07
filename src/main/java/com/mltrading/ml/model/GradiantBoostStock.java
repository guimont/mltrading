package com.mltrading.ml.model;

import com.mltrading.ml.*;

import org.apache.spark.api.java.JavaRDD;

import org.apache.spark.mllib.linalg.Vector;

import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.GradientBoostedTrees;

import org.apache.spark.mllib.tree.configuration.BoostingStrategy;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Serializable;


import java.util.HashMap;

import java.util.Map;

/**
 * Created by gmo on 14/11/2015.
 */
public class GradiantBoostStock extends MlModelGeneric<MLGradiantBoostStockModel> implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(GradiantBoostStock.class);


    @Override
    protected void setModel(MLStocks mls, PredictionPeriodicity period, MLGradiantBoostStockModel model) {
        mls.setModel(period, model, ModelType.GRADIANTBOOSTTREE);
    }

    /**
     * train model with gradiant boost
     * @param trainingData
     * @param validator
     * @return
     */
    @Override
    protected MLGradiantBoostStockModel trainModel(JavaRDD<LabeledPoint> trainingData, MatrixValidator validator) {
        BoostingStrategy boostingStrategy = BoostingStrategy.defaultParams("Regression");
        boostingStrategy.setNumIterations(3); // Note: Use more iterations in practice.
        boostingStrategy.getTreeStrategy().setMaxDepth(5);
// Empty categoricalFeaturesInfo indicates all features are continuous.

        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
        boostingStrategy.treeStrategy().setCategoricalFeaturesInfo(categoricalFeaturesInfo);

        final GradientBoostedTreesModel model =
            GradientBoostedTrees.train(trainingData, boostingStrategy);

        MLGradiantBoostStockModel mlGradiantBoostStockModel = new MLGradiantBoostStockModel(model);
        mlGradiantBoostStockModel.setValidator(validator);

        return mlGradiantBoostStockModel;
    }

    @Override
    //TODO
    protected double predict(MLStocks mls, PredictionPeriodicity period, Vector vector) {
        return mls.getModel(period,ModelType.GRADIANTBOOSTTREE).predict(vector);
    }

    @Override
    protected double predict(Vector vector, MLGradiantBoostStockModel model) {
        return model.predict(vector);
    }



    @Override
    protected MatrixValidator getValidator(MLStocks mls, PredictionPeriodicity period) {
        return mls.getModel(period,ModelType.GRADIANTBOOSTTREE).getValidator();
    }

    @Override
    protected MLStatus getStatus(MLStocks mls) {
        return mls.getStatus(ModelType.GRADIANTBOOSTTREE);
    }

}

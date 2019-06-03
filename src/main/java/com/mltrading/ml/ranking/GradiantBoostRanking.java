package com.mltrading.ml.ranking;


import com.mltrading.ml.MLStatus;
import com.mltrading.ml.model.ModelType;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.GradientBoostedTrees;
import org.apache.spark.mllib.tree.configuration.BoostingStrategy;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;
import scala.Serializable;

import java.util.HashMap;
import java.util.Map;

public class GradiantBoostRanking extends MLModelRanking<MLGradiantBoostRanking> implements Serializable {


    @Override
    protected MLGradiantBoostRanking trainModel(JavaRDD<LabeledPoint> trainingData) {
        BoostingStrategy boostingStrategyRank = BoostingStrategy.defaultParams("Regression");
        boostingStrategyRank.setNumIterations(3); // Note: Use more iterations in practice.
        boostingStrategyRank.getTreeStrategy().setMaxDepth(5);
// Empty categoricalFeaturesInfo indicates all features are continuous.

        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
        boostingStrategyRank.treeStrategy().setCategoricalFeaturesInfo(categoricalFeaturesInfo);

        final GradientBoostedTreesModel model =
                GradientBoostedTrees.train(trainingData, boostingStrategyRank);


        MLGradiantBoostRanking mlRandomForestModel = new MLGradiantBoostRanking(model);

        return mlRandomForestModel;
    }



    @Override
    protected void setModel(MLRank mlr, MLGradiantBoostRanking model) {
        mlr.setModel(model, ModelType.GRADIANTBOOSTTREE);
    }

    @Override
    protected double predict(MLRank mlr, Vector vector) {
        return mlr.getModel(ModelType.GRADIANTBOOSTTREE).predict(vector);
    }

    @Override
    protected double predict(Vector vector, MLGradiantBoostRanking model) {
        return model.predict(vector);
    }

    @Override
    protected MLStatus getStatus(MLRank mls) {
        return mls.getStatus(ModelType.GRADIANTBOOSTTREE);
    }

}

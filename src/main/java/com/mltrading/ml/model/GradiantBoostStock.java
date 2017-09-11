package com.mltrading.ml.model;

import com.mltrading.ml.*;
import com.mltrading.models.util.MLActivities;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.GradientBoostedTrees;

import org.apache.spark.mllib.tree.configuration.BoostingStrategy;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gmo on 14/11/2015.
 */
public class GradiantBoostStock extends MlModelGeneric<GradientBoostedTreesModel> {

    private static final Logger log = LoggerFactory.getLogger(GradiantBoostStock.class);


    public JavaRDD<LabeledPoint> createRDD(JavaSparkContext sc,  List<FeaturesStock> fsL, PredictionPeriodicity type) {

        JavaRDD<FeaturesStock> data = sc.parallelize(fsL);

        JavaRDD<LabeledPoint> parsedData = data.map(
            new Function<FeaturesStock, LabeledPoint>() {
                public LabeledPoint call(FeaturesStock fs) {
                    return new LabeledPoint(fs.getResultValue(type), Vectors.dense(fs.vectorize()));
                }
            }

        );



        return parsedData;
    }

    @Override
    protected void setModel(MLStocks mls, PredictionPeriodicity period, GradientBoostedTreesModel model) {
        //mls.setModel(period, model);
    }

    @Override
    protected GradientBoostedTreesModel trainModel(JavaRDD<LabeledPoint> trainingData, MatrixValidator validator) {
        BoostingStrategy boostingStrategy = BoostingStrategy.defaultParams("Regression");
        boostingStrategy.setNumIterations(3); // Note: Use more iterations in practice.
        boostingStrategy.getTreeStrategy().setMaxDepth(5);
// Empty categoricalFeaturesInfo indicates all features are continuous.

        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
        boostingStrategy.treeStrategy().setCategoricalFeaturesInfo(categoricalFeaturesInfo);

        final GradientBoostedTreesModel model =
            GradientBoostedTrees.train(trainingData, boostingStrategy);

        return model;
    }

    @Override
    //TODO
    protected double predict(MLStocks mls, PredictionPeriodicity period, Vector vector) {
        return 0;
    }


    /**
     * TODO
     * @param codif
     * @param mls
     * @return
     */
    public MLStocks processRFResult(String codif, MLStocks mls) {

        return mls;

    }
}

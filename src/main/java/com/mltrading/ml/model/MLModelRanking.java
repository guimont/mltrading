package com.mltrading.ml.model;

import com.mltrading.ml.*;
import com.mltrading.models.util.MLActivities;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.model.TreeEnsembleModel;
import scala.Serializable;

import java.util.List;

public abstract class MLModelRanking<R extends TreeEnsembleModel> implements Serializable {

    public JavaRDD<LabeledPoint> createRDD(JavaSparkContext sc,  List<FeatureRank> fsL, PredictionPeriodicity periodicity) {

        JavaRDD<FeatureRank> data = sc.parallelize(fsL);

        JavaRDD<LabeledPoint> parsedData = data.map(
            (Function<FeatureRank, LabeledPoint>) fs -> new LabeledPoint(fs.getResultValue(periodicity), Vectors.dense(fs.vectorize()))
        );

        return parsedData;
    }


    public MLRank processRanking(String code ,String codif, MLRank mlr) {

        CacheMLActivities.addActivities(new MLActivities("FeaturesStock", codif, "start", 0, 0, false));
        List<FeatureRank> fsL = FeatureRank.create(code,codif, MLStockRanking.RANGE_MAX);
        CacheMLActivities.addActivities(new MLActivities("FeaturesStock", codif, "start", 0, 0, true));

        if (null == fsL) return null;

        int born = fsL.size() - MLStockRanking.RENDERING;

        List<FeatureRank> fsLTrain =fsL.subList(0,born);
        List<FeatureRank> fsLTest =fsL.subList(born, fsL.size());

        JavaSparkContext sc = CacheMLStock.getJavaSparkContext();

        // Load and parse the data file.
        JavaRDD<LabeledPoint> trainingData = createRDD(sc, fsLTrain, PredictionPeriodicity.D20);
        JavaRDD<FeatureRank> testData = sc.parallelize(fsLTest);


        // Train a RandomForest model.
        final R model = trainModel(trainingData);

        setModel(mlr,model);

        JavaRDD<FeatureRank> predictionAndLabel = testData.map(
            (Function<FeatureRank, FeatureRank>) fs -> {

                double pred = model.predict(Vectors.dense(fs.vectorize()));
                FeatureRank fsResult = new FeatureRank(fs, pred, PredictionPeriodicity.D20);

                /*already done in constructor*/
                //fsResult.setPredictionValue(pred, PredictionPeriodicity.D20);
                //fsResult.setDate(fs.getDate(PredictionPeriodicity.D20), PredictionPeriodicity.D20);

                return fsResult;
            }
        );


        return mlr;
    }


    protected abstract R trainModel(JavaRDD<LabeledPoint> trainingData);

    protected abstract void setModel(MLRank mlr, R model);

}

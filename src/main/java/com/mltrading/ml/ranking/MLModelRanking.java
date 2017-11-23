package com.mltrading.ml.ranking;

import com.mltrading.ml.*;
import com.mltrading.ml.ranking.FeatureRank;
import com.mltrading.ml.ranking.MLRank;
import com.mltrading.ml.ranking.MLStockRanking;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.util.MLActivities;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.model.TreeEnsembleModel;
import scala.Serializable;

import java.util.ArrayList;
import java.util.List;

public abstract class MLModelRanking<R extends TreeEnsembleModel> implements Serializable {

    public JavaRDD<LabeledPoint> createRDD(JavaSparkContext sc, List<FeatureRank> fsL, PredictionPeriodicity periodicity) {

        JavaRDD<FeatureRank> data = sc.parallelize(fsL);

        List<LabeledPoint> test = new ArrayList<>();

        fsL.forEach(fs -> test.add(new LabeledPoint(fs.getResultValue(periodicity), Vectors.dense(fs.vectorize()))));

        JavaRDD<LabeledPoint> parsedData = data.map(
            (Function<FeatureRank, LabeledPoint>) fs -> new LabeledPoint(fs.getResultValue(periodicity), Vectors.dense(fs.vectorize()))
        );

        return parsedData;
    }


    public MLRank processRanking(List<StockGeneral> l, MLRank mlr) {


        List<FeatureRank> fsL = FeatureRank.create(l, MLStockRanking.RANGE_MAX);


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

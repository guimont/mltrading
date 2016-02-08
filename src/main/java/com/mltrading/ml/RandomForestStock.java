package com.mltrading.ml;

import java.util.*;

import com.mltrading.models.stock.Stock;
import org.apache.spark.mllib.linalg.Vectors;
import scala.Serializable;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.apache.spark.SparkConf;

/**
 * Created by gmo on 14/11/2015.
 */
public class RandomForestStock implements Serializable {


    public JavaRDD<LabeledPoint> createRDD(JavaSparkContext sc,  List<FeaturesStock> fsL) {

        JavaRDD<FeaturesStock> data = sc.parallelize(fsL);

        JavaRDD<LabeledPoint> parsedData = data.map(
            new Function<FeaturesStock, LabeledPoint>() {
                public LabeledPoint call(FeaturesStock fs) {
                    return new LabeledPoint(fs.getResultValue(), Vectors.dense(fs.vectorize()));
                }
            }

        );

        parsedData.cache();
        return parsedData;
    }

    public MLStock processRF(Stock stock) {

        List<FeaturesStock> fsL = FeaturesStock.create(stock);

        if (null == fsL) return null;

        List<FeaturesStock> fsLTrain =fsL.subList(0,(int)(fsL.size()*0.7));
        List<FeaturesStock> fsLTest =fsL.subList((int)(fsL.size()*0.7), fsL.size());

        JavaSparkContext sc = CacheMLStock.getJavaSparkContext();

        // Load and parse the data file.
        JavaRDD<LabeledPoint> trainingData = createRDD(sc, fsLTrain);
        JavaRDD<FeaturesStock> testData = sc.parallelize(fsLTest);

        // Split the data into training and test sets (30% held out for testing)

        /*JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];*/


        // Set parameters.
        //  Empty categoricalFeaturesInfo indicates all features are continuous.
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
        String impurity = "variance";
        Integer maxDepth = 6;
        Integer maxBins = 32;
        Integer numTrees = 60; // Use more in practice.
        String featureSubsetStrategy = "auto"; // Let the algorithm choose.

        // Train a RandomForest model.
        final RandomForestModel model = RandomForest.trainRegressor(trainingData,
            categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins, maxBins);


        MLStock mls = new MLStock();
        mls.setCodif(stock.getCodeif());
        mls.setModel(model);

        JavaRDD<FeaturesStock> predictionAndLabel = testData.map(
            new Function<FeaturesStock, FeaturesStock>() {
                public FeaturesStock call(FeaturesStock fs) {
                    double pred = model.predict(Vectors.dense(fs.vectorize()));
                    return new FeaturesStock(fs, pred);
                }
            }
        );

        predictionAndLabel.cache();
        mls.setTestData(predictionAndLabel);

        JavaRDD<MLPerformance> res =
            predictionAndLabel.map(new Function <FeaturesStock, MLPerformance>() {
                public MLPerformance call(FeaturesStock pl) {
                    System.out.println("estimate: " + pl.getPredictionValue());
                    System.out.println("result: " + pl.getResultValue());
                    //Double diff = pl.getPredictionValue() - pl.getResultValue();
                    return MLPerformance.calculYields(pl.getDate(), pl.getPredictionValue(), pl.getResultValue(), pl.getCurrentValue());
                }
            });

        mls.setPerfList(res.collect());

        return mls;

    }
}

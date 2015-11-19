package com.mltrading.ml;

import java.util.*;

import com.mltrading.models.stock.StockHistory;
import org.apache.spark.mllib.linalg.Vectors;
import scala.Tuple2;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.SparkConf;

/**
 * Created by gmo on 14/11/2015.
 */
public class RandomForestStock {

    SparkConf sparkConf = new SparkConf().setAppName("JavaRandomForest");
    JavaSparkContext sc = new JavaSparkContext(sparkConf);

    public void createRDD() {
        List<StockHistory> test = new ArrayList<StockHistory>();
        JavaRDD<StockHistory> data = sc.parallelize(test);

        JavaRDD<LabeledPoint> parsedData = data.map(
            new Function<StockHistory, LabeledPoint>() {
                public LabeledPoint call(StockHistory line) {
                    return new LabeledPoint(0., Vectors.dense(null));
                }
            }
        );

        parsedData.cache();
    }

    public void processRF() {


        // Load and parse the data file.
        String datapath = "data/mllib/sample_libsvm_data.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc.sc(), datapath).toJavaRDD();
        // Split the data into training and test sets (30% held out for testing)
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];


        // Set parameters.
        //  Empty categoricalFeaturesInfo indicates all features are continuous.
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
        String impurity = "variance";
        Integer maxDepth = 4;
        Integer maxBins = 32;
        Integer numTrees = 3; // Use more in practice.
        String featureSubsetStrategy = "auto"; // Let the algorithm choose.

        // Train a RandomForest model.
        final RandomForestModel model = RandomForest.trainRegressor(trainingData,
            categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins, maxBins);

        // Evaluate model on test instances and compute test error
        JavaPairRDD<Double, Double> predictionAndLabel =
            testData.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
                @Override
                public Tuple2<Double, Double> call(LabeledPoint p) {
                    return new Tuple2<Double, Double>(model.predict(p.features()), p.label());
                }
            });
        Double testMSE =
            predictionAndLabel.map(new Function<Tuple2<Double, Double>, Double>() {
                @Override
                public Double call(Tuple2<Double, Double> pl) {
                    Double diff = pl._1() - pl._2();
                    return diff * diff;
                }
            }).reduce(new Function2<Double, Double, Double>() {
                @Override
                public Double call(Double a, Double b) {
                    return a + b;
                }
            }) / testData.count();


        System.out.println("Test Mean Squared Error: " + testMSE);
        System.out.println("Learned regression forest model:\n" + model.toDebugString());

        // Save and load model
        //model.save(sc.sc(), "myModelPath");
        //RandomForestModel sameModel = RandomForestModel.load(sc.sc(), "myModelPath");
    }
}

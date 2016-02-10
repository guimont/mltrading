package com.mltrading.ml;

import java.util.*;

import com.mltrading.models.stock.Stock;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.storage.StorageLevel;
import scala.Serializable;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import com.mltrading.ml.FeaturesStock.PredictionPeriodicity;
/**
 * Created by gmo on 14/11/2015.
 */
public class RandomForestStock implements Serializable {


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

    public MLStocks processRF(Stock stock) {

        List<FeaturesStock> fsL = FeaturesStock.create(stock);

        if (null == fsL) return null;

        List<FeaturesStock> fsLTrain =fsL.subList(0,(int)(fsL.size()*0.7));
        List<FeaturesStock> fsLTest =fsL.subList((int)(fsL.size()*0.7), fsL.size());

        JavaSparkContext sc = CacheMLStock.getJavaSparkContext();

        // Load and parse the data file.
        JavaRDD<LabeledPoint> trainingDataD1 = createRDD(sc, fsLTrain, PredictionPeriodicity.D1);
        JavaRDD<LabeledPoint> trainingDataD5 = createRDD(sc, fsLTrain, PredictionPeriodicity.D5);
        JavaRDD<LabeledPoint> trainingDataD20 = createRDD(sc, fsLTrain, PredictionPeriodicity.D20);

        JavaRDD<FeaturesStock> testData = sc.parallelize(fsLTest);

        // Split the data into training and test sets (30% held out for testing)

        // Set parameters.
        //  Empty categoricalFeaturesInfo indicates all features are continuous.
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
        String impurity = "variance";
        Integer maxDepth = 6;
        Integer maxBins = 32;
        Integer numTrees = 60; // Use more in practice.
        String featureSubsetStrategy = "auto"; // Let the algorithm choose.

        // Train a RandomForest model.
        final RandomForestModel modelD1 = RandomForest.trainRegressor(trainingDataD1,
            categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins, maxBins);

        // Train a RandomForest model.
        final RandomForestModel modelD5 = RandomForest.trainRegressor(trainingDataD5,
            categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins, maxBins);

        // Train a RandomForest model.
        final RandomForestModel modelD20 = RandomForest.trainRegressor(trainingDataD20,
            categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins, maxBins);


        MLStocks mls = new MLStocks(stock.getCodeif());

        mls.getMlD1().setModel(modelD1);
        mls.getMlD5().setModel(modelD5);
        mls.getMlD20().setModel(modelD20);


        JavaRDD<FeaturesStock> predictionAndLabel = testData.map(
            new Function<FeaturesStock, FeaturesStock>() {
                public FeaturesStock call(FeaturesStock fs) {
                    double pred = modelD1.predict(Vectors.dense(fs.vectorize()));
                    FeaturesStock fsResult = new FeaturesStock(fs, pred, PredictionPeriodicity.D1);
                    if (fs.getResultValue(PredictionPeriodicity.D5) != 0) {
                        pred = modelD5.predict(Vectors.dense(fs.vectorize()));
                        fsResult.setPredictionValue(pred,PredictionPeriodicity.D5);
                    }

                    return fsResult;
                }
            }
        );


        mls.setTestData(predictionAndLabel);

        JavaRDD<MLPerformance> res =
            predictionAndLabel.map(new Function <FeaturesStock, MLPerformance>() {
                public MLPerformance call(FeaturesStock pl) {
                    System.out.println("estimate: " + pl.getPredictionValue(PredictionPeriodicity.D1));
                    System.out.println("result: " + pl.getResultValue(PredictionPeriodicity.D1));
                    //Double diff = pl.getPredictionValue() - pl.getResultValue();
                    List<MLPerformance> perfList = new ArrayList<MLPerformance>();

                   return  MLPerformance.calculYields(pl.getDate(), pl.getPredictionValue(PredictionPeriodicity.D1), pl.getResultValue(PredictionPeriodicity.D1), pl.getCurrentValue());

                    /*if (pl.getResultValue(PredictionPeriodicity.D5) != 0)
                        perfList.add(MLPerformance.calculYields(pl.getDate(), pl.getPredictionValue(PredictionPeriodicity.D5), pl.getResultValue(PredictionPeriodicity.D5), pl.getCurrentValue()));

                    //perfList.add();
                    return perfList;*/
                }
            });



        mls.getMlD1().setPerfList(res.collect());
        //mls.getMlD5().setPerfList(res.collect().get(1));

        return mls;

    }
}

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

    public MLStocks processRF(Stock stock, boolean generate) {

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
        Map<Integer, Integer> categoricalFeaturesInfoD1 = new HashMap<Integer, Integer>();
        Map<Integer, Integer> categoricalFeaturesInfoD5 = new HashMap<Integer, Integer>();
        Map<Integer, Integer> categoricalFeaturesInfoD20 = new HashMap<Integer, Integer>();
        String impurity = "variance";

        String featureSubsetStrategy = "auto"; // Let the algorithm choose.

        MLStocks mls = new MLStocks(stock.getCodeif());

        if (generate) {
            mls.getMlD1().getValidator().generate();
            mls.getMlD5().getValidator().generate();
            mls.getMlD20().getValidator().generate();
        }

        // Train a RandomForest model.
        final RandomForestModel modelD1 = RandomForest.trainRegressor(trainingDataD1,
            categoricalFeaturesInfoD1, mls.getMlD1().getValidator().getNumTrees(), featureSubsetStrategy, impurity,
            mls.getMlD1().getValidator().getMaxDepth(), mls.getMlD1().getValidator().getMaxBins(),
            mls.getMlD1().getValidator().getSeed());

        // Train a RandomForest model.
        final RandomForestModel modelD5 = RandomForest.trainRegressor(trainingDataD5,
            categoricalFeaturesInfoD5,  mls.getMlD5().getValidator().getNumTrees(), featureSubsetStrategy, impurity,
            mls.getMlD5().getValidator().getMaxDepth(), mls.getMlD5().getValidator().getMaxBins(),
            mls.getMlD5().getValidator().getSeed());

        // Train a RandomForest model.
        final RandomForestModel modelD20 = RandomForest.trainRegressor(trainingDataD20,
            categoricalFeaturesInfoD20, mls.getMlD20().getValidator().getNumTrees(), featureSubsetStrategy, impurity,
            mls.getMlD20().getValidator().getMaxDepth(), mls.getMlD20().getValidator().getMaxBins(),
            mls.getMlD20().getValidator().getSeed());




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

                    if (fs.getResultValue(PredictionPeriodicity.D20) != 0) {
                        pred = modelD20.predict(Vectors.dense(fs.vectorize()));
                        fsResult.setPredictionValue(pred,PredictionPeriodicity.D20);
                    }

                    return fsResult;
                }
            }
        );


        mls.setTestData(predictionAndLabel);

        JavaRDD<MLPerformances> res =
            predictionAndLabel.map(new Function <FeaturesStock, MLPerformances>() {
                public MLPerformances call(FeaturesStock pl) {
                    System.out.println("estimate: " + pl.getPredictionValue(PredictionPeriodicity.D1));
                    System.out.println("result: " + pl.getResultValue(PredictionPeriodicity.D1));
                    //Double diff = pl.getPredictionValue() - pl.getResultValue();
                    MLPerformances perf = new MLPerformances(pl.getDate());

                    perf.setMlD1(MLPerformance.calculYields(pl.getDate(), pl.getPredictionValue(PredictionPeriodicity.D1), pl.getResultValue(PredictionPeriodicity.D1), pl.getCurrentValue()));

                    if (pl.getResultValue(PredictionPeriodicity.D5) != 0)
                        perf.setMlD5(MLPerformance.calculYields(pl.getDate(), pl.getPredictionValue(PredictionPeriodicity.D5), pl.getResultValue(PredictionPeriodicity.D5), pl.getCurrentValue()));

                    if (pl.getResultValue(PredictionPeriodicity.D20) != 0)
                        perf.setMlD20(MLPerformance.calculYields(pl.getDate(), pl.getPredictionValue(PredictionPeriodicity.D20), pl.getResultValue(PredictionPeriodicity.D20), pl.getCurrentValue()));


                    return perf;

                }
            });



        mls.setPerfList(res.collect());
        //mls.getMlD5().setPerfList(res.collect().get(1));

        return mls;

    }
}

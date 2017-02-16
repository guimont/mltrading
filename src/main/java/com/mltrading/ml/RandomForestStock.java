package com.mltrading.ml;

import java.util.*;


import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.util.MLActivities;
import org.apache.spark.mllib.linalg.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Serializable;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;

/**
 * Created by gmo on 14/11/2015.
 */
public class RandomForestStock implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(RandomForestStock.class);


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



    @Deprecated
    public MLStocks processRF(String codif, MLStocks mls) {

        CacheMLActivities.addActivities(new MLActivities("FeaturesStock", codif,"start",0,0,false));
        List<FeaturesStock> fsL = FeaturesStock.create(codif, mls.getValidator(PredictionPeriodicity.D1), CacheMLStock.RANGE_MAX);
        CacheMLActivities.addActivities(new MLActivities("FeaturesStock", codif,"start",0,0,true));

        if (null == fsL) return null;

        int born = fsL.size() - CacheMLStock.RENDERING;

        List<FeaturesStock> fsLTrain =fsL.subList(0,born);
        List<FeaturesStock> fsLTest =fsL.subList(born, fsL.size());

        JavaSparkContext sc = CacheMLStock.getJavaSparkContext();

        // Load and parse the data file.
        JavaRDD<LabeledPoint> trainingDataD1 = createRDD(sc, fsLTrain, PredictionPeriodicity.D1);
        JavaRDD<LabeledPoint> trainingDataD5 = createRDD(sc, fsLTrain, PredictionPeriodicity.D5);
        JavaRDD<LabeledPoint> trainingDataD20 = createRDD(sc, fsLTrain, PredictionPeriodicity.D20);
        JavaRDD<LabeledPoint> trainingDataD40 = createRDD(sc, fsLTrain, PredictionPeriodicity.D40);

        JavaRDD<FeaturesStock> testData = sc.parallelize(fsLTest);

        // Split the data into training and test sets (30% held out for testing)

        // Set parameters.
        //  Empty categoricalFeaturesInfo indicates all features are continuous.
        Map<Integer, Integer> categoricalFeaturesInfoD1 = new HashMap<Integer, Integer>();
        Map<Integer, Integer> categoricalFeaturesInfoD5 = new HashMap<Integer, Integer>();
        Map<Integer, Integer> categoricalFeaturesInfoD20 = new HashMap<Integer, Integer>();
        Map<Integer, Integer> categoricalFeaturesInfoD40 = new HashMap<Integer, Integer>();
        String impurity = "variance";

        String featureSubsetStrategy = "auto"; // Let the algorithm choose.


        // Train a RandomForest model.
        final RandomForestModel modelD1 = RandomForest.trainRegressor(trainingDataD1,
            categoricalFeaturesInfoD1, mls.getValidator(PredictionPeriodicity.D1).getNumTrees(), featureSubsetStrategy, impurity,
            mls.getValidator(PredictionPeriodicity.D1).getMaxDepth(), mls.getValidator(PredictionPeriodicity.D1).getMaxBins(),
            mls.getValidator(PredictionPeriodicity.D1).getSeed());

        // Train a RandomForest model.
        final RandomForestModel modelD5 = RandomForest.trainRegressor(trainingDataD5,
            categoricalFeaturesInfoD5,  mls.getValidator(PredictionPeriodicity.D5).getNumTrees(), featureSubsetStrategy, impurity,
            mls.getValidator(PredictionPeriodicity.D5).getMaxDepth(), mls.getValidator(PredictionPeriodicity.D5).getMaxBins(),
            mls.getValidator(PredictionPeriodicity.D5).getSeed());

        // Train a RandomForest model.
        final RandomForestModel modelD20 = RandomForest.trainRegressor(trainingDataD20,
            categoricalFeaturesInfoD20, mls.getValidator(PredictionPeriodicity.D20).getNumTrees(), featureSubsetStrategy, impurity,
            mls.getValidator(PredictionPeriodicity.D20).getMaxDepth(), mls.getValidator(PredictionPeriodicity.D20).getMaxBins(),
            mls.getValidator(PredictionPeriodicity.D20).getSeed());


        // Train a RandomForest model.
        final RandomForestModel modelD40 = RandomForest.trainRegressor(trainingDataD40,
            categoricalFeaturesInfoD40, mls.getValidator(PredictionPeriodicity.D40).getNumTrees(), featureSubsetStrategy, impurity,
            mls.getValidator(PredictionPeriodicity.D40).getMaxDepth(), mls.getValidator(PredictionPeriodicity.D40).getMaxBins(),
            mls.getValidator(PredictionPeriodicity.D40).getSeed());


        mls.setModel(PredictionPeriodicity.D1, modelD1);
        mls.setModel(PredictionPeriodicity.D5, modelD5);
        mls.setModel(PredictionPeriodicity.D20, modelD20);
        mls.setModel(PredictionPeriodicity.D40, modelD40);


        mls.getValidator(PredictionPeriodicity.D1).setVectorSize(fsL.get(0).currentVectorPos);
        mls.getValidator(PredictionPeriodicity.D5).setVectorSize(fsL.get(0).currentVectorPos);
        mls.getValidator(PredictionPeriodicity.D20).setVectorSize(fsL.get(0).currentVectorPos);
        mls.getValidator(PredictionPeriodicity.D40).setVectorSize(fsL.get(0).currentVectorPos);

        JavaRDD<FeaturesStock> predictionAndLabel = testData.map(
            new Function<FeaturesStock, FeaturesStock>() {
                public FeaturesStock call(FeaturesStock fs) {

                    double pred = modelD1.predict(Vectors.dense(fs.vectorize()));
                    FeaturesStock fsResult = new FeaturesStock(fs, pred, PredictionPeriodicity.D1);

                        pred = modelD5.predict(Vectors.dense(fs.vectorize()));
                        fsResult.setPredictionValue(pred,PredictionPeriodicity.D5);
                        fsResult.setDate(fs.getDate(PredictionPeriodicity.D5), PredictionPeriodicity.D5);

                        pred = modelD20.predict(Vectors.dense(fs.vectorize()));
                        fsResult.setPredictionValue(pred,PredictionPeriodicity.D20);
                        fsResult.setDate(fs.getDate(PredictionPeriodicity.D20), PredictionPeriodicity.D20);

                        pred = modelD40.predict(Vectors.dense(fs.vectorize()));
                        fsResult.setPredictionValue(pred,PredictionPeriodicity.D40);
                        fsResult.setDate(fs.getDate(PredictionPeriodicity.D40), PredictionPeriodicity.D40);


                    return fsResult;
                }
            }
        );


        //mls.setTestData(predictionAndLabel);

        JavaRDD<MLPerformances> res =
            predictionAndLabel.map(new Function <FeaturesStock, MLPerformances>() {
                public MLPerformances call(FeaturesStock pl) {
                    System.out.println("estimate: " + pl.getPredictionValue(PredictionPeriodicity.D1));
                    System.out.println("result: " + pl.getResultValue(PredictionPeriodicity.D1));
                    //Double diff = pl.getPredictionValue() - pl.getResultValue();
                    MLPerformances perf = new MLPerformances(pl.getCurrentDate());

                    perf.setMl(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D1), pl.getPredictionValue(PredictionPeriodicity.D1), pl.getResultValue(PredictionPeriodicity.D1), pl.getCurrentValue()), PredictionPeriodicity.D1);

                    //if (pl.getResultValue(PredictionPeriodicity.D5) != 0)
                    perf.setMl(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D5), pl.getPredictionValue(PredictionPeriodicity.D5), pl.getResultValue(PredictionPeriodicity.D5), pl.getCurrentValue()), PredictionPeriodicity.D5);

                    //if (pl.getResultValue(PredictionPeriodicity.D20) != 0)
                    perf.setMl(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D20), pl.getPredictionValue(PredictionPeriodicity.D20), pl.getResultValue(PredictionPeriodicity.D20), pl.getCurrentValue()), PredictionPeriodicity.D20);

                    perf.setMl(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D40), pl.getPredictionValue(PredictionPeriodicity.D40), pl.getResultValue(PredictionPeriodicity.D40), pl.getCurrentValue()), PredictionPeriodicity.D40);


                    return perf;

                }
            });


        mls.getStatus().setPerfList(res.collect());

        return mls;

    }



    public MLStocks processRFRef(String codif, MLStocks mls) {

        CacheMLActivities.addActivities(new MLActivities("FeaturesStock", codif, "start", 0, 0, false));
        List<FeaturesStock> fsL = FeaturesStock.create(codif, mls.getValidator(PredictionPeriodicity.D1), CacheMLStock.RANGE_MAX);
        CacheMLActivities.addActivities(new MLActivities("FeaturesStock", codif, "start", 0, 0, true));

        subprocessRF( mls,  fsL, PredictionPeriodicity.D1);
        subprocessRF( mls,  fsL, PredictionPeriodicity.D5);
        subprocessRF( mls,  fsL, PredictionPeriodicity.D20);
        subprocessRF( mls,  fsL, PredictionPeriodicity.D40);


        return mls;
    }



    public MLStocks subprocessRF(MLStocks mls,  List<FeaturesStock> fsL, PredictionPeriodicity period) {


        if (null == fsL) return null;

        int born = fsL.size() - CacheMLStock.RENDERING;

        List<FeaturesStock> fsLTrain =fsL.subList(0,born);
        List<FeaturesStock> fsLTest =fsL.subList(born, fsL.size());

        JavaSparkContext sc = CacheMLStock.getJavaSparkContext();

        // Load and parse the data file.
        JavaRDD<LabeledPoint> trainingData = createRDD(sc, fsLTrain, period);

        JavaRDD<FeaturesStock> testData = sc.parallelize(fsLTest);

        // Split the data into training and test sets (30% held out for testing)

        // Set parameters.
        //  Empty categoricalFeaturesInfo indicates all features are continuous.
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();

        String impurity = "variance";

        String featureSubsetStrategy = "auto"; // Let the algorithm choose.


        // Train a RandomForest model.
        final RandomForestModel model = RandomForest.trainRegressor(trainingData,
            categoricalFeaturesInfo, mls.getValidator(period).getNumTrees(), featureSubsetStrategy, impurity,
            mls.getValidator(period).getMaxDepth(), mls.getValidator(period).getMaxBins(),
            mls.getValidator(period).getSeed());


        mls.setModel(period, model);



        mls.getValidator(period).setVectorSize(fsL.get(0).currentVectorPos);


        JavaRDD<FeaturesStock> predictionAndLabel = testData.map(
            new Function<FeaturesStock, FeaturesStock>() {
                public FeaturesStock call(FeaturesStock fs) {

                    double pred = model.predict(Vectors.dense(fs.vectorize()));
                    FeaturesStock fsResult = new FeaturesStock(fs, pred, period);

                    fsResult.setPredictionValue(pred,period);
                    fsResult.setDate(fs.getDate(period), period);

                    return fsResult;
                }
            }
        );



        JavaRDD<MLPerformances> res =
            predictionAndLabel.map(new Function <FeaturesStock, MLPerformances>() {
                public MLPerformances call(FeaturesStock pl) {
                    System.out.println("estimate: " + pl.getPredictionValue(period));
                    System.out.println("result: " + pl.getResultValue(period));
                    //Double diff = pl.getPredictionValue() - pl.getResultValue();
                    MLPerformances perf = new MLPerformances(pl.getCurrentDate());
                    perf.setMl(MLPerformance.calculYields(pl.getDate(period), pl.getPredictionValue(period), pl.getResultValue(period), pl.getCurrentValue()), period);

                    return perf;

                }
            });


        try {
            mls.getStatus().setPerfList(res.collect(),period);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mls;

    }


    public MLStocks processRF(String codif, MLStocks mls, PredictionPeriodicity period) {
        MatrixValidator validator = mls.getSock(period).getValidator();

        List<FeaturesStock> fsL = FeaturesStock.create(codif, validator , CacheMLStock.RANGE_MAX);

        if (null == fsL) return null;

        int born = fsL.size() - CacheMLStock.RENDERING;

        List<FeaturesStock> fsLTrain =fsL.subList(0,born);
        List<FeaturesStock> fsLTest =fsL.subList(born, fsL.size());

        JavaSparkContext sc = CacheMLStock.getJavaSparkContext();

        // Load and parse the data file.
        JavaRDD<LabeledPoint> trainingData = createRDD(sc, fsLTrain, period);


        JavaRDD<FeaturesStock> testData = sc.parallelize(fsLTest);

        // Split the data into training and test sets (30% held out for testing)

        // Set parameters.
        //  Empty categoricalFeaturesInfo indicates all features are continuous.
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();

        String impurity = "variance";

        String featureSubsetStrategy = "auto"; // Let the algorithm choose.


        // Train a RandomForest model.
        final RandomForestModel model = RandomForest.trainRegressor(trainingData,
            categoricalFeaturesInfo, validator.getNumTrees(), featureSubsetStrategy, impurity,
            validator.getMaxDepth(), validator.getMaxBins(),
            validator.getSeed());




        mls.getSock(period).setModel(model);

        validator.setVectorSize(fsL.get(0).currentVectorPos);


        JavaRDD<FeaturesStock> predictionAndLabel = testData.map(
            new Function<FeaturesStock, FeaturesStock>() {
                public FeaturesStock call(FeaturesStock fs) {

                    double pred = model.predict(Vectors.dense(fs.vectorize()));
                    FeaturesStock fsResult = new FeaturesStock(fs, pred, period);
                    return fsResult;
                }
            }
        );


        //mls.setTestData(predictionAndLabel);

        JavaRDD<MLPerformances> res =
            predictionAndLabel.map(new Function <FeaturesStock, MLPerformances>() {
                public MLPerformances call(FeaturesStock pl) {
                    System.out.println("estimate: " + pl.getPredictionValue(PredictionPeriodicity.D1));
                    System.out.println("result: " + pl.getResultValue(PredictionPeriodicity.D1));
                    //Double diff = pl.getPredictionValue() - pl.getResultValue();
                    MLPerformances perf = new MLPerformances(pl.getCurrentDate());
                    if (pl.getDate(period) != null && !pl.getDate(period).isEmpty())
                        perf.setPerf(period, MLPerformance.calculYields(pl.getDate(period), pl.getPredictionValue(period), pl.getResultValue(period), pl.getCurrentValue()));

                    return perf;

                }
            });


        mls.getStatus().mergeList(res.collect());

        return mls;

    }



    public MLStocks processRFResult(String codif, MLStocks mls) {

        List<FeaturesStock> fsLD1 = FeaturesStock.create(codif, mls.getValidator(PredictionPeriodicity.D1), FeaturesStock.RANGE_TEST);
        if( fsLD1.get(0).currentVectorPos != mls.getValidator(PredictionPeriodicity.D1).getVectorSize())    {
            log.error("size vector not corresponding");
            log.error("validator: " + mls.getValidator(PredictionPeriodicity.D1).getVectorSize());
            log.error("vector: " + fsLD1.get(0).currentVectorPos );
        }
        List<FeaturesStock> fsLD5 = FeaturesStock.create(codif, mls.getValidator(PredictionPeriodicity.D5), FeaturesStock.RANGE_TEST);
        if( fsLD5.get(0).currentVectorPos != mls.getValidator(PredictionPeriodicity.D5).getVectorSize())    {
            log.error("size vector not corresponding");
            log.error("validator: " + mls.getValidator(PredictionPeriodicity.D5).getVectorSize());
            log.error("vector: " + fsLD5.get(0).currentVectorPos );
        }
        List<FeaturesStock> fsLD20 = FeaturesStock.create(codif, mls.getValidator(PredictionPeriodicity.D20), FeaturesStock.RANGE_TEST);
        if( fsLD20.get(0).currentVectorPos != mls.getValidator(PredictionPeriodicity.D20).getVectorSize())    {
            log.error("size vector not corresponding");
            log.error("validator: " + mls.getValidator(PredictionPeriodicity.D20).getVectorSize());
            log.error("vector: " + fsLD20.get(0).currentVectorPos );
        }

        List<FeaturesStock> fsLD40 = FeaturesStock.create(codif, mls.getValidator(PredictionPeriodicity.D40), FeaturesStock.RANGE_TEST);
        if( fsLD40.get(0).currentVectorPos != mls.getValidator(PredictionPeriodicity.D40).getVectorSize())    {
            log.error("size vector not corresponding");
            log.error("validator: " + mls.getValidator(PredictionPeriodicity.D40).getVectorSize());
            log.error("vector: " + fsLD40.get(0).currentVectorPos );
        }

        if (null == fsLD1) return null;

        //JavaSparkContext sc = CacheMLStock.getJavaSparkContext();
        //JavaRDD<FeaturesStock> testData = sc.parallelize(fsL);

        // Split the data into training and test sets (30% held out for testing)

        List<FeaturesStock> resFSList = new ArrayList<>();

        for (int i = 0; i < fsLD1.size(); i++)
        {
            double  pred = 0;
            try {
                FeaturesStock fsD1 = fsLD1.get(i);
                pred = mls.getModel(PredictionPeriodicity.D1).predict(Vectors.dense(fsD1.vectorize()));
            } catch (Exception e) {
                System.out.print(e.toString());
            }


            FeaturesStock fsResult = new FeaturesStock(fsLD1.get(i), pred, PredictionPeriodicity.D1);

            FeaturesStock fsD5 = fsLD5.get(i);
            pred = mls.getModel(PredictionPeriodicity.D5).predict(Vectors.dense(fsD5.vectorize()));
            fsResult.setPredictionValue(pred, PredictionPeriodicity.D5);
            fsResult.setDate(fsD5.getDate( PredictionPeriodicity.D5),  PredictionPeriodicity.D5);


            FeaturesStock fsD20 = fsLD20.get(i);
            pred = mls.getModel(PredictionPeriodicity.D20).predict(Vectors.dense(fsD20.vectorize()));
            fsResult.setPredictionValue(pred, PredictionPeriodicity.D20);
            fsResult.setDate(fsD20.getDate( PredictionPeriodicity.D20),  PredictionPeriodicity.D20);

            FeaturesStock fsD40 = fsLD40.get(i);
            pred = mls.getModel(PredictionPeriodicity.D40).predict(Vectors.dense(fsD40.vectorize()));
            fsResult.setPredictionValue(pred, PredictionPeriodicity.D40);
            fsResult.setDate(fsD40.getDate( PredictionPeriodicity.D40),  PredictionPeriodicity.D40);

            resFSList.add(fsResult);
        }



        List<MLPerformances> resList = new ArrayList<>();
        for (FeaturesStock pl :resFSList) {
            System.out.println("estimate: " + pl.getPredictionValue(PredictionPeriodicity.D1));
            System.out.println("result: " + pl.getResultValue(PredictionPeriodicity.D1));
            //Double diff = pl.getPredictionValue() - pl.getResultValue();
            MLPerformances perf = new MLPerformances(pl.getCurrentDate());

            perf.setMl(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D1), pl.getPredictionValue(PredictionPeriodicity.D1), pl.getResultValue(PredictionPeriodicity.D1), pl.getCurrentValue()), PredictionPeriodicity.D1);

            if (pl.getResultValue(PredictionPeriodicity.D5) != 0)
                perf.setMl(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D5), pl.getPredictionValue(PredictionPeriodicity.D5), pl.getResultValue(PredictionPeriodicity.D5), pl.getCurrentValue()), PredictionPeriodicity.D5);
            else
                perf.setMl(new MLPerformance(pl.getDate(PredictionPeriodicity.D5),pl.getPredictionValue(PredictionPeriodicity.D5), -1, pl.getCurrentValue(), 0, 0, true), PredictionPeriodicity.D5);


            if (pl.getResultValue(PredictionPeriodicity.D20) != 0)
                perf.setMl(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D20), pl.getPredictionValue(PredictionPeriodicity.D20), pl.getResultValue(PredictionPeriodicity.D20), pl.getCurrentValue()), PredictionPeriodicity.D20);
            else
                perf.setMl(new MLPerformance(pl.getDate(PredictionPeriodicity.D20), pl.getPredictionValue(PredictionPeriodicity.D20), -1, pl.getCurrentValue(), 0, 0, true), PredictionPeriodicity.D20);

            if (pl.getResultValue(PredictionPeriodicity.D40) != 0)
                perf.setMl(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D40), pl.getPredictionValue(PredictionPeriodicity.D40), pl.getResultValue(PredictionPeriodicity.D40), pl.getCurrentValue()), PredictionPeriodicity.D40);
            else
                perf.setMl(new MLPerformance(pl.getDate(PredictionPeriodicity.D40), pl.getPredictionValue(PredictionPeriodicity.D40), -1, pl.getCurrentValue(), 0, 0, true), PredictionPeriodicity.D40);



            resList.add(perf);

        }


        mls.getStatus().setPerfList(resList);

        return mls;

    }
}

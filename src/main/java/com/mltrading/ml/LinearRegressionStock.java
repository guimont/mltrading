package com.mltrading.ml;

import com.mltrading.models.stock.StockGeneral;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import scala.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 14/11/2015.
 */
public class LinearRegressionStock implements Serializable {


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

    public MLStocks processRF(StockGeneral stock, MLStocks mls) {

        List<FeaturesStock> fsL = FeaturesStock.create(stock, mls.getMlD1().getValidator(), FeaturesStock.RANGE_MAX);

        if (null == fsL) return null;

        List<FeaturesStock> fsLTrain =fsL.subList(0,(int)(fsL.size()*0.7));
        List<FeaturesStock> fsLTest =fsL.subList((int)(fsL.size()*0.7), fsL.size());

        JavaSparkContext sc = CacheMLStock.getJavaSparkContext();

        // Load and parse the data file.
        JavaRDD<LabeledPoint> trainingDataD1 = createRDD(sc, fsLTrain, PredictionPeriodicity.D1);
        /*JavaRDD<LabeledPoint> trainingDataD5 = createRDD(sc, fsLTrain, PredictionPeriodicity.D5);
        JavaRDD<LabeledPoint> trainingDataD20 = createRDD(sc, fsLTrain, PredictionPeriodicity.D20);*/

        JavaRDD<FeaturesStock> testData = sc.parallelize(fsLTest);


        trainingDataD1.cache();

        // Train a RandomForest model.
        int numIterations = 300;
        //final LinearRegressionModel modelD1 = LinearRegressionWithSGD.train(JavaRDD.toRDD(trainingDataD1), numIterations, 5);
        //final LinearRegressionModel modelD5 = LinearRegressionWithSGD.train(JavaRDD.toRDD(trainingDataD5), numIterations);
        //final LinearRegressionModel modelD20 = LinearRegressionWithSGD.train(JavaRDD.toRDD(trainingDataD20), numIterations);


        LinearRegressionModel modelD1 = (LinearRegressionModel) new LinearRegressionWithSGD(0.2, numIterations, 1.0).
            setIntercept(true).
            run(JavaRDD.toRDD(trainingDataD1));

        /*mls.getMlD1().setModel(modelD1);
        mls.getMlD5().setModel(modelD5);
        mls.getMlD20().setModel(modelD20);*/

        mls.getMlD1().getValidator().setVectorSize(fsL.get(0).currentVectorPos);
        mls.getMlD5().getValidator().setVectorSize(fsL.get(0).currentVectorPos);
        mls.getMlD20().getValidator().setVectorSize(fsL.get(0).currentVectorPos);

        JavaRDD<FeaturesStock> predictionAndLabel = testData.map(
            new Function<FeaturesStock, FeaturesStock>() {
                public FeaturesStock call(FeaturesStock fs) {

                    double pred = modelD1.predict(Vectors.dense(fs.vectorize()));
                    FeaturesStock fsResult = new FeaturesStock(fs, pred, PredictionPeriodicity.D1);
                    /*if (fs.getResultValue(PredictionPeriodicity.D5) != 0) {
                        pred = modelD5.predict(Vectors.dense(fs.vectorize()));
                        fsResult.setPredictionValue(pred,PredictionPeriodicity.D5);
                    }

                    if (fs.getResultValue(PredictionPeriodicity.D20) != 0) {
                        pred = modelD20.predict(Vectors.dense(fs.vectorize()));
                        fsResult.setPredictionValue(pred,PredictionPeriodicity.D20);
                    }*/

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
                    MLPerformances perf = new MLPerformances(pl.getCurrentDate());

                    perf.setMlD1(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D1), pl.getPredictionValue(PredictionPeriodicity.D1), pl.getResultValue(PredictionPeriodicity.D1), pl.getCurrentValue()));

                    if (pl.getResultValue(PredictionPeriodicity.D5) != 0)
                        perf.setMlD5(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D5), pl.getPredictionValue(PredictionPeriodicity.D5), pl.getResultValue(PredictionPeriodicity.D5), pl.getCurrentValue()));

                    if (pl.getResultValue(PredictionPeriodicity.D20) != 0)
                        perf.setMlD20(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D20), pl.getPredictionValue(PredictionPeriodicity.D20), pl.getResultValue(PredictionPeriodicity.D20), pl.getCurrentValue()));


                    return perf;

                }
            });


        mls.getStatus().setPerfList(res.collect());

        return mls;

    }



    public MLStocks processRFResult(StockGeneral stock, MLStocks mls) {

        List<FeaturesStock> fsLD1 = FeaturesStock.create(stock, mls.getMlD1().getValidator(), FeaturesStock.RANGE_TEST);
        List<FeaturesStock> fsLD5 = FeaturesStock.create(stock, mls.getMlD5().getValidator(), FeaturesStock.RANGE_TEST);
        List<FeaturesStock> fsLD20 = FeaturesStock.create(stock, mls.getMlD20().getValidator(), FeaturesStock.RANGE_TEST);

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
                pred = mls.getMlD1().getModel().predict(Vectors.dense(fsD1.vectorize()));
            } catch (Exception e) {
                System.out.print(e.toString());
            }


            FeaturesStock fsResult = new FeaturesStock(fsLD1.get(i), pred, PredictionPeriodicity.D1);

            if (fsLD5.size() > i) {
                FeaturesStock fsD5 = fsLD5.get(i);
                if (fsD5.getResultValue(PredictionPeriodicity.D5) != 0) {
                    pred = mls.getMlD5().getModel().predict(Vectors.dense(fsD5.vectorize()));
                    fsResult.setPredictionValue(pred, PredictionPeriodicity.D5);
                }
            }

            if (fsLD20.size() > i) {
                FeaturesStock fsD20 = fsLD20.get(i);
                if (fsD20.getResultValue(PredictionPeriodicity.D20) != 0) {
                    pred = mls.getMlD5().getModel().predict(Vectors.dense(fsD20.vectorize()));
                    fsResult.setPredictionValue(pred, PredictionPeriodicity.D20);
                }
            }
            resFSList.add(fsResult);
        }



        List<MLPerformances> resList = new ArrayList<>();
        for (FeaturesStock pl :resFSList) {
            System.out.println("estimate: " + pl.getPredictionValue(PredictionPeriodicity.D1));
            System.out.println("result: " + pl.getResultValue(PredictionPeriodicity.D1));
            //Double diff = pl.getPredictionValue() - pl.getResultValue();
            MLPerformances perf = new MLPerformances(pl.getCurrentDate());

            perf.setMlD1(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D1), pl.getPredictionValue(PredictionPeriodicity.D1), pl.getResultValue(PredictionPeriodicity.D1), pl.getCurrentValue()));

            if (pl.getResultValue(PredictionPeriodicity.D5) != 0)
                perf.setMlD5(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D5), pl.getPredictionValue(PredictionPeriodicity.D5), pl.getResultValue(PredictionPeriodicity.D5), pl.getCurrentValue()));

            if (pl.getResultValue(PredictionPeriodicity.D20) != 0)
                perf.setMlD20(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D20), pl.getPredictionValue(PredictionPeriodicity.D20), pl.getResultValue(PredictionPeriodicity.D20), pl.getCurrentValue()));

            resList.add(perf);

        }



/*
        JavaRDD<FeaturesStock> predictionAndLabel = testData.map(
            new Function<FeaturesStock, FeaturesStock>() {
                public FeaturesStock call(FeaturesStock fs) {
                    double pred = mls.getMlD1().getModel().predict(Vectors.dense(fs.vectorize()));
                    FeaturesStock fsResult = new FeaturesStock(fs, pred, PredictionPeriodicity.D1);
                    if (fs.getResultValue(PredictionPeriodicity.D5) != 0) {
                        pred = mls.getMlD5().getModel().predict(Vectors.dense(fs.vectorize()));
                        fsResult.setPredictionValue(pred,PredictionPeriodicity.D5);
                    }

                    if (fs.getResultValue(PredictionPeriodicity.D20) != 0) {
                        pred = mls.getMlD20().getModel().predict(Vectors.dense(fs.vectorize()));
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

        if (res.isEmpty())
            return null;
            */

        mls.getStatus().setPerfList(resList);

        return mls;

    }
}

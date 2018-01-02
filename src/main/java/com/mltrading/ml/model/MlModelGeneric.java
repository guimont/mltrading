package com.mltrading.ml.model;

import com.mltrading.ml.*;
import com.mltrading.models.util.MLActivities;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Serializable;

import java.util.*;


/**
 * Created by gmo on 10/03/2017.
 */
public abstract class MlModelGeneric<Model> implements Serializable {


    private static final Logger log = LoggerFactory.getLogger(MlModelGeneric.class);

    private static final List<PredictionPeriodicity> periodicity = Arrays.asList(PredictionPeriodicity.D1, PredictionPeriodicity.D5, PredictionPeriodicity.D20, PredictionPeriodicity.D40);


    public JavaRDD<LabeledPoint> createRDD(JavaSparkContext sc, List<FeaturesStock> fsL, PredictionPeriodicity type) {

        JavaRDD<FeaturesStock> data = sc.parallelize(fsL);

        JavaRDD<LabeledPoint> parsedData = data.map(
            (Function<FeaturesStock, LabeledPoint>) fs -> new LabeledPoint(fs.getResultValue(type), Vectors.dense(fs.vectorize()))

        );


        return parsedData;
    }

    public MLStocks processRFRef(String codif, MLStocks mls, boolean merge) {

        CacheMLActivities.addActivities(new MLActivities("FeaturesStock", codif, "start", 0, 0, false));
        List<FeaturesStock> fsL = FeaturesStock.create(codif, getValidator(mls, PredictionPeriodicity.D1), CacheMLStock.RANGE_MAX);
        CacheMLActivities.addActivities(new MLActivities("FeaturesStock", codif, "start", 0, 0, true));

        periodicity.forEach(p -> subprocessRF(mls, fsL, p, merge));

        return mls;
    }

    public MLStocks processRFRef(String codif, MLStocks mls, boolean merge, PredictionPeriodicity p) {

        CacheMLActivities.addActivities(new MLActivities("FeaturesStock", codif, "start", 0, 0, false));
        List<FeaturesStock> fsL = FeaturesStock.create(codif, getValidator(mls,p), CacheMLStock.RANGE_MAX);
        CacheMLActivities.addActivities(new MLActivities("FeaturesStock", codif, "start", 0, 0, true));
        subprocessRF(mls, fsL, p, merge);
        return mls;
    }


    public MLStocks subprocessRF(MLStocks mls, List<FeaturesStock> fsL, PredictionPeriodicity period, boolean merge) {


        if (null == fsL) return null;

        int born = fsL.size() - CacheMLStock.RENDERING;

        List<FeaturesStock> fsLTrain = fsL.subList(0, born);
        List<FeaturesStock> fsLTest = fsL.subList(born, fsL.size());

        JavaSparkContext sc = CacheMLStock.getJavaSparkContext();

        // Load and parse the data file.
        JavaRDD<LabeledPoint> trainingData = createRDD(sc, fsLTrain, period);

        JavaRDD<FeaturesStock> testData = sc.parallelize(fsLTest);


        // Train a RandomForest model.
        Model model = trainModel(trainingData, getValidator(mls, period));



        setModel(mls, period, model);

        getValidator(mls,period).setVectorSize(fsL.get(0).currentVectorPos);
        try {

        JavaRDD<FeaturesStock> predictionAndLabel = testData.map(
            (Function<FeaturesStock, FeaturesStock>) fs -> {

                double pred = predict(Vectors.dense(fs.vectorize()), model);
                FeaturesStock fsResult = new FeaturesStock(fs, pred, period);

                fsResult.setPredictionValue(pred, period);
                fsResult.setDate(fs.getDate(period), period);

                return fsResult;
            }
        );


        JavaRDD<MLPerformances> res =
            predictionAndLabel.map((Function<FeaturesStock, MLPerformances>) pl -> {
                System.out.println("estimate: " + pl.getPredictionValue(period));
                System.out.println("result: " + pl.getResultValue(period));
                //Double diff = pl.getPredictionValue() - pl.getResultValue();
                MLPerformances perf = new MLPerformances(pl.getCurrentDate());
                perf.setMl(MLPerformance.calculYields(pl.getDate(period), pl.getPredictionValue(period), pl.getResultValue(period), pl.getCurrentValue()), period);

                return perf;

            });



            /* merge for optimize model only else replace*/
            if (!merge) getStatus(mls).setPerfList(res.collect(), period);
            else getStatus(mls).mergeList(res.collect(), period);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mls;

    }

    protected abstract void setModel(MLStocks mls, PredictionPeriodicity period, Model model);

    protected abstract Model trainModel(JavaRDD<LabeledPoint> trainingData, MatrixValidator validator);

    protected abstract double predict(MLStocks mls, PredictionPeriodicity period, Vector vector);

    protected abstract double predict(Vector vector, Model model);

    protected abstract MatrixValidator getValidator(MLStocks mls, PredictionPeriodicity period);

    protected abstract MLStatus getStatus(MLStocks mls);


    public MLStocks processRFResult(String codif, MLStocks mls) {

        Map<PredictionPeriodicity, List<FeaturesStock>> map = new HashMap<>();

        periodicity.forEach(p -> {
            List<FeaturesStock> fsL = FeaturesStock.create(codif, getValidator(mls,p), CacheMLStock.RENDERING);
            if (fsL.get(0).currentVectorPos != getValidator(mls,p).getVectorSize()) {
                log.error("size vector not corresponding");
                log.error("validator: " + getValidator(mls,PredictionPeriodicity.D1).getVectorSize());
                log.error("vector: " + fsL.get(0).currentVectorPos);
            }
            map.put(p, fsL);

        });


        if (map.isEmpty()) return null;
        List<FeaturesStock> fsLD1 = map.get(PredictionPeriodicity.D1);
        List<FeaturesStock> fsLD5 = map.get(PredictionPeriodicity.D5);
        List<FeaturesStock> fsLD20 = map.get(PredictionPeriodicity.D20);
        List<FeaturesStock> fsLD40 = map.get(PredictionPeriodicity.D40);


        // Split the data into training and test sets (30% held out for testing)

        List<FeaturesStock> resFSList = new ArrayList<>();

        for (int i = 0; i < fsLD1.size(); i++) {
            double pred = 0;
            try {
                FeaturesStock fsD1 = fsLD1.get(i);
                pred = predict(mls, PredictionPeriodicity.D1, Vectors.dense(fsD1.vectorize()));
            } catch (Exception e) {
                System.out.print(e.toString());
            }


            FeaturesStock fsResult = new FeaturesStock(fsLD1.get(i), pred, PredictionPeriodicity.D1);

            FeaturesStock fsD5 = fsLD5.get(i);
            pred = mls.getModel(PredictionPeriodicity.D5,ModelType.RANDOMFOREST).predict(Vectors.dense(fsD5.vectorize()));
            fsResult.setPredictionValue(pred, PredictionPeriodicity.D5);
            fsResult.setDate(fsD5.getDate(PredictionPeriodicity.D5), PredictionPeriodicity.D5);


            FeaturesStock fsD20 = fsLD20.get(i);
            pred = mls.getModel(PredictionPeriodicity.D20,ModelType.RANDOMFOREST).predict(Vectors.dense(fsD20.vectorize()));
            fsResult.setPredictionValue(pred, PredictionPeriodicity.D20);
            fsResult.setDate(fsD20.getDate(PredictionPeriodicity.D20), PredictionPeriodicity.D20);

            FeaturesStock fsD40 = fsLD40.get(i);
            pred = mls.getModel(PredictionPeriodicity.D40,ModelType.RANDOMFOREST).predict(Vectors.dense(fsD40.vectorize()));
            fsResult.setPredictionValue(pred, PredictionPeriodicity.D40);
            fsResult.setDate(fsD40.getDate(PredictionPeriodicity.D40), PredictionPeriodicity.D40);

            resFSList.add(fsResult);
        }


        List<MLPerformances> resList = new ArrayList<>();
        for (FeaturesStock pl : resFSList) {
            System.out.println("estimate: " + pl.getPredictionValue(PredictionPeriodicity.D1));
            System.out.println("result: " + pl.getResultValue(PredictionPeriodicity.D1));
            //Double diff = pl.getPredictionValue() - pl.getResultValue();
            MLPerformances perf = new MLPerformances(pl.getCurrentDate());

            perf.setMl(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D1), pl.getPredictionValue(PredictionPeriodicity.D1), pl.getResultValue(PredictionPeriodicity.D1), pl.getCurrentValue()), PredictionPeriodicity.D1);

            if (pl.getResultValue(PredictionPeriodicity.D5) != 0)
                perf.setMl(MLPerformance.calculYields(pl.getDate(PredictionPeriodicity.D5), pl.getPredictionValue(PredictionPeriodicity.D5), pl.getResultValue(PredictionPeriodicity.D5), pl.getCurrentValue()), PredictionPeriodicity.D5);
            else
                perf.setMl(new MLPerformance(pl.getDate(PredictionPeriodicity.D5), pl.getPredictionValue(PredictionPeriodicity.D5), -1, pl.getCurrentValue(), 0, 0, true), PredictionPeriodicity.D5);


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


        getStatus(mls).setPerfList(resList);

        return mls;

    }


}

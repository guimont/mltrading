package com.mltrading.ml.model;

import com.mltrading.ml.*;
import com.mltrading.models.util.MLActivities;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import org.apache.spark.mllib.tree.model.TreeEnsembleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Serializable;

import java.util.Arrays;
import java.util.List;


/**
 * Created by gmo on 10/03/2017.
 */
public abstract class MlModelGeneric<R extends TreeEnsembleModel> implements Serializable {


    private static final Logger log = LoggerFactory.getLogger(MlModelGeneric.class);

    private static final List<PredictionPeriodicity> periodicity = Arrays.asList(PredictionPeriodicity.D1, PredictionPeriodicity.D5, PredictionPeriodicity.D20, PredictionPeriodicity.D40);



    public JavaRDD<LabeledPoint> createRDD(JavaSparkContext sc,  List<FeaturesStock> fsL, PredictionPeriodicity type) {

        JavaRDD<FeaturesStock> data = sc.parallelize(fsL);

        JavaRDD<LabeledPoint> parsedData = data.map(
            (Function<FeaturesStock, LabeledPoint>) fs -> new LabeledPoint(fs.getResultValue(type), Vectors.dense(fs.vectorize()))

        );



        return parsedData;
    }

    public MLStocks processRFRef(String codif, MLStocks mls, boolean merge) {

        CacheMLActivities.addActivities(new MLActivities("FeaturesStock", codif, "start", 0, 0, false));
        List<FeaturesStock> fsL = FeaturesStock.create(codif, mls.getValidator(PredictionPeriodicity.D1), CacheMLStock.RANGE_MAX);
        CacheMLActivities.addActivities(new MLActivities("FeaturesStock", codif, "start", 0, 0, true));

        periodicity.forEach(p -> subprocessRF( mls,  fsL, p, merge));

        return mls;
    }


    public MLStocks subprocessRF(MLStocks mls,  List<FeaturesStock> fsL, PredictionPeriodicity period, boolean merge) {


        if (null == fsL) return null;

        int born = fsL.size() - CacheMLStock.RENDERING;

        List<FeaturesStock> fsLTrain =fsL.subList(0,born);
        List<FeaturesStock> fsLTest =fsL.subList(born, fsL.size());

        JavaSparkContext sc = CacheMLStock.getJavaSparkContext();

        // Load and parse the data file.
        JavaRDD<LabeledPoint> trainingData = createRDD(sc, fsLTrain, period);

        JavaRDD<FeaturesStock> testData = sc.parallelize(fsLTest);


        // Train a RandomForest model.
        final R model = trainModel(trainingData, mls.getValidator(period));


       setModel(mls, period, model);


        mls.getValidator(period).setVectorSize(fsL.get(0).currentVectorPos);


        JavaRDD<FeaturesStock> predictionAndLabel = testData.map(
            (Function<FeaturesStock, FeaturesStock>) fs -> {

                double pred = model.predict(Vectors.dense(fs.vectorize()));
                FeaturesStock fsResult = new FeaturesStock(fs, pred, period);

                fsResult.setPredictionValue(pred,period);
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


        try {
            /* merge for optimize model only else replace*/
            if (!merge) mls.getStatus().setPerfList(res.collect(),period);
            else mls.getStatus().mergeList(res.collect(),period);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mls;

    }

    protected abstract void setModel(MLStocks mls, PredictionPeriodicity period, R model);

    protected abstract R trainModel(JavaRDD<LabeledPoint> trainingData, MatrixValidator validator);


}

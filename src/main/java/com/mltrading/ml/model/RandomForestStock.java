package com.mltrading.ml.model;

import java.util.*;


import com.mltrading.ml.*;
import org.apache.spark.mllib.linalg.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.spark.api.java.JavaRDD;

import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;

/**
 * Created by gmo on 14/11/2015.
 */
public class RandomForestStock extends MlModelGeneric<RandomForestModel> {

    private static final Logger log = LoggerFactory.getLogger(RandomForestStock.class);

    private static final List<PredictionPeriodicity> periodicity = Arrays.asList(PredictionPeriodicity.D1, PredictionPeriodicity.D5, PredictionPeriodicity.D20, PredictionPeriodicity.D40);



    @Override
    protected void setModel(MLStocks mls, PredictionPeriodicity period, RandomForestModel model) {
        mls.setModel(period, model);
    }


    @Override
    protected RandomForestModel trainModel(JavaRDD<LabeledPoint> trainingData, MatrixValidator validator) {
        // Train a RandomForest model.
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();

        String impurity = "variance";

        String featureSubsetStrategy = "auto"; // Let the algorithm choose.
        final RandomForestModel model = RandomForest.trainRegressor(trainingData,
            categoricalFeaturesInfo, validator.getNumTrees(), featureSubsetStrategy, impurity,
            validator.getMaxDepth(), validator.getMaxBins(),
            validator.getSeed());

        return model;
    }





    public MLStocks processRFResult(String codif, MLStocks mls) {

        Map<PredictionPeriodicity,List<FeaturesStock>> map = new HashMap<>();

        periodicity.forEach(p -> {
            List<FeaturesStock> fsL = FeaturesStock.create(codif, mls.getValidator(p), CacheMLStock.RENDERING);
            if( fsL.get(0).currentVectorPos != mls.getValidator(p).getVectorSize())    {
                log.error("size vector not corresponding");
                log.error("validator: " + mls.getValidator(PredictionPeriodicity.D1).getVectorSize());
                log.error("vector: " + fsL.get(0).currentVectorPos );
            }
            map.put(p,fsL);

            });


        if (map.isEmpty()) return null;
        List<FeaturesStock> fsLD1 = map.get(PredictionPeriodicity.D1);
        List<FeaturesStock> fsLD5 = map.get(PredictionPeriodicity.D5);
        List<FeaturesStock> fsLD20 = map.get(PredictionPeriodicity.D20);
        List<FeaturesStock> fsLD40 = map.get(PredictionPeriodicity.D40);


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

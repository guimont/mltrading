package com.mltrading.ml.model;

import com.mltrading.ml.*;
import com.mltrading.models.util.MLActivities;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.GradientBoostedTrees;

import org.apache.spark.mllib.tree.configuration.BoostingStrategy;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gmo on 14/11/2015.
 */
public class GradiantBoostStock extends MlModelGeneric<GradientBoostedTreesModel> {

    private static final Logger log = LoggerFactory.getLogger(GradiantBoostStock.class);


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

    @Override
    protected void setModel(MLStocks mls, PredictionPeriodicity period, GradientBoostedTreesModel model) {
        //mls.setModel(period, model);
    }

    @Override
    protected GradientBoostedTreesModel trainModel(JavaRDD<LabeledPoint> trainingData, MatrixValidator validator) {
        BoostingStrategy boostingStrategy = BoostingStrategy.defaultParams("Regression");
        boostingStrategy.setNumIterations(3); // Note: Use more iterations in practice.
        boostingStrategy.getTreeStrategy().setMaxDepth(5);
// Empty categoricalFeaturesInfo indicates all features are continuous.

        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
        boostingStrategy.treeStrategy().setCategoricalFeaturesInfo(categoricalFeaturesInfo);

        final GradientBoostedTreesModel model =
            GradientBoostedTrees.train(trainingData, boostingStrategy);

        return model;
    }





    public MLStocks processRFResult(String codif, MLStocks mls) {

        List<FeaturesStock> fsLD1 = FeaturesStock.create(codif, mls.getValidator(PredictionPeriodicity.D1), CacheMLStock.RENDERING);
        if( fsLD1.get(0).currentVectorPos != mls.getValidator(PredictionPeriodicity.D1).getVectorSize())    {
            log.error("size vector not corresponding");
            log.error("validator: " + mls.getValidator(PredictionPeriodicity.D1).getVectorSize());
            log.error("vector: " + fsLD1.get(0).currentVectorPos );
        }
        List<FeaturesStock> fsLD5 = FeaturesStock.create(codif, mls.getValidator(PredictionPeriodicity.D5), CacheMLStock.RENDERING);
        if( fsLD5.get(0).currentVectorPos != mls.getValidator(PredictionPeriodicity.D5).getVectorSize())    {
            log.error("size vector not corresponding");
            log.error("validator: " + mls.getValidator(PredictionPeriodicity.D5).getVectorSize());
            log.error("vector: " + fsLD5.get(0).currentVectorPos );
        }
        List<FeaturesStock> fsLD20 = FeaturesStock.create(codif, mls.getValidator(PredictionPeriodicity.D20), CacheMLStock.RENDERING);
        if( fsLD20.get(0).currentVectorPos != mls.getValidator(PredictionPeriodicity.D20).getVectorSize())    {
            log.error("size vector not corresponding");
            log.error("validator: " + mls.getValidator(PredictionPeriodicity.D20).getVectorSize());
            log.error("vector: " + fsLD20.get(0).currentVectorPos );
        }

        List<FeaturesStock> fsLD40 = FeaturesStock.create(codif, mls.getValidator(PredictionPeriodicity.D40), CacheMLStock.RENDERING);
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

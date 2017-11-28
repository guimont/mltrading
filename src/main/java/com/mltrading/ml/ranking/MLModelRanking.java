package com.mltrading.ml.ranking;

import com.mltrading.ml.*;

import com.mltrading.models.stock.StockGeneral;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.model.TreeEnsembleModel;
import scala.Serializable;

import java.util.List;

public abstract class MLModelRanking<R extends TreeEnsembleModel> implements Serializable {

    public JavaRDD<LabeledPoint> createRDD(JavaSparkContext sc, List<FeaturesRank> fsL, PredictionPeriodicity periodicity) {

        JavaRDD<FeaturesRank> data = sc.parallelize(fsL);

        /*List<LabeledPoint> test = new ArrayList<>();

        fsL.forEach(fs -> test.add(new LabeledPoint(fs.getResultValue(periodicity), Vectors.dense(fs.vectorize()))));
*/
        JavaRDD<LabeledPoint> parsedData = data.map(
            (Function<FeaturesRank, LabeledPoint>) fs -> new LabeledPoint(fs.getResultValue(periodicity), Vectors.dense(fs.vectorize()))
        );

        return parsedData;
    }


    public MLRank processRanking(List<StockGeneral> l, MLRank mlr) {


        List<FeaturesRank> fsLTrain  = FeaturesRank.create(l, MLStockRanking.RANGE_MAX, MLStockRanking.RENDERING);
        List<FeaturesRank> fsLTest = FeaturesRank.create(l, MLStockRanking.RENDERING, MLStockRanking.NOT_DEFINE);

        if (null == fsLTrain || null == fsLTest) return null;

        JavaSparkContext sc = CacheMLStock.getJavaSparkContext();

        // Load and parse the data file.
        JavaRDD<LabeledPoint> trainingData = createRDD(sc, fsLTrain, PredictionPeriodicity.D20);
        JavaRDD<FeaturesRank> testData = sc.parallelize(fsLTest);


        // Train a RandomForest model.
        final R model = trainModel(trainingData);

        setModel(mlr,model);

        JavaRDD<FeaturesRank> predictionAndLabel = testData.map(
            (Function<FeaturesRank, FeaturesRank>) fs -> {

                double pred = model.predict(Vectors.dense(fs.vectorize()));
                FeaturesRank fsResult = new FeaturesRank(fs, pred, PredictionPeriodicity.D20);

                /*already done in constructor*/
                //fsResult.setPredictionValue(pred, PredictionPeriodicity.D20);
                //fsResult.setDate(fs.getDate(PredictionPeriodicity.D20), PredictionPeriodicity.D20);

                return fsResult;
            }
        );

        JavaRDD<MLPerformances> res =
            predictionAndLabel.map((Function<FeaturesRank, MLPerformances>) pl -> {
                System.out.println("estimate: " + pl.getPredictionValue(PredictionPeriodicity.D20));
                System.out.println("result: " + pl.getResultValue(PredictionPeriodicity.D20));
                //Double diff = pl.getPredictionValue() - pl.getResultValue();
                MLPerformances perf = new MLPerformances(pl.getCurrentDate());
                perf.setCodif(pl.getCodif());
                perf.setMl(MLPerformance.calculOnlyYields(pl.getDate(PredictionPeriodicity.D20), pl.getPredictionValue(PredictionPeriodicity.D20), pl.getResultValue(PredictionPeriodicity.D20)), PredictionPeriodicity.D20);

                return perf;

            });


        try {
            mlr.getStatus().setPerfList(res.collect(), PredictionPeriodicity.D20);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mlr;
    }


    protected abstract R trainModel(JavaRDD<LabeledPoint> trainingData);

    protected abstract void setModel(MLRank mlr, R model);

    protected abstract double predict(MLRank mls, Vector vector);


    public MLRank processRFResult(String codif, MLRank mls) {

        return mls;
    }


}

package com.mltrading.ml.ranking;



/* create rank for stock*/
/*for each stock => predict ranking */

import com.mltrading.ml.*;
import com.mltrading.genetic.GeneticAlgorithm;
import com.mltrading.ml.model.ModelType;
import com.mltrading.ml.util.Ensemble;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class MLStockRanking {


    public static final int RANGE_MAX = 200;
    public static final int RENDERING = 70;
    public static final int NOT_DEFINE = 20;

    public void optimize() {

        //MLRank ref = CacheMLStock.getMlRankCache();
        CacheMLStock.modelTypes.forEach( t -> {

            List<StockGeneral> l = Stream.concat(CacheStockGeneral.getIsinCache().values().stream(), CacheStockGeneral.getIsinExCache().values().stream())
                .collect(Collectors.toList());


            MLModelRanking rfr;

            if (t == ModelType.RANDOMFOREST)
                rfr = new RandomForestRanking();
            else
                rfr = new GradiantBoostRanking();


            MLRank mlr = new MLRank();
            rfr.processRanking(l, mlr);
            mlr.getStatus(t).calculeAvgPrd();


            /**
             * on ne sauve pas les perfs donc on ne peut pas comparer les modeles .. cela a un sens si on cherche a ameliorer le model
             */
            /*if (ref != null) {
                if (compareResult(mlr.getStatus(t), ref.getStatus(t), PredictionPeriodicity.D20)) {
                    CacheMLStock.setMlRank(mlr);
                }
            } else {*/
                CacheMLStock.setMlRank(mlr);
            //}

            mlr.deleteModel();
            mlr.saveModel(t);
            mlr.saveModelDB(t);
        });

        updateEsembleRanking();

    }


    public static void updateEsembleRanking() {

        MLRank m = CacheMLStock.getMlRankCache();

        List<MLPerformances> mlPerformancesRF = m.getStatus(ModelType.RANDOMFOREST).getPerfList();
        List<MLPerformances> mlPerformancesGBT = m.getStatus(ModelType.GRADIANTBOOSTTREE).getPerfList();

        if ((((mlPerformancesRF == null) || (mlPerformancesGBT == null))
            || (((mlPerformancesRF != null) && (mlPerformancesRF.size() == 0)) || ((mlPerformancesGBT != null) && (mlPerformancesGBT.size() == 0)))))
            return;

        final GeneticAlgorithm<Ensemble> algo = new GeneticAlgorithm<>(e -> e.evaluate(m,null), Ensemble::newInstance,
            Ensemble::merge, Ensemble::mutate);

        algo.initialize(20);

        /*if model is empty*/
        algo.iterate(100, 2, 10, 0, 4);

        {
            m.ratio = algo.best().getRatio();

            int size = mlPerformancesRF.size();

            List<MLPerformances> listEnsemble = new ArrayList<>();

            for (int index = 0; index < size; index++) {

                MLPerformances mlRF = mlPerformancesRF.get(index);
                MLPerformances mlGBT = mlPerformancesGBT.get(index);
                MLPerformances perf = new MLPerformances(mlRF.getDate());
                PeriodicityList.periodicityLong.forEach(p -> {
                    MLPerformance mpRF = mlRF.getMl(p);
                    MLPerformance mpGBT = mlGBT.getMl(p);


                    if (mpRF.getRealvalue() == -1)
                        perf.setMl(new MLPerformance(mlRF.getMl(p).getDate(), mlRF.getMl(p).getCurrentDate(), (mpRF.getPrediction() * m.getRatio() + mpGBT.getPrediction()) / (1. + m.ratio), -1, mpRF.getCurrentValue(), 0, 0, true), p);

                    else

                        perf.setMl(MLPerformance.calculYields(mlRF.getMl(p).getDate(), mlRF.getMl(p).getCurrentDate(),
                            (mpRF.getPrediction() * m.getRatio() + mpGBT.getPrediction()) / (1. + m.ratio),
                            mpRF.getRealvalue(), mpRF.getCurrentValue()), p);


                });
                listEnsemble.add(perf);


            }

            m.getStatus(ModelType.ENSEMBLE).setPerfList(listEnsemble);
            m.getStatus(ModelType.ENSEMBLE).calculeAvgPrd();

        }
    }







    /**
     * compare result
     *
     * @param mls
     * @param ref
     * @param period
     * @return
     */
    public static  boolean compareResult(MLStatus mls, MLStatus ref, PredictionPeriodicity period) {
        return mls.getErrorRate(period) <= ref.getErrorRate(period) ||
            (mls.getErrorRate(period) == ref.getErrorRate(period) &&
                mls.getAvg(period) < ref.getAvg(period));
    }

}

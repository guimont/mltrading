package com.mltrading.ml;

import com.mltrading.config.MLProperties;
import com.mltrading.ml.genetic.GeneticAlgorithm;
import com.mltrading.ml.model.RandomForestStock;
import com.mltrading.ml.util.Combination;
import com.mltrading.ml.util.Evaluate;
import com.mltrading.ml.util.FixedThreadPoolExecutor;
import com.mltrading.models.stock.*;

import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockSector;
import com.mltrading.models.util.MLActivities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

/**
 * Created by gmo on 08/01/2016.
 */
@Service
public class MlForecast extends Evaluate{

    public static final List<PredictionPeriodicity> periodicity = Arrays.asList(PredictionPeriodicity.D1, PredictionPeriodicity.D5, PredictionPeriodicity.D20, PredictionPeriodicity.D40);


    private static final Logger log = LoggerFactory.getLogger(MlForecast.class);
    private ExecutorService executorRef;
    private static int DEFAULT_NB_THREADS = 2;

    public void processList() {

        List<StockGeneral> l = new ArrayList(CacheStockGeneral.getIsinCache().values());

        for (StockGeneral s : l) {
            RandomForestStock rfs = new RandomForestStock();
            MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
            if (mls != null) {
                mls = rfs.processRFResult(s.getCodif(), mls);
                mls.getStatus().calculeAvgPrd();
            }
        }


        List<StockSector> ls = new ArrayList(CacheStockSector.getSectorCache().values());

        for (StockSector s : ls) {
            RandomForestStock rfs = new RandomForestStock();
            MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
            if (mls != null) {
                mls = rfs.processRFResult(s.getCodif(), mls);
                mls.getStatus().calculeAvgPrd();
            }
        }


        //check

        log.info("result mlf size: " + CacheMLStock.getMLStockCache().size());

        for (MLStocks mls : CacheMLStock.getMLStockCache().values()) {
            log.info("perf list result size: " + mls.getStatus().getPerfList().size());
        }

    }

    public MlForecast() {

        int nbThread = MLProperties.getProperty("optimize.threads", DEFAULT_NB_THREADS);

        this.executorRef = new FixedThreadPoolExecutor(nbThread,
            "ExtractionRefThreadPool");
    }

    public static enum Method {
        RandomForest,
        LinearRegression
    }

    public static enum Type {
        Feature,
        None, RF
    }


    /**
     * optimization for matrix validator
     * @param loop: indicate loop for each saveValidator model
     * @param backloop: iteration before saveValidator
     * @param validator: validator model
     * @param target: PX1 or sector only
     */
    public void optimize(int loop, int backloop, String validator, String target) {

        if (CacheMLActivities.setIsRunning() == false) {
            log.error("optimizing still running, cannot launch new optimization");
            return;
        }

        for (int i = 0; i < loop; i++) {
            CacheMLStock.getMLStockCache().values()
                .forEach(m ->
                {
                    if (m!=null)
                        m.resetScoring();
                });
            if (target.equals("PX1"))
                optimize(CacheStockGeneral.getIsinCache().values().stream(), CacheStockGeneral.getIsinCache().values().size(), backloop, validator, Method.RandomForest);
            else
                optimize(CacheStockSector.getSectorCache().values().stream(), CacheStockSector.getSectorCache().values().size(), backloop, validator, Method.RandomForest);

            updatePredictor();


            CacheMLActivities.addActivities(new MLActivities("saveValidator forecast model", "", "start", 0, 0, false));
            log.info("saveML");
            CacheMLStock.save();
            CacheMLActivities.addActivities(new MLActivities("saveValidator forecast model", "", "end", 0, 0, true));
        }

        CacheMLActivities.endRunning();
        //evaluate();

    }


    /**
     * sub function to iterate and use threadpool
     * @param stream
     * @param size
     * @param backloop
     * @param validator
     * @param method
     */
    private void optimize(Stream<? extends StockHistory> stream, int size, int backloop, String validator, Method method) {

        final CountDownLatch latches = new CountDownLatch(size);
        //final CountDownLatch latches = new CountDownLatch(1); //testmode ORA
        CacheMLActivities.setCountGlobal(latches.getCount());

        CacheMLActivities.addActivities(new MLActivities("optimize forecast", "", "start", 0, 0, false));

        //For test purpose only
        stream/*.filter(s -> s.getCodif().equals("ORA"))*/.forEach(s -> executorRef.submit(() -> {    //For test purpose only
            try {
                if (validator.contains("optimizeModel"))
                    optimizeModel(s.getCodif());
                else if (validator.contains("optimizeGenetic"))
                    optimizeGenetic(s.getCodif(),backloop);
                else
                    optimize(s.getCodif(), backloop, method, validator);
            } finally {
                {
                    latches.countDown();
                    CacheMLActivities.setCountGlobal(latches.getCount());
                }
            }
        }));

        // Wait for completion
        try {
            latches.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        CacheMLActivities.addActivities(new MLActivities("optimize forecast", "", "end", 0, 0, true));


    }


    /**
     * processing machine learning algorithm
     * @param codif name
     * @param loop sub iteration
     * @param method not use
     * @param validator  validator model
     */
    private void optimize(String codif, int loop, Method method, String validator) {

        /**
         * to have some results faster, 2 iterations loop
         */

        for (int i = 0; i < loop; i++) {

            final MLStocks mls = new MLStocks(codif);

            CacheMLActivities.addActivities(new MLActivities("optimize", codif, "start", loop, 0, false));
            MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());

            int rowSector = getRowSector(codif);


            /**
             * merge validator for optimizeEconomical
             */
            if (validator.contains("optimizeEconomicalModel")) {
                mls.generateValidator("generateEconomicalModel",rowSector);
                if (ref != null) mls.mergeValidator(ref);
            } else {
                mls.generateValidator(validator,rowSector);
            }


            String saveCode;
            {
                RandomForestStock rfs = new RandomForestStock();
                rfs.processRFRef(codif, mls, false);
                saveCode = "V";
            }

            if (null != mls) {
                mls.getStatus().calculeAvgPrd();

                periodicity.forEach(p -> mls.getValidator(p).save(mls.getCodif() + saveCode +
                    p, mls.getStatus().getErrorRate(p), mls.getStatus().getAvg(p)));



                if (ref != null) {

                    periodicity.forEach(p -> {
                        if (checkResult(mls, ref, p))
                            CacheMLActivities.addActivities(new MLActivities("optimize", codif, "increase model: " + p, loop, 1, true));
                        else
                            CacheMLActivities.addActivities(new MLActivities("optimize", codif, "not increase model: " + p, loop, 0, true));
                    });


                } else {
                    /* empty ref so improve scoring yes*/
                    mls.setScoring(true);
                    CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                    CacheMLActivities.addActivities(new MLActivities("optimize", codif, "empty model", loop, 0, true));
                }
            } else {
                CacheMLActivities.addActivities(new MLActivities("optimize", codif, "failed", loop, 0, true));
            }
        }

    }


    /**
     * Genetic optimization
     * @param codif
     * @param loop
     */
    void optimizeGenetic(String codif,int loop) {


        for (PredictionPeriodicity p : periodicity) {
        //periodicity.forEach(p -> {
            log.info("start loop for period: " + p);
            final GeneticAlgorithm<Combination> algo = new GeneticAlgorithm<Combination>(c -> c.evaluate(codif,p), () -> Combination.newInstance(),
                (first, second) -> first.merge(second), c -> c.mutate());
            algo.initialize(8);

            /*if model is empty*/
            if (CacheMLStock.getMLStockCache().get(codif) != null)
                algo.addReference(new Combination(CacheMLStock.getMLStockCache().get(codif).getValidator(p).clone()));
            algo.iterate(loop, 2, 6, 4, 0);
            //algo.printTo(System.err);


            /*Validate result */
            final MLStocks mls = new MLStocks(codif);
            MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());
            mls.setValidator(p,algo.best().getMv());


            RandomForestStock rfs = new RandomForestStock();
            rfs.processRFRef(codif, mls, false, p);
            mls.getStatus().calculeAvgPrd();

            if (ref != null) {

                if ( MlForecast.checkResult(mls, ref, p))
                    CacheMLActivities.addActivities(new MLActivities("optimize", codif, "increase model: " + p, 0, 1, true));
                else
                    CacheMLActivities.addActivities(new MLActivities("optimize", codif, "not increase model: " + p, 0, 0, true));
            } else {
                    /* empty ref so improve scoring yes*/
                mls.setScoring(true);
                CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                CacheMLActivities.addActivities(new MLActivities("optimize", codif, "empty model", 0, 0, true));
            }

            System.err.println(algo.best().toString());
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

    /**
     * compare result and replace it if better
     *
     * @param mls
     * @param ref
     * @param period
     * @return
     */
    public static  boolean checkResult(MLStocks mls, MLStocks ref, PredictionPeriodicity period) {
        if (compareResult(mls.getStatus(), ref.getStatus(), period)) {
            ref.replace(period, mls);
            ref.getStatus().setAvg(mls.getStatus().getAvgD1(), period);
            ref.getStatus().setErrorRate(mls.getStatus().getErrorRate(period), period);
            ref.getSock(period).setModelImprove(true);
            try {
                ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), period);
            } catch (Exception e) {
                log.error(e.toString());
            } finally {
                return true;
            }

        }

        return false;
    }




    /**
     * optimize model with processing validator sample
     *
     * @param s
     */
    static int NOTUSE = 0;
    private void optimizeModel(String codif) {

        /**
         * to have some results faster, 2 iterations loop
         */

        MLStocks ref = null;

        MLStocks validatorModel = new MLStocks(codif);
        validatorModel.generateValidator("generateSimpleModel",NOTUSE);

        MLStocks mls = new MLStocks(codif);
        mls.generateValidator("generateSimpleModel",NOTUSE);

        while (mls.randomizeModel()) {

            RandomForestStock rfs = new RandomForestStock();
            mls = rfs.processRFRef(codif, mls, true);

            final MLStocks finalMls = mls;

            String saveCode = "V";


            if (null != finalMls) {
                finalMls.getStatus().calculeAvgPrd();

                periodicity.forEach(p -> finalMls.getValidator(p).save(finalMls.getCodif() + saveCode +
                    p, finalMls.getStatus().getErrorRate(p), finalMls.getStatus().getAvg(p)));

                /* period have no importance, iteration are same for all period*/
                int col = mls.getValidator(PredictionPeriodicity.D1).getCol() -1;

                if (ref != null) {

                    final MLStocks finalRef = ref;
                    periodicity.forEach(p -> {
                        if (compareResult(finalMls.getStatus(), finalRef.getStatus(), p))
                            validatorModel.getValidator(p).validate(col);
                    });

                } else {
                    ref = finalMls.clone();
                }

                mls = new MLStocks(codif);
                mls.generateValidator("generateSimpleModel",NOTUSE);
                mls.updateColValidator(col+1);

            }
        }

        /** keep best model*/
        RandomForestStock rfs = new RandomForestStock();
        MLStocks bestModel = rfs.processRFRef(codif, validatorModel, true);
        bestModel.getStatus().calculeAvgPrd();

        final MLStocks mlsCache = CacheMLStock.getMLStockCache().get(codif);
        periodicity.forEach(p -> {
            if (compareResult(mlsCache.getStatus(), bestModel.getStatus(), p))
                CacheMLStock.getMLStockCache().put(codif, bestModel);
        });




    }


    /**
     *
     */
    public static void updatePerf() {
        CacheStockGeneral.getCache().values().forEach(com.mltrading.ml.MlForecast::updatePredictor);

        CacheStockSector.getSectorCache().values().forEach(com.mltrading.ml.MlForecast::updatePredictor);
    }


    /**
     *
     */
    public static void updatePredictor() {
        CacheStockGeneral.getCache().values().forEach(com.mltrading.ml.MlForecast::updatePredictor);

        CacheStockSector.getSectorCache().values().forEach(com.mltrading.ml.MlForecast::updatePredictor);
    }

    private static void updatePredictor(StockHistory sg) {
        MLPredictor predictor = new MLPredictor();

        StockPrediction p = predictor.prediction(sg.getCodif());

        if (p != null) {
            sg.setPrediction(p);
            double yield20 = (p.getPredictionD20() - sg.getValue()) / sg.getValue();
            double yield5 = Math.abs((p.getPredictionD5() - sg.getValue()) / sg.getValue());
            double consensus = 1;//StockHistory.getStockHistoryLast(sg.getCodif(), 1).get(0).getConsensusNote();
            sg.setPerformanceEstimate(p.getConfidenceD20() / 20 * yield20 * p.getConfidenceD5() / 10 * yield5
                * consensus);

            double essai = 0;
        } else {
            sg.setPerformanceEstimate(0.);
        }
    }

}

package com.mltrading.ml;

import com.mltrading.ml.util.FixedThreadPoolExecutor;
import com.mltrading.models.stock.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Created by gmo on 08/01/2016.
 */
@Service
public class MlForecast {

    private static final Logger log = LoggerFactory.getLogger(MlForecast.class);
    private ExecutorService executorRefRF;
    private ExecutorService executorRef;
    private static int DEFAULT_NB_THREADS_RF = 2;
    private static int DEFAULT_NB_THREADS = 2;

    public void processList(List<StockGeneral> l) {

        for (StockGeneral s : l) {
            RandomForestStock rfs = new RandomForestStock();
            MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
            if (mls != null) {
                mls = rfs.processRFResult(s, mls);
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
        this.executorRefRF = new FixedThreadPoolExecutor(DEFAULT_NB_THREADS_RF,
            "ExtractionRefThreadPool");

        this.executorRef = new FixedThreadPoolExecutor(DEFAULT_NB_THREADS,
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
     * optimize by model randomization
     */
    public void optimize() {

        final CountDownLatch latches = new CountDownLatch(CacheStockGeneral.getIsinCache().values().size());
        //final CountDownLatch latches = new CountDownLatch(1); //testmode ORA

        //For test purpose only
        CacheStockGeneral.getIsinCache().values().stream()/*.filter(s -> s.getRealCodif().equals("ORA"))*/.forEach(s -> executorRef.submit(() -> {    //For test purpose only
            try {
                optimize(s, 1, 1, Method.RandomForest, Type.Feature);
            } finally {
                {
                    latches.countDown();
                }
            }
        }));

        // Wait for completion
        try {
            latches.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        //evaluate();
        updatePredictor();


        log.info("saveML");
        CacheMLStock.save();
    }

    /**
     * optimize model with processing validator sample
     *
     * @param s
     * @param loop
     * @param backloop
     * @param method
     * @param type
     */
    public void optimize(StockGeneral s, int loop, int backloop, Method method, Type type) {
        for (int i = 0; i < backloop; i++) {
            optimize(s, loop, method, type);
        }
    }

    private void optimize(StockGeneral s, int loop, Method method, Type type) {

        /**
         * to have some results faster, 2 iterations loop
         */

        final CountDownLatch latches = new CountDownLatch(loop);


        for (int i = 0; i < loop; i++) {
            executorRefRF.submit(() -> {
                try {
                    MLStocks mls = new MLStocks(s.getCodif());

                    if (type == Type.Feature) {
                        //mls.generateValidator("generate"); too big for test
                        mls.generateValidator("generateSimpleModel");
                    }


                    String saveCode;
                    {
                        RandomForestStock rfs = new RandomForestStock();
                        mls = rfs.processRF(s, mls);
                        saveCode = "V";
                    }

                    if (null != mls) {
                        mls.getStatus().calculeAvgPrd();

                        //TODO refactor this common code
                        mls.getValidator(PredictionPeriodicity.D1).save(mls.getCodif() + saveCode +
                            PredictionPeriodicity.D1, mls.getStatus().getErrorRateD1(), mls.getStatus().getAvgD1());
                        mls.getValidator(PredictionPeriodicity.D5).save(mls.getCodif() + saveCode +
                            PredictionPeriodicity.D5, mls.getStatus().getErrorRateD5(), mls.getStatus().getAvgD5());
                        mls.getValidator(PredictionPeriodicity.D20).save(mls.getCodif() + saveCode +
                            PredictionPeriodicity.D20, mls.getStatus().getErrorRateD20(), mls.getStatus().getAvgD20());
                        mls.getValidator(PredictionPeriodicity.D40).save(mls.getCodif() + saveCode +
                            PredictionPeriodicity.D40.toString(), mls.getStatus().getErrorRate(PredictionPeriodicity.D40), mls.getStatus().getAvg(PredictionPeriodicity.D40));
                        MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());


                        if (ref != null) {

                            checkResult(mls,ref,PredictionPeriodicity.D1);
                            checkResult(mls,ref,PredictionPeriodicity.D5);
                            checkResult(mls,ref,PredictionPeriodicity.D20);
                            checkResult(mls,ref,PredictionPeriodicity.D40);

                        } else {
                            CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                        }
                    }
                } finally {
                    {
                        latches.countDown();
                    }
                }

            });
        }


        // Wait for completion
        try {
            latches.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }




    }


    /**
     * compare result
     * @param mls
     * @param ref
     * @param period
     * @return
     */
    private boolean compareResult(MLStatus mls, MLStatus ref, PredictionPeriodicity period) {
        return mls.getErrorRate(period) <= ref.getErrorRate(period) ||
            (mls.getErrorRate(period) == ref.getErrorRate(period) &&
                mls.getAvg(period) < ref.getAvg(period));
    }

    /**
     * compare result and replace it if better
     * @param mls
     * @param ref
     * @param period
     * @return
     */
    private boolean checkResult(MLStocks mls, MLStocks ref, PredictionPeriodicity period) {
        if (compareResult(mls.getStatus(),ref.getStatus(),period)) {
            ref.replace(period,mls);
            ref.getStatus().setAvg(mls.getStatus().getAvgD1(), period);
            ref.getStatus().setErrorRate(mls.getStatus().getErrorRate(period), period);

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

    public void optimizeModel() {

        final CountDownLatch latches = new CountDownLatch(CacheStockGeneral.getIsinCache().values().size());

        CacheStockGeneral.getIsinCache().values().stream().filter(s -> s.getCodif().equals("ORA")).forEach(s -> executorRef.submit(() -> {    //For test purpose only
            try {
                optimizeModel(s);
            } finally {
                {
                    latches.countDown();
                }
            }
        }));
    }


    /**
     * optimize model with processing validator sample
     *
     * @param s
     */
    public void optimizeModel(StockGeneral s) {

        /**
         * to have some results faster, 2 iterations loop
         */

        MLStocks mls = new MLStocks(s.getCodif());

        mls.generateValidator("generateSimpleModel");


        while (mls.randomizeModel()) {


            RandomForestStock rfs = new RandomForestStock();
            mls = rfs.processRF(s, mls, PredictionPeriodicity.D1);
            mls = rfs.processRF(s, mls, PredictionPeriodicity.D5);
            mls = rfs.processRF(s, mls, PredictionPeriodicity.D20);
            String saveCode = "V";


            if (null != mls) {
                mls.getStatus().calculeAvgPrd();
                mls.getValidator(PredictionPeriodicity.D1).save(mls.getCodif() + saveCode + "D1", mls.getStatus().getErrorRateD1(), mls.getStatus().getAvgD1());
                mls.getValidator(PredictionPeriodicity.D5).save(mls.getCodif() + saveCode + "D5", mls.getStatus().getErrorRateD5(), mls.getStatus().getAvgD5());
                mls.getValidator(PredictionPeriodicity.D20).save(mls.getCodif() + saveCode + "D20", mls.getStatus().getErrorRateD20(), mls.getStatus().getAvgD20());
                MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());


                if (ref != null) {

                    //TODO better to iterate all model => refactor
                    if (checkResult(mls,ref,PredictionPeriodicity.D1) == false)
                        mls.getValidator(PredictionPeriodicity.D1).revertModel(mls.getValidator(PredictionPeriodicity.D1));


                    if (checkResult(mls,ref,PredictionPeriodicity.D5) == false) {
                        mls.getValidator(PredictionPeriodicity.D5).revertModel(mls.getValidator(PredictionPeriodicity.D5));
                    }


                    if (checkResult(mls,ref,PredictionPeriodicity.D20) == false){
                        mls.getValidator(PredictionPeriodicity.D20).revertModel(mls.getValidator(PredictionPeriodicity.D20));
                    }

                    if (checkResult(mls,ref,PredictionPeriodicity.D40) == false){
                        mls.getValidator(PredictionPeriodicity.D40).revertModel(mls.getValidator(PredictionPeriodicity.D40));
                    }

                } else {
                    CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                    ref = mls;
                }


                mls = mls.replaceValidator(ref);

            }


        }

        log.info("evaluate");
        //evaluate();

        updatePredictor();
        log.info("saveML");
    }



    public static void updatePredictor() {
        for (StockGeneral sg : CacheStockGeneral.getCache().values()) {
            MLPredictor predictor = new MLPredictor();

            StockPrediction p = predictor.prediction(sg);

            if (p != null)
            {
                sg.setPrediction(p);
                double yield20 =  (p.getPredictionD20()-sg.getValue()) / sg.getValue();
                double yield5 =  Math.abs((p.getPredictionD5() - sg.getValue()) / sg.getValue());
                double consensus = StockHistory.getStockHistoryLast(sg.getCodif(),1).get(0).getConsensusNote();
                sg.setPerformanceEstimate(p.getConfidenceD20()/20 * yield20 * p.getConfidenceD5() / 10 * yield5
                    * consensus);

                double essai = 0;
            }else {
                sg.setPerformanceEstimate(0.);
            }



        }
    }
}

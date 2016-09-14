package com.mltrading.ml;

import com.mltrading.ml.util.FixedThreadPoolExecutor;
import com.mltrading.models.stock.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    private static int DEFAULT_NB_THREADS = 1;

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

        //final CountDownLatch latches = new CountDownLatch(CacheStockGeneral.getIsinCache().values().size());
        final CountDownLatch latches = new CountDownLatch(1); //testmode ORA

        //For test purpose only
        CacheStockGeneral.getIsinCache().values().stream().filter(s -> s.getCodif().equals("ORA")).forEach(s -> executorRef.submit(() -> {    //For test purpose only
            try {
                optimize(s, 5, 1, Method.RandomForest, Type.Feature);
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
                        mls.generateValidator("generate");
                    }


                    String saveCode;
                   {
                        RandomForestStock rfs = new RandomForestStock();
                        mls = rfs.processRF(s, mls);
                        saveCode = "V";
                    }

                    if (null != mls) {
                        mls.getStatus().calculeAvgPrd();
                        mls.getValidator(PredictionPeriodicity.D1).save(mls.getCodif() + saveCode + "D1", mls.getStatus().getErrorRateD1(), mls.getStatus().getAvgD1());
                        mls.getValidator(PredictionPeriodicity.D5).save(mls.getCodif() + saveCode + "D5", mls.getStatus().getErrorRateD5(), mls.getStatus().getAvgD5());
                        mls.getValidator(PredictionPeriodicity.D20).save(mls.getCodif() + saveCode + "D20", mls.getStatus().getErrorRateD20(), mls.getStatus().getAvgD20());
                        MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());


                        if (ref != null) {
                            /**for 1 day prevision*/
                            if (mls.getStatus().getErrorRateD1() <= ref.getStatus().getErrorRateD1() ||
                                (mls.getStatus().getErrorRateD1() == ref.getStatus().getErrorRateD1() &&
                                    mls.getStatus().getAvgD1() < ref.getStatus().getAvgD1())) {
                                ref.replace(PredictionPeriodicity.D1,mls);
                                ref.getStatus().setAvgD1(mls.getStatus().getAvgD1());
                                ref.getStatus().setErrorRateD1(mls.getStatus().getErrorRateD1());

                                try {
                                    ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), 1);
                                    //mls.getStatus().savePerf(s.getCodif() , 1);
                                } catch (Exception e) {
                                }
                            }


                            /**for 5 day prevision*/
                            if (mls.getStatus().getErrorRateD5() < ref.getStatus().getErrorRateD5() ||
                                (mls.getStatus().getErrorRateD5() == ref.getStatus().getErrorRateD5() &&
                                    mls.getStatus().getAvgD5() < ref.getStatus().getAvgD5())) {
                                ref.replace(PredictionPeriodicity.D5, mls);
                                ref.getStatus().setAvgD5(mls.getStatus().getAvgD5());
                                ref.getStatus().setErrorRateD5(mls.getStatus().getErrorRateD5());
                                try {
                                    ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), 5);
                                } catch (Exception e) {
                                    log.error("Cannot replace element for period 5 days" + e);
                                }
                            }


                            /**for 20 day prevision*/
                            if (mls.getStatus().getErrorRateD20() < ref.getStatus().getErrorRateD20() ||
                                (mls.getStatus().getErrorRateD20() == ref.getStatus().getErrorRateD20() &&
                                    mls.getStatus().getAvgD20() < ref.getStatus().getAvgD20())) {
                                ref.replace(PredictionPeriodicity.D20, mls);
                                ref.getStatus().setAvgD20(mls.getStatus().getAvgD20());
                                ref.getStatus().setErrorRateD20(mls.getStatus().getErrorRateD20());
                                try {
                                    ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), 20);
                                } catch (Exception e) {
                                    log.error("Cannot replace element for period 20 days" + e);
                                }
                            }

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


        //evaluate();
        MLPredictor predictor = new MLPredictor();

        for (StockGeneral sg : CacheStockGeneral.getCache().values()) {
            StockPrediction p = predictor.prediction(sg);
            sg.setPrediction(p);
        }
        log.info("saveML");
        CacheMLStock.save();

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
                    /**for 1 day prevision*/
                    if (mls.getStatus().getErrorRateD1() <= ref.getStatus().getErrorRateD1() ||
                        (mls.getStatus().getErrorRateD1() == ref.getStatus().getErrorRateD1() &&
                            mls.getStatus().getAvgD1() < ref.getStatus().getAvgD1())) {
                        ref.replace(PredictionPeriodicity.D1, mls);
                        ref.getStatus().setAvgD1(mls.getStatus().getAvgD1());
                        ref.getStatus().setErrorRateD1(mls.getStatus().getErrorRateD1());

                        try {
                            ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), 1);
                            //mls.getStatus().savePerf(s.getCodif() , 1);
                        } catch (Exception e) {
                        }
                    } else {
                        mls.getValidator(PredictionPeriodicity.D1).revertModel(mls.getValidator(PredictionPeriodicity.D1));
                    }


                    /**for 5 day prevision*/
                    if (mls.getStatus().getErrorRateD5() < ref.getStatus().getErrorRateD5() ||
                        (mls.getStatus().getErrorRateD5() == ref.getStatus().getErrorRateD5() &&
                            mls.getStatus().getAvgD5() < ref.getStatus().getAvgD5())) {
                        ref.replace(PredictionPeriodicity.D5, mls);
                        ref.getStatus().setAvgD5(mls.getStatus().getAvgD5());
                        ref.getStatus().setErrorRateD5(mls.getStatus().getErrorRateD5());
                        try {
                            ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), 5);
                        } catch (Exception e) {
                            log.error("Cannot replace element for period 5 days" + e);
                        }
                    } else {
                        mls.getValidator(PredictionPeriodicity.D5).revertModel(mls.getValidator(PredictionPeriodicity.D5));
                    }


                    /**for 20 day prevision*/
                    if (mls.getStatus().getErrorRateD20() < ref.getStatus().getErrorRateD20() ||
                        (mls.getStatus().getErrorRateD20() == ref.getStatus().getErrorRateD20() &&
                            mls.getStatus().getAvgD20() < ref.getStatus().getAvgD20())) {
                        ref.replace(PredictionPeriodicity.D20, mls);
                        ref.getStatus().setAvgD20(mls.getStatus().getAvgD20());
                        ref.getStatus().setErrorRateD20(mls.getStatus().getErrorRateD20());
                        try {
                            ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), 20);
                        } catch (Exception e) {
                            log.error("Cannot replace element for period 20 days" + e);
                        }
                    } else {
                        mls.getValidator(PredictionPeriodicity.D20).revertModel(mls.getValidator(PredictionPeriodicity.D20));
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
        MLPredictor predictor = new MLPredictor();

        for (StockGeneral sg : CacheStockGeneral.getCache().values()) {
            StockPrediction p = predictor.prediction(sg);
            sg.setPrediction(p);
        }
        log.info("saveML");
    }
}

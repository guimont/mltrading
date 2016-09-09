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
    private ExecutorService executorRef;
    private static int DEFAULT_NB_THREADS = 5;

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
            executorRef.submit(() -> {
                try {
                    MLStocks mls = new MLStocks(s.getCodif());

                    if (type == Type.Feature) {
                        //mls.getMlD1().getValidator().generate();
                        mls.getMlD1().getValidator().generateSimpleModel();
                        mls.getMlD5().setValidator(mls.getMlD1().getValidator().clone());
                        mls.getMlD20().setValidator(mls.getMlD1().getValidator().clone());
                    }

                    if (type == Type.RF) {
                        //mls.getMlD1().getValidator().numTrees = 20 + i*40;
                        mls.getMlD5().setValidator(mls.getMlD1().getValidator().clone());
                        mls.getMlD20().setValidator(mls.getMlD1().getValidator().clone());
                    }


                    String saveCode;
                    if (method == Method.LinearRegression) {
                        LinearRegressionStock rfs = new LinearRegressionStock();
                        mls = rfs.processRF(s, mls);
                        saveCode = "L";
                    } else {
                        RandomForestStock rfs = new RandomForestStock();
                        mls = rfs.processRF(s, mls);
                        saveCode = "V";
                    }

                    if (null != mls) {
                        mls.getStatus().calculeAvgPrd();
                        mls.getMlD1().getValidator().save(mls.getCodif() + saveCode + "D1", mls.getStatus().getErrorRateD1(), mls.getStatus().getAvgD1());
                        mls.getMlD5().getValidator().save(mls.getCodif() + saveCode + "D5", mls.getStatus().getErrorRateD5(), mls.getStatus().getAvgD5());
                        mls.getMlD20().getValidator().save(mls.getCodif() + saveCode + "D20", mls.getStatus().getErrorRateD20(), mls.getStatus().getAvgD20());
                        MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());


                        if (ref != null) {
                            /**for 1 day prevision*/
                            if (mls.getStatus().getErrorRateD1() <= ref.getStatus().getErrorRateD1() ||
                                (mls.getStatus().getErrorRateD1() == ref.getStatus().getErrorRateD1() &&
                                    mls.getStatus().getAvgD1() < ref.getStatus().getAvgD1())) {
                                ref.getMlD1().setValidator(mls.getMlD1().getValidator());
                                ref.getMlD1().setModel(mls.getMlD1().getModel());
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
                                ref.getMlD5().setValidator(mls.getMlD5().getValidator());
                                ref.getMlD5().setModel(mls.getMlD5().getModel());
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
                                ref.getMlD20().setValidator(mls.getMlD20().getValidator());
                                ref.getMlD20().setModel(mls.getMlD20().getModel());
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
        mls.getMlD1().getValidator().generateSimpleModel();
        mls.getMlD5().setValidator(mls.getMlD1().getValidator().clone());
        mls.getMlD20().setValidator(mls.getMlD1().getValidator().clone());


        while (mls.getMlD1().getValidator().randomizeModel(mls.getMlD1().getValidator())) {
            mls.getMlD5().getValidator().randomizeModel(mls.getMlD5().getValidator());
            mls.getMlD20().getValidator().randomizeModel(mls.getMlD20().getValidator());


            RandomForestStock rfs = new RandomForestStock();
            mls = rfs.processRF(s, mls, PredictionPeriodicity.D1);
            mls = rfs.processRF(s, mls, PredictionPeriodicity.D5);
            mls = rfs.processRF(s, mls, PredictionPeriodicity.D20);
            String saveCode = "V";


            if (null != mls) {
                mls.getStatus().calculeAvgPrd();
                mls.getMlD1().getValidator().save(mls.getCodif() + saveCode + "D1", mls.getStatus().getErrorRateD1(), mls.getStatus().getAvgD1());
                mls.getMlD5().getValidator().save(mls.getCodif() + saveCode + "D5", mls.getStatus().getErrorRateD5(), mls.getStatus().getAvgD5());
                mls.getMlD20().getValidator().save(mls.getCodif() + saveCode + "D20", mls.getStatus().getErrorRateD20(), mls.getStatus().getAvgD20());
                MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());


                if (ref != null) {
                    /**for 1 day prevision*/
                    if (mls.getStatus().getErrorRateD1() <= ref.getStatus().getErrorRateD1() ||
                        (mls.getStatus().getErrorRateD1() == ref.getStatus().getErrorRateD1() &&
                            mls.getStatus().getAvgD1() < ref.getStatus().getAvgD1())) {
                        ref.getMlD1().setValidator(mls.getMlD1().getValidator());
                        ref.getMlD1().setModel(mls.getMlD1().getModel());
                        ref.getStatus().setAvgD1(mls.getStatus().getAvgD1());
                        ref.getStatus().setErrorRateD1(mls.getStatus().getErrorRateD1());

                        try {
                            ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), 1);
                            //mls.getStatus().savePerf(s.getCodif() , 1);
                        } catch (Exception e) {
                        }
                    } else {
                        mls.getMlD1().getValidator().revertModel(mls.getMlD1().getValidator());
                    }


                    /**for 5 day prevision*/
                    if (mls.getStatus().getErrorRateD5() < ref.getStatus().getErrorRateD5() ||
                        (mls.getStatus().getErrorRateD5() == ref.getStatus().getErrorRateD5() &&
                            mls.getStatus().getAvgD5() < ref.getStatus().getAvgD5())) {
                        ref.getMlD5().setValidator(mls.getMlD5().getValidator());
                        ref.getMlD5().setModel(mls.getMlD5().getModel());
                        ref.getStatus().setAvgD5(mls.getStatus().getAvgD5());
                        ref.getStatus().setErrorRateD5(mls.getStatus().getErrorRateD5());
                        try {
                            ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), 5);
                        } catch (Exception e) {
                            log.error("Cannot replace element for period 5 days" + e);
                        }
                    } else {
                        mls.getMlD5().getValidator().revertModel(mls.getMlD5().getValidator());
                    }


                    /**for 20 day prevision*/
                    if (mls.getStatus().getErrorRateD20() < ref.getStatus().getErrorRateD20() ||
                        (mls.getStatus().getErrorRateD20() == ref.getStatus().getErrorRateD20() &&
                            mls.getStatus().getAvgD20() < ref.getStatus().getAvgD20())) {
                        ref.getMlD20().setValidator(mls.getMlD20().getValidator());
                        ref.getMlD20().setModel(mls.getMlD20().getModel());
                        ref.getStatus().setAvgD20(mls.getStatus().getAvgD20());
                        ref.getStatus().setErrorRateD20(mls.getStatus().getErrorRateD20());
                        try {
                            ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), 20);
                        } catch (Exception e) {
                            log.error("Cannot replace element for period 20 days" + e);
                        }
                    } else {
                        mls.getMlD20().getValidator().revertModel(mls.getMlD20().getValidator());
                    }

                } else {
                    CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                    ref = mls;
                }

                int position = mls.getMlD1().getValidator().getCol();
                mls = new MLStocks(s.getCodif());
                mls.getMlD1().setValidator(ref.getMlD1().getValidator().clone());
                mls.getMlD1().getValidator().setCol(position);
                mls.getMlD5().setValidator(ref.getMlD5().getValidator().clone());
                mls.getMlD5().getValidator().setCol(position);
                mls.getMlD20().setValidator(ref.getMlD20().getValidator().clone());
                mls.getMlD20().getValidator().setCol(position);
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

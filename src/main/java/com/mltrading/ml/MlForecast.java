package com.mltrading.ml;

import com.mltrading.config.MLProperties;
import com.mltrading.ml.model.RandomForestStock;
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
public class MlForecast {

    private static final List<PredictionPeriodicity> periodicity = Arrays.asList(PredictionPeriodicity.D1, PredictionPeriodicity.D5, PredictionPeriodicity.D20, PredictionPeriodicity.D40);


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

        for (int i = 0; i < loop; i++) {
            CacheMLStock.getMLStockCache().values().forEach(m -> m.resetScoring());
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
    public void optimize(Stream<? extends StockHistory> stream, int size, int backloop, String validator, Method method) {

        //final CountDownLatch latches = new CountDownLatch(size);
        final CountDownLatch latches = new CountDownLatch(1); //testmode ORA
        CacheMLActivities.setCountGlobal(latches.getCount());

        CacheMLActivities.addActivities(new MLActivities("optimize forecast", "", "start", 0, 0, false));

        //For test purpose only
        stream.filter(s -> s.getCodif().equals("ORA")).forEach(s -> executorRef.submit(() -> {    //For test purpose only
            try {
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
     * processing machaine learning algorithm
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


            //mls.generateValidator("generate"); too big for test
            mls.generateValidator(validator);


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


                MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());


                if (ref != null) {

                    if (checkResult(mls, ref, PredictionPeriodicity.D1))
                        CacheMLActivities.addActivities(new MLActivities("optimize", codif, "increase model: " + PredictionPeriodicity.D1, loop, 1, true));
                    else
                        CacheMLActivities.addActivities(new MLActivities("optimize", codif, "not increase model: " + PredictionPeriodicity.D1, loop, 0, true));

                    if (checkResult(mls, ref, PredictionPeriodicity.D5))
                        CacheMLActivities.addActivities(new MLActivities("optimize", codif, "increase model: " + PredictionPeriodicity.D5, loop, 1, true));
                    else
                        CacheMLActivities.addActivities(new MLActivities("optimize", codif, "not increase model: " + PredictionPeriodicity.D5, loop, 0, true));

                    if (checkResult(mls, ref, PredictionPeriodicity.D20))
                        CacheMLActivities.addActivities(new MLActivities("optimize", codif, "increase model: " + PredictionPeriodicity.D20, loop, 1, true));
                    else
                        CacheMLActivities.addActivities(new MLActivities("optimize", codif, "not increase model: " + PredictionPeriodicity.D20, loop, 0, true));

                    if (checkResult(mls, ref, PredictionPeriodicity.D40))
                        CacheMLActivities.addActivities(new MLActivities("optimize", codif, "increase model: " + PredictionPeriodicity.D40, loop, 1, true));
                    else
                        CacheMLActivities.addActivities(new MLActivities("optimize", codif, "not increase model: " + PredictionPeriodicity.D40, loop, 0, true));


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
     * compare result
     *
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
     *
     * @param mls
     * @param ref
     * @param period
     * @return
     */
    private boolean checkResult(MLStocks mls, MLStocks ref, PredictionPeriodicity period) {
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
     * compare result and replace it if better
     *
     * @param mls
     * @param ref
     * @param period
     * @return
     */
    private boolean checkResultReplace(MLStocks mls, MLStocks ref, PredictionPeriodicity period) {
        if (compareResult(mls.getStatus(), ref.getStatus(), period)) {
            ref.insert(period, mls);
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

    /**
     * optimization for matrix validator
     * use iteration on model not only one model
     * proccessing very long
     */
    public void optimizeModel() {

        //final CountDownLatch latches = new CountDownLatch(CacheStockGeneral.getIsinCache().values().size());
        final CountDownLatch latches = new CountDownLatch(1);

        CacheStockGeneral.getIsinCache().values().stream().filter(s -> s.getCodif().equals("ORA")).forEach(s -> executorRef.submit(() -> {    //For test purpose only
            try {
                optimizeModel(s);
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

        updatePredictor();


        CacheMLActivities.addActivities(new MLActivities("saveValidator forecast model", "", "start", 0, 0, false));
        log.info("saveML");
        CacheMLStock.save();
        CacheMLActivities.addActivities(new MLActivities("saveValidator forecast model", "", "end", 0, 0, true));

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

        MLStocks ref = null;


        MLStocks validatorModel = new MLStocks(s.getCodif());
        validatorModel.generateValidator("generateSimpleModel");

        MLStocks mls = new MLStocks(s.getCodif());
        mls.generateValidator("generateSimpleModel");

        while (mls.randomizeModel()) {

            RandomForestStock rfs = new RandomForestStock();
            mls = rfs.processRFRef(s.getCodif(), mls, true);

            String saveCode = "V";


            if (null != mls) {
                mls.getStatus().calculeAvgPrd();
                mls.getValidator(PredictionPeriodicity.D1).save(mls.getCodif() + saveCode + "D1", mls.getStatus().getErrorRateD1(), mls.getStatus().getAvgD1());
                mls.getValidator(PredictionPeriodicity.D5).save(mls.getCodif() + saveCode + "D5", mls.getStatus().getErrorRateD5(), mls.getStatus().getAvgD5());
                mls.getValidator(PredictionPeriodicity.D20).save(mls.getCodif() + saveCode + "D20", mls.getStatus().getErrorRateD20(), mls.getStatus().getAvgD20());

                int col = mls.getValidator(PredictionPeriodicity.D1).getCol() -1;

                if (ref != null) {



                    if (compareResult(mls.getStatus(), ref.getStatus(), PredictionPeriodicity.D1))
                        validatorModel.getValidator(PredictionPeriodicity.D1).validate(col);

                    if (compareResult(mls.getStatus(), ref.getStatus(), PredictionPeriodicity.D5))
                        validatorModel.getValidator(PredictionPeriodicity.D5).validate(col);

                    if (compareResult(mls.getStatus(), ref.getStatus(), PredictionPeriodicity.D20))
                        validatorModel.getValidator(PredictionPeriodicity.D20).validate(col);

                    if (compareResult(mls.getStatus(), ref.getStatus(), PredictionPeriodicity.D40))
                        validatorModel.getValidator(PredictionPeriodicity.D40).validate(col);


                } else {
                    ref = mls.clone();
                }

                mls = new MLStocks(s.getCodif());
                mls.generateValidator("generateSimpleModel");
                mls.updateColValidator(col+1);



            }


        }
    }


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
            double consensus = StockHistory.getStockHistoryLast(sg.getCodif(), 1).get(0).getConsensusNote();
            sg.setPerformanceEstimate(p.getConfidenceD20() / 20 * yield20 * p.getConfidenceD5() / 10 * yield5
                * consensus);

            double essai = 0;
        } else {
            sg.setPerformanceEstimate(0.);
        }
    }

}

package com.mltrading.ml;

import com.mltrading.config.MLProperties;
import com.mltrading.ml.genetic.GeneticAlgorithm;
import com.mltrading.ml.model.GradiantBoostStock;
import com.mltrading.ml.model.MlModelGeneric;
import com.mltrading.ml.model.ModelType;
import com.mltrading.ml.model.RandomForestStock;
import com.mltrading.ml.util.Combination;
import com.mltrading.ml.util.Ensemble;
import com.mltrading.ml.util.Evaluate;
import com.mltrading.ml.util.FixedThreadPoolExecutor;
import com.mltrading.models.stock.*;

import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockSector;
import com.mltrading.models.util.CsvFileReader;
import com.mltrading.models.util.CsvFileWriter;
import com.mltrading.models.util.MLActivities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    public void processList(ModelType type) {

        List<StockGeneral> l = new ArrayList(CacheStockGeneral.getIsinCache().values());

        MlModelGeneric rfs;


        if (type == ModelType.RANDOMFOREST) {
            rfs = new RandomForestStock();
        }
        else {
            rfs = new GradiantBoostStock();
        }


        for (StockGeneral s : l) {
            MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
            if (mls != null && mls.isEmtpyModel() == false) {
                mls = rfs.processRFResult(s.getCodif(), mls);
                mls.getStatus(type).calculeAvgPrd();
            }
        }


        List<StockSector> ls = new ArrayList(CacheStockSector.getSectorCache().values());

        for (StockSector s : ls) {
            MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
            if (mls != null && mls.isEmtpyModel() == false) {
                mls = rfs.processRFResult(s.getCodif(), mls);
                mls.getStatus(type).calculeAvgPrd();
            }
        }


        //check

        log.info("result mlf size: " + CacheMLStock.getMLStockCache().size());

        for (MLStocks mls : CacheMLStock.getMLStockCache().values()) {
            log.info("perf list result size: " + mls.getStatus(type).getPerfList().size());
        }

    }

    /**
     * Constructor
     * Specifiy size of threadPool executor
     */
    public MlForecast() {

        int nbThread = MLProperties.getProperty("optimize.threads", DEFAULT_NB_THREADS);

        this.executorRef = new FixedThreadPoolExecutor(nbThread,
            "ExtractionRefThreadPool");
    }


    /**
     * function to export matrix validator model to csv file
     * it a backup model to regenerate model from file
     */
    public void exportModel() {
        CsvFileWriter fileWriter = new CsvFileWriter("MatrixValidator.csv", "");

        List<StockGeneral> l = new ArrayList(CacheStockGeneral.getIsinCache().values());


        for (StockGeneral s : l) {
            MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
            if (mls != null && mls.isEmtpyModel() == false) {
                periodicity.forEach(p -> mls.getModel(p).export(fileWriter,s.getCodif(),p.toString()));
            }
        }

        List<StockSector> ls = new ArrayList(CacheStockSector.getSectorCache().values());

        for (StockSector s : ls) {
            MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
            if (mls != null && mls.isEmtpyModel() == false) {
                periodicity.forEach(p -> mls.getModel(p).export(fileWriter,s.getCodif(),p.toString()));
            }
        }


        fileWriter.close();
    }

    /**
     *
     */
    public void importModel() {


        try {
            HashMap<String, MatrixValidator> mapMV = new HashMap<>();
            new CsvFileReader("MatrixValidator.csv",mapMV);



            List<StockGeneral> l = new ArrayList(CacheStockGeneral.getIsinCache().values());

            /**
             * Start RANDOMFOREST import
             */
            for (StockGeneral s : l) {


                final MLStocks mls = new MLStocks(s.getCodif());

                if (mapMV.get(s.getCodif() + ModelType.code(ModelType.RANDOMFOREST) + PredictionPeriodicity.D1) == null)
                    continue;

                periodicity.forEach(p -> {
                    MatrixValidator mv = mapMV.get(s.getCodif() + ModelType.code(ModelType.RANDOMFOREST) + p);
                    mls.createValidator(p, mv, ModelType.RANDOMFOREST);
                    mls.getSock(p).setModelImprove(true);
                });

                RandomForestStock rfs = new RandomForestStock();
                rfs.processSpecifcRFRef(s.getCodif(), mls);
                if (mls.getStatus(ModelType.RANDOMFOREST).getPerfList() != null) {
                    mls.getStatus(ModelType.RANDOMFOREST).calculeAvgPrd();
                    CacheMLStock.getMLStockCache().put(s.getCodif(), mls);
                    PeriodicityList.periodicity.forEach(p -> {
                        mls.saveModel(p, ModelType.RANDOMFOREST);
                        mls.saveDB(p, ModelType.RANDOMFOREST);
                    });
                    mls.getStatus(ModelType.RANDOMFOREST).savePerf(mls.getCodif(), ModelType.RANDOMFOREST);
                    mls.saveValidator(ModelType.RANDOMFOREST);
                }
            }
            /**
             * end RANDOMFOREST import
             */

            /**
             * Start GRADIANTBOOSTTREE import
             */

            //boolean jump = true;
            for (StockGeneral s : l) {

                final MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());

                /*if (s.getCodif().equalsIgnoreCase("ENGI")) jump = false;
                if (jump) continue;*/

                /** cannot filled so RF is not valid*/
                /** skip this model GBT so           */
                if (mls == null) {
                    continue;
                }

                if (mapMV.get(s.getCodif() + ModelType.code(ModelType.GRADIANTBOOSTTREE) + PredictionPeriodicity.D1) == null)
                    continue;

                periodicity.forEach(p -> {
                    MatrixValidator mv = mapMV.get(s.getCodif() + ModelType.code(ModelType.GRADIANTBOOSTTREE) + p);
                    mls.createValidator(p, mv, ModelType.GRADIANTBOOSTTREE);
                    mls.getSock(p).setModelImprove(true);
                });

                GradiantBoostStock gbt = new GradiantBoostStock();
                gbt.processSpecifcRFRef(s.getCodif(),mls);
                if (mls.getStatus(ModelType.GRADIANTBOOSTTREE).getPerfList() != null) {
                    mls.getStatus(ModelType.GRADIANTBOOSTTREE).calculeAvgPrd();
                    CacheMLStock.getMLStockCache().put(s.getCodif(), mls);
                    PeriodicityList.periodicity.forEach(p -> {
                        mls.saveModel(p, ModelType.GRADIANTBOOSTTREE);
                        mls.saveDB(p, ModelType.GRADIANTBOOSTTREE);
                    });
                    mls.getStatus(ModelType.GRADIANTBOOSTTREE).savePerf(mls.getCodif(), ModelType.GRADIANTBOOSTTREE);
                    mls.saveValidator(ModelType.GRADIANTBOOSTTREE);
                }

            }
            /**
             * end GRADIANTBOOSTTREE import
             */

            for (StockGeneral s : l) {

                final MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
                updateEnsemble();
                mls.getStatus(ModelType.ENSEMBLE).savePerf(mls.getCodif(), ModelType.ENSEMBLE);
            }


            List<StockSector> ls = new ArrayList(CacheStockSector.getSectorCache().values());

            /**
             * Start RANDOMFOREST import
             */
            for (StockSector s : ls) {

                final MLStocks mls = new MLStocks(s.getCodif());

                periodicity.forEach(p -> {
                    MatrixValidator mv = mapMV.get(s.getCodif() + ModelType.code(ModelType.RANDOMFOREST) + p);
                    mls.createValidator(p, mv, ModelType.RANDOMFOREST);
                    mls.getSock(p).setModelImprove(true);
                });

                RandomForestStock rfs = new RandomForestStock();
                rfs.processSpecifcRFRef(s.getCodif(), mls);
                if (mls.getStatus(ModelType.RANDOMFOREST).getPerfList() != null) {
                    mls.getStatus(ModelType.RANDOMFOREST).calculeAvgPrd();
                    CacheMLStock.getMLStockCache().put(s.getCodif(), mls);
                    PeriodicityList.periodicity.forEach(p -> {
                        mls.saveModel(p, ModelType.RANDOMFOREST);
                        mls.saveDB(p, ModelType.RANDOMFOREST);
                    });
                    mls.getStatus(ModelType.RANDOMFOREST).savePerf(mls.getCodif(), ModelType.RANDOMFOREST);
                    mls.saveValidator(ModelType.RANDOMFOREST);
                }
            }

            /**
             * end RANDOMFOREST import
             */

            /**
             * Start GRADIANTBOOSTTREE import
             */
            for (StockSector s : ls) {

                final MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());

                /** cannot filled so RF is not valid*/
                /** skip this model GBT so           */
                if (mls == null) {
                    continue;
                }

                periodicity.forEach(p -> {

                    MatrixValidator mv = mapMV.get(s.getCodif() + ModelType.code(ModelType.GRADIANTBOOSTTREE) + p);
                    mls.createValidator(p, mv, ModelType.GRADIANTBOOSTTREE);
                    mls.getSock(p).setModelImprove(true);
                });

                GradiantBoostStock gbt = new GradiantBoostStock();
                gbt.processSpecifcRFRef(s.getCodif(),mls);
                if (mls.getStatus(ModelType.GRADIANTBOOSTTREE).getPerfList() != null) {
                    mls.getStatus(ModelType.GRADIANTBOOSTTREE).calculeAvgPrd();
                    CacheMLStock.getMLStockCache().put(s.getCodif(), mls);
                    PeriodicityList.periodicity.forEach(p -> {
                        mls.saveModel(p, ModelType.GRADIANTBOOSTTREE);
                        mls.saveDB(p, ModelType.GRADIANTBOOSTTREE);
                    });
                    mls.getStatus(ModelType.GRADIANTBOOSTTREE).savePerf(mls.getCodif(), ModelType.GRADIANTBOOSTTREE);
                    mls.saveValidator(ModelType.GRADIANTBOOSTTREE);
                }

            }
            /**
             * end GRADIANTBOOSTTREE import
             */

        } catch (Exception e) {
            log.error(e.toString());
        }


    }


    public enum Type {
        Feature,
        None,
        RF
    }


    /**
     * optimization for matrix validator
     * @param loop: indicate loop for each saveValidator model
     * @param backloop: iteration before saveValidator
     * @param validator: validator model
     * @param target: PX1 or sector only
     */
    public void optimize(int loop, int backloop, String validator, String target, ModelType type) {

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
                optimize(CacheStockGeneral.getIsinCache().values().stream(), CacheStockGeneral.getIsinCache().values().size(), backloop, validator, type);
            else
                optimize(CacheStockSector.getSectorCache().values().stream(), CacheStockSector.getSectorCache().values().size(), backloop, validator, type);

            updateEnsemble();
            updatePredictor();


            CacheMLActivities.addActivities(new MLActivities("saveValidator forecast model", "", "start", 0, 0, false));
            log.info("saveML");
            CacheMLStock.save(type);
            CacheMLActivities.addActivities(new MLActivities("saveValidator forecast model", "", "end", 0, 0, true));
        }

        CacheMLActivities.endRunning();
        //evaluate();

    }

    public void updateEnsemble() {

        CacheMLStock.getMLStockCache().values()
            .forEach( m -> {

                final GeneticAlgorithm<Ensemble> algo = new GeneticAlgorithm<>(e -> e.evaluate(m), () -> Ensemble.newInstance(),
                    (first, second) -> first.merge(second), e -> e.mutate());

                algo.initialize(20);

                /*if model is empty*/
                algo.iterate(100, 2, 10, 0, 4);


                {
                    List<MLPerformances> mlPerformancesRF =  m.getStatus(ModelType.RANDOMFOREST).getPerfList();
                    List<MLPerformances> mlPerformancesGBT =  m.getStatus(ModelType.GRADIANTBOOSTTREE).getPerfList();

                    m.ratio = algo.best().getRatio();

                    if (mlPerformancesRF .size() == 0 || mlPerformancesGBT.size()==0)
                        return;

                    int size = mlPerformancesRF .size();

                    List<MLPerformances> listEnsemble = new ArrayList<>();

                    for (int index = 0; index< size; index++){

                        MLPerformances mlRF = mlPerformancesRF.get(index);
                        MLPerformances mlGBT = mlPerformancesGBT.get(index);
                        MLPerformances perf = new MLPerformances(mlRF.getDate());
                        periodicity.forEach(p -> {
                            MLPerformance mpRF = mlRF.getMl(p);
                            MLPerformance mpGBT = mlGBT.getMl(p);


                            perf.setMl(MLPerformance.calculYields(mlRF.getDate(),
                                (mpRF.getPrediction() * m.getRatio() + mpGBT.getPrediction())/(1. + m.ratio),
                                mpRF.getRealvalue(), mpRF.getCurrentValue()), p);


                        });
                        listEnsemble.add(perf);


                    }

                    m.getStatus(ModelType.ENSEMBLE).setPerfList(listEnsemble);
                    m.getStatus(ModelType.ENSEMBLE).calculeAvgPrd();



                }
            });


    }


    /**
     * sub function to iterate and use threadpool
     * @param stream
     * @param size
     * @param backloop
     * @param validator
     */
    private void optimize(Stream<? extends StockHistory> stream, int size, int backloop, String validator,  ModelType type) {

        final CountDownLatch latches = new CountDownLatch(size);
        //final CountDownLatch latches = new CountDownLatch(1); //testmode ORA
        CacheMLActivities.setCountGlobal(latches.getCount());

        CacheMLActivities.addActivities(new MLActivities("optimize forecast", "", "start", 0, 0, false));

        //For test purpose only
        stream./*filter(s -> s.getCodif().equals("ORA")).*/forEach(s -> executorRef.submit(() -> {    //For test purpose only
            try {
                if (validator.contains("optimizeModel"))
                    optimizeModel(s.getCodif(),type);
                else if (validator.contains("optimizeGenetic"))
                    optimizeGenetic(s.getCodif(),backloop, type);
                else
                    optimize(s.getCodif(), backloop, type, validator);
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
     * @param type not use
     * @param validator  validator model
     */
    private void optimize(String codif, int loop,  ModelType type, String validator) {

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
                mls.generateValidator("generateEconomicalModel",rowSector, type);
                if (ref != null) mls.mergeValidator(ref, type);
            } else {
                mls.generateValidator(validator,rowSector, type);
            }



            if (type == ModelType.RANDOMFOREST) {
                RandomForestStock rfs = new RandomForestStock();
                rfs.processRFRef(codif, mls, false);
            }
            else {
                GradiantBoostStock rfs = new GradiantBoostStock();
                rfs.processRFRef(codif, mls, false);
            }


            if (null != mls) {
                mls.getStatus(type).calculeAvgPrd();




                periodicity.forEach(p -> {
                    try {
                        mls.getModel(p).getValidator(type).save(mls.getCodif() + ModelType.code(type) +
                            p, mls.getStatus(type).getErrorRate(p), mls.getStatus(type).getAvg(p));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });




                if (ref != null
                    && ref.getStatus(type).getPerfList() != null
                    && !ref.getStatus(type).getPerfList().isEmpty()) {

                    periodicity.forEach(p -> {
                        if (checkResult(mls, ref, p, type))
                            CacheMLActivities.addActivities(new MLActivities("optimize", codif, "increase model: " + p, loop, 1, true));
                        else
                            CacheMLActivities.addActivities(new MLActivities("optimize", codif, "not increase model: " + p, loop, 0, true));
                    });


                } else if (ref == null) {
                    /* empty ref so improve scoring yes*/
                    mls.setScoring(true);
                    CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                    CacheMLActivities.addActivities(new MLActivities("optimize", codif, "empty model", loop, 0, true));
                }
                else if (ref != null) {
                    ref.setScoring(true);
                    //replace MlSTocks => period => model
                    ref.setModels(type,mls);
                    //replace MLStatus => model => period
                    ref.setStatus(mls.getStatus(type),type);

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
    void optimizeGenetic(String codif,int loop, ModelType type) {


        for (PredictionPeriodicity p : periodicity) {
            //periodicity.forEach(p -> {
            log.info("start loop for period: " + p);
            final GeneticAlgorithm<Combination> algo = new GeneticAlgorithm<Combination>(c -> c.evaluate(codif,p,type), () -> Combination.newInstance(),
                (first, second) -> first.merge(second), c -> c.mutate());
            algo.initialize(8);

            /*if model is empty*/
            if (CacheMLStock.getMLStockCache().get(codif) != null)
                algo.addReference(new Combination(CacheMLStock.getMLStockCache().get(codif).getModel(p).getValidator(type).clone()));
            algo.iterate(loop, 2, 6, 4, 0);
            //algo.printTo(System.err);


            /*Validate result */
            final MLStocks mls = new MLStocks(codif);
            MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());
            mls.getModel(p).setValidator(type,algo.best().getMv());


            if (type == ModelType.RANDOMFOREST) {
                RandomForestStock rfs = new RandomForestStock();
                rfs.processRFRef(codif, mls, false, p);
            }
            else {
                GradiantBoostStock rfs = new GradiantBoostStock();
                rfs.processRFRef(codif, mls, false, p);
            }

            mls.getStatus(type).calculeAvgPrd();

            if (ref != null
                && ref.getStatus(type).getPerfList() != null
                && !ref.getStatus(type).getPerfList().isEmpty()) {

                if ( MlForecast.checkResult(mls, ref, p, type))
                    CacheMLActivities.addActivities(new MLActivities("optimize", codif, "increase model: " + p, 0, 1, true));
                else
                    CacheMLActivities.addActivities(new MLActivities("optimize", codif, "not increase model: " + p, 0, 0, true));
            }  else if (ref == null) {
                /* empty ref so improve scoring yes*/
                mls.setScoring(true);
                CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                CacheMLActivities.addActivities(new MLActivities("optimize", codif, "empty model", 0, 0, true));
            } else if (ref != null) {
                ref.setScoring(true);
                //replace MlSTocks => period => model
                ref.setModels(type,mls);
                //replace MLStatus => model => period
                ref.setStatus(mls.getStatus(type),type);

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
    public static  boolean checkResult(MLStocks mls, MLStocks ref, PredictionPeriodicity period, ModelType type) {
        if (compareResult(mls.getStatus(type), ref.getStatus(type), period)) {
            ref.replace(period, mls, type );
            ref.getStatus(type).setAvg(mls.getStatus(type).getAvgD1(), period);
            ref.getStatus(type).setErrorRate(mls.getStatus(type).getErrorRate(period), period);
            ref.getSock(period).setModelImprove(true);
            try {
                ref.getStatus(type).replaceElementList(mls.getStatus(type).getPerfList(), period);
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
    private void optimizeModel(String codif, ModelType type) {

        /**
         * to have some results faster, 2 iterations loop
         */

        MLStocks ref = null;

        MLStocks validatorModel = new MLStocks(codif);
        validatorModel.generateValidator("generateSimpleModel",NOTUSE, type);

        MLStocks mls = new MLStocks(codif);
        mls.generateValidator("generateSimpleModel",NOTUSE, type);

        while (mls.randomizeModel(type)) {

            RandomForestStock rfs = new RandomForestStock();
            mls = rfs.processRFRef(codif, mls, true);

            final MLStocks finalMls = mls;

            String saveCode = "V";


            if (null != finalMls) {
                finalMls.getStatus(type).calculeAvgPrd();

                periodicity.forEach(p -> {
                    try {
                        finalMls.getModel(p).getValidator(type).save(finalMls.getCodif() + saveCode +
                            p, finalMls.getStatus(type).getErrorRate(p), finalMls.getStatus(type).getAvg(p));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                /* period have no importance, iteration are same for all period*/
                int col = mls.getModel(PredictionPeriodicity.D1).getValidator(type).getCol() -1;

                if (ref != null) {

                    final MLStocks finalRef = ref;
                    periodicity.forEach(p -> {
                        if (compareResult(finalMls.getStatus(type), finalRef.getStatus(type), p))
                            validatorModel.getModel(p).getValidator(type).validate(col);
                    });

                } else {
                    ref = finalMls.clone();
                }

                mls = new MLStocks(codif);
                mls.generateValidator("generateSimpleModel",NOTUSE, type);
                mls.updateColValidator(col+1, type);

            }
        }

        /** keep best model*/
        RandomForestStock rfs = new RandomForestStock();
        MLStocks bestModel = rfs.processRFRef(codif, validatorModel, true);
        bestModel.getStatus(type).calculeAvgPrd();

        final MLStocks mlsCache = CacheMLStock.getMLStockCache().get(codif);
        periodicity.forEach(p -> {
            if (compareResult(mlsCache.getStatus(type), bestModel.getStatus(type), p))
                CacheMLStock.getMLStockCache().put(codif, bestModel);
        });




    }


    /**
     *
     */
    public static void updatePredictor() {
        CacheStockGeneral.getCache().values().forEach(s -> updatePredictor(s,  true));

        CacheStockSector.getSectorCache().values().forEach(s -> updatePredictor(s,false));
    }

    private static void updatePredictor(StockHistory sg, boolean bRanking) {
        MLPredictor predictor = new MLPredictor();

        CacheMLStock.getMlRankCache();

        StockPrediction p = predictor.prediction(sg.getCodif(),bRanking);

        if (p != null) {
            sg.setPrediction(p);
            /*double yield20 = (p.getPredictionD20() - sg.getCurrentValue()) / sg.getCurrentValue();
            double yield5 = Math.abs((p.getPredictionD5() - sg.getCurrentValue()) / sg.getCurrentValue());
            double consensus = 1;//StockHistory.getStockHistoryLast(sg.getCodif(), 1).get(0).getConsensusNote();
            sg.setPerformanceEstimate(p.getConfidenceD20() / 20 * yield20 * p.getConfidenceD5() / 10 * yield5
                * consensus);

            double essai = 0;*/

            sg.setPerformanceEstimate(p.getYieldD20());
        } else {
            sg.setPerformanceEstimate(0.);
        }
    }

}

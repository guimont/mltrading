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

import com.mltrading.models.util.MLActivities;
import com.mltrading.web.rest.dto.ForecastDTO;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Created by gmo on 08/01/2016.
 */
@Service
public class MlForecast extends Evaluate{


    private static final Logger log = LoggerFactory.getLogger(MlForecast.class);
    private ExecutorService executorRef;
    private static int DEFAULT_NB_THREADS = 2;

    private MlStockType mlStockType;
    private ForecastDTO forecastDTO;
    private Map<String, MLStocks> stocksMap;

    public void processList(ModelType type) {

        List<StockGeneral> l = new ArrayList<>(CacheStockGeneral.getIsinCache().values());

        MlModelGeneric rfs;


        if (type == ModelType.RANDOMFOREST) rfs = new RandomForestStock();
        else rfs = new GradiantBoostStock();


        for (StockGeneral s : l) {
            MLStocks mls = stocksMap.get(s.getCodif());
            if (mls != null && mls.isEmtpyModel() == false) {
                mls = rfs.processRFResult(s.getCodif(), mls);
                mls.getStatus(type).calculeAvgPrd();
            }
        }


        List<StockSector> ls = new ArrayList<>(CacheStockSector.getSectorCache().values());

        for (StockSector s : ls) {
            MLStocks mls = stocksMap.get(s.getCodif());
            if (mls != null && mls.isEmtpyModel() == false) {
                mls = rfs.processRFResult(s.getCodif(), mls);
                mls.getStatus(type).calculeAvgPrd();
            }
        }


        //check

        log.info("result mlf size: " + stocksMap.size());

        for (MLStocks mls : stocksMap.values()) {
            log.info("perf list result size: " + mls.getStatus(type).getPerfList().size());
        }

    }


    public MlForecast() {

    }

    /**
     * Constructor
     * Specifiy size of threadPool executor
     */
    public MlForecast(ForecastDTO forecastDTO) {

        int nbThread = MLProperties.getProperty("optimize.threads", DEFAULT_NB_THREADS);

        this.executorRef = new FixedThreadPoolExecutor(nbThread,
            "ExtractionRefThreadPool");

        this.mlStockType = MlStockType.Convert(forecastDTO.getForecastType());

        this.forecastDTO = forecastDTO;

        setMap();
    }

    private void setMap() {
        switch (mlStockType) {
            case BASE: this.stocksMap = CacheMLStock.getMLStockCache(); break;
            case SHORT: this.stocksMap = CacheMLStock.getMLStockShortCache(); break;
        }
    }




    public void optimize() {
        if (forecastDTO.getSpecific().equalsIgnoreCase("ALL")) {
            if (forecastDTO.getTarget().equals("PX1"))
                optimizeBase(new ArrayList(CacheStockGeneral.getIsinCache().values()), CacheStockGeneral.getIsinCache().values().size());
            else
                optimizeBase(new ArrayList(CacheStockSector.getSectorCache().values()), CacheStockSector.getSectorCache().values().size());
        }

    }


    public enum Type {
        Feature,
        None,
        RF
    }




    /**
     * * optimization for matrix validator
     * @param list
     * @param size
     */
        private void optimizeBase(List list, int size) {

        if (!CacheMLActivities.setIsRunning()) {
            log.error("optimizing still running, cannot launch new optimization");
            return;
        }

        for (int i = 0; i < forecastDTO.getGlobalLoop(); i++) {
            stocksMap.values()
                .forEach(m ->
                {
                    if (m!=null)
                        m.resetScoring();
                });

            optimize(list, size);

            updateEnsemble();
            updatePredictor();


            CacheMLActivities.addActivities(new MLActivities("saveValidator forecast model", "", "start", 0, 0, false));
            log.info("saveML");
            save();
            CacheMLActivities.addActivities(new MLActivities("saveValidator forecast model", "", "end", 0, 0, true));
        }

        CacheMLActivities.endRunning();
        //evaluate();

    }


    private void save() {
        switch (mlStockType) {
            case BASE:  CacheMLStock.save( ModelType.get(forecastDTO.getModelType())); break;
            case SHORT:  CacheMLStock.saveShort( ModelType.get(forecastDTO.getModelType())); break;
        }
    }

    public void updateEnsemble() {

        stocksMap.values()
            .forEach( m -> {

                List<MLPerformances> mlPerformancesRF =  m.getStatus(ModelType.RANDOMFOREST).getPerfList();
                List<MLPerformances> mlPerformancesGBT =  m.getStatus(ModelType.GRADIANTBOOSTTREE).getPerfList();

                if ((((mlPerformancesRF == null) || (mlPerformancesGBT == null))
                    || (((mlPerformancesRF != null) && (mlPerformancesRF.size() == 0)) || ((mlPerformancesGBT != null) && (mlPerformancesGBT.size() == 0)))))
                    return;

                final GeneticAlgorithm<Ensemble> algo = new GeneticAlgorithm<>(e -> e.evaluate(m), Ensemble::newInstance,
                    Ensemble::merge, Ensemble::mutate);

                algo.initialize(20);

                /*if model is empty*/
                algo.iterate(100, 2, 10, 0, 4);

                {

                    m.ratio = algo.best().getRatio();

                    int size = mlPerformancesRF .size();

                    List<MLPerformances> listEnsemble = new ArrayList<>();

                    for (int index = 0; index< size; index++){

                        MLPerformances mlRF = mlPerformancesRF.get(index);
                        MLPerformances mlGBT = mlPerformancesGBT.get(index);
                        MLPerformances perf = new MLPerformances(mlRF.getDate());
                        PeriodicityList.periodicityLong.forEach(p -> {
                            MLPerformance mpRF = mlRF.getMl(p);
                            MLPerformance mpGBT = mlGBT.getMl(p);


                            if (mpRF.getRealvalue() == -1)
                                perf.setMl(new MLPerformance(mlRF.getMl(p).getDate(), mlRF.getMl(p).getCurrentDate(),(mpRF.getPrediction() * m.getRatio() + mpGBT.getPrediction())/(1. + m.ratio), -1, mpRF.getCurrentValue(), 0, 0, true), p);

                            else

                            perf.setMl(MLPerformance.calculYields(mlRF.getMl(p).getDate(), mlRF.getMl(p).getCurrentDate() ,
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
     * @param list
     * @param size
     */
    private void optimize(List<? extends StockHistory> list, int size) {

        final CountDownLatch latches = new CountDownLatch(size);
        //final CountDownLatch latches = new CountDownLatch(1); //testmode ORA
        CacheMLActivities.setCountGlobal(latches.getCount());

        CacheMLActivities.addActivities(new MLActivities("optimize forecast", "", "start", 0, 0, false));



        //For test purpose only
        list.stream()./*filter(s -> s.getCodif().equals("ORA")).*/forEach(s -> executorRef.submit(() -> {    //For test purpose only
            try {
                if (forecastDTO.getValidator().contains("optimizeModel"))
                    //optimizeModel(s.getCodif(),type);
                    throw new NotImplementedException("not use now");
                else if (forecastDTO.getValidator().contains("optimizeGenetic"))
                    optimizeGenetic(s.getCodif());
                else
                    optimize(s.getCodif());
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

     */
    private void optimize(String codif) {

        /**
         * to have some results faster, 2 iterations loop
         */

        final String validator = forecastDTO.getValidator();
        final ModelType type = ModelType.get(forecastDTO.getModelType());
        final int loop = forecastDTO.getInputLoop();

        for (int i = 0; i < loop; i++) {



            final MLStocks mls = MLStocks.newStock(codif, forecastDTO);

            CacheMLActivities.addActivities(new MLActivities("optimize", codif, "start", loop, 0, false));
            MLStocks ref = this.stocksMap.get(mls.getCodif());

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


            /* for break purpose only*/
            if (codif.equalsIgnoreCase("BN"))
                log.error("BN");


            if (null != mls) {
                mls.getStatus(type).calculeAvgPrd();

                if (ref != null
                    && ref.getStatus(type).getPerfList() != null
                    && !ref.getStatus(type).getPerfList().isEmpty()) {

                    PeriodicityList.periodicityLong.forEach(p -> {
                        if (checkResult(mls, ref, p, type))
                            CacheMLActivities.addActivities(new MLActivities("optimize", codif, "increase model: " + p, loop, 1, true));
                        else
                            CacheMLActivities.addActivities(new MLActivities("optimize", codif, "not increase model: " + p, loop, 0, true));
                    });


                } else if (ref == null) {
                    /* empty ref so improve scoring yes*/
                    mls.setScoring(true);
                    stocksMap.put(mls.getCodif(), mls);
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
     *  Genetic optimization
     * @param codif
     */
    void optimizeGenetic(String codif) {

        final ModelType type = ModelType.get(forecastDTO.getModelType());
        final int loop = forecastDTO.getInputLoop();


        for (PredictionPeriodicity p : PeriodicityList.periodicityLong) {
            //periodicity.forEach(p -> {
            log.info("start loop for period: " + p);
            final GeneticAlgorithm<Combination> algo = new GeneticAlgorithm<Combination>(c -> c.evaluate(codif,p,forecastDTO), () -> Combination.newInstance(),
                (first, second) -> first.merge(second), c -> c.mutate());
            algo.initialize(8);

            /*if model is empty*/
            if (this.stocksMap.get(codif) != null)
                algo.addReference(new Combination(this.stocksMap.get(codif).getModel(p).getValidator(type).clone()));
            algo.iterate(loop, 2, 6, 4, 0);
            //algo.printTo(System.err);


            /*Validate result */
            final MLStocks mls = MLStocks.newStock(codif, forecastDTO);
            MLStocks ref = this.stocksMap.get(mls.getCodif());
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
                this.stocksMap.put(mls.getCodif(), mls);
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
     *
    static int NOTUSE = 0;
    private void optimizeModel(String codif, ModelType type) {

        /**
         * to have some results faster, 2 iterations loop
         *

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

                /* period have no importance, iteration are same for all period*
                int col = mls.getModel(PredictionPeriodicity.D20).getValidator(type).getCol() -1;

                if (ref != null) {

                    final MLStocks finalRef = ref;
                    PeriodicityList.periodicityLong.forEach(p -> {
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

        /** keep best model*
        RandomForestStock rfs = new RandomForestStock();
        MLStocks bestModel = rfs.processRFRef(codif, validatorModel, true);
        bestModel.getStatus(type).calculeAvgPrd();

        final MLStocks mlsCache = CacheMLStock.getMLStockCache().get(codif);
        PeriodicityList.periodicityLong.forEach(p -> {
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

    public static void updatePredictor(StockHistory sg, boolean bRanking) {
        MLPredictor predictor = new MLPredictor();

        StockPrediction p = predictor.prediction(sg.getCodif(), sg.getValue());
        if (p != null) {
            sg.setPrediction(p);
        }


        StockPrediction pShort = predictor.predictionShort(sg.getCodif(), sg.getValue());
        if (pShort != null) {
            sg.setPredictionShort(pShort);
        }

        if (bRanking) {
            StockPrediction pRank = predictor.predictionRank(sg.getCodif());

            if (pRank != null)
                sg.setPerformanceEstimate((pRank.getYieldD20() + p.getYieldD20() + pShort.getYieldD20())/3);
            else
                sg.setPerformanceEstimate(0.);

        }




    }

}

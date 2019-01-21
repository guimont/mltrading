package com.mltrading.ml;


import com.mltrading.config.MLProperties;
import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.ml.model.ModelType;
import com.mltrading.ml.ranking.MLRank;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockSector;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.util.MLActivities;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by gmo on 15/12/2015.
 */
public class CacheMLStock {
    //


    public static String dbNameModel = "modelNote2";
    public static String dbNameModelPerf = "modelPerf";

    public static String dbNameModelShort = "modelNoteShort";
    public static String dbNameModelShortPerf = "modelShortPerf";

    public static final List<ModelType> modelTypes = Arrays.asList(ModelType.RANDOMFOREST, ModelType.GRADIANTBOOSTTREE);


    public static String SPARK_DEFAULT_MEMORY = "4g";

    public static int RENDERING_SHORT = 90;
    public static int RENDERING = 300;
    public static int RANGE_MAX = 1500;
    public static int RANGE_MIN = 300;

    public static String NO_EXTENDED ="";
    public static String SHORT_EXTENDED ="SH";

    private static final Logger log = LoggerFactory.getLogger(CacheMLStock.class);

    private static final Map<String, MLStocks> mlStockMap;

    private static final Map<String, MLStocks> mlStockShortMap;


    private static  MLRank mlRank = null;

    static {
        System.setProperty("hadoop.home.dir", MLProperties.getProperty("spark.hadoop.path"));
        System.setProperty("spark.sql.warehouse.dir", MLProperties.getProperty("spark.warehouse"));
        mlStockMap = new HashMap<>();
        mlStockShortMap = new HashMap<>();
    }

    //static SparkConf sparkConf = new SparkConf().setAppName("JavaRandomForest").setMaster("local[*]");
    static String url = "spark://" + MLProperties.getProperty("spark.master") + ":" +
        MLProperties.getProperty("spark.master.port");
    static SparkConf sparkConf = new SparkConf()/*.setSparkHome("file:///C:/temp")*/.setAppName("JavaRandomForest")
        .set("spark.driver.maxResultSize",MLProperties.getProperty("spark.driver.maxResultSize", SPARK_DEFAULT_MEMORY))
        .set("spark.driver.memory",MLProperties.getProperty("spark.driver.memory",SPARK_DEFAULT_MEMORY))
        .set("spark.executor.memory",MLProperties.getProperty("spark.executor.memory",SPARK_DEFAULT_MEMORY))

        .setMaster(url).setJars(new String[]{MLProperties.getProperty("spark.jars")});

    static JavaSparkContext sc = new JavaSparkContext(sparkConf);



    /**
     * load model and MLStocks map to apply model
     */
    public static void load() {

        /* trace */
        MLActivities g = new MLActivities("CacheMLStock", "", "load", 0, 0, false);

        CacheMLActivities.addActivities(g);

        deleteDB();
        //SynchWorker.delete();
        // load model on worker
        //SynchWorker.load();

        mlRank = new MLRank();
        mlRank.loadModel();

        load(new ArrayList(CacheStockSector.getSectorCache().values()));
        load(new ArrayList(CacheStockGeneral.getIsinCache().values()));

        CacheMLActivities.addActivities(g.setEndDate());
    }


    /**
     * load model for each stock
     * @param sl stock list (px1 or sector)
     */
    public static void load(List<? extends StockHistory> sl) {



        for (StockHistory s : sl) {
            MLActivities a = new MLActivities("CacheMLStock", "", "load", 0, 0, false);
            try {


                MLStocks mls = new MLStocksBase(s.getCodif());
                modelTypes.forEach(mls::distibute);

                modelTypes.forEach( t -> {
                    mls.getStatus(t).loadPerf(s.getCodif(),t, mls.getDbNamePerf(), CacheMLStock.RENDERING);
                    mls.load(t);
                });

                mls.getStatus(ModelType.ENSEMBLE).loadPerf(s.getCodif(), ModelType.ENSEMBLE, mls.getDbNamePerf(), CacheMLStock.RENDERING);

                mlStockMap.put(s.getCodif(), mls);



                /**Short model***/
                MLStocks mlsShort = new MLStocksShort(s.getCodif());
                //ModelType t = ModelType.RANDOMFOREST;
                modelTypes.forEach(mlsShort::distibute);
                //mlsShort.distibute(t);
                modelTypes.forEach( t -> {
                    mlsShort.getStatus(t).loadPerf(s.getCodif(),t, mlsShort.getDbNamePerf(), CacheMLStock.RENDERING_SHORT);
                    mlsShort.load(t);
                });
                mlsShort.getStatus(ModelType.ENSEMBLE).loadPerf(s.getCodif(), ModelType.ENSEMBLE, mls.getDbNamePerf(), CacheMLStock.RENDERING_SHORT);
                mlStockShortMap.put(s.getCodif(), mlsShort);
                CacheMLActivities.addActivities(a.setEndDate().setStatus("Success"));



            } catch (Exception e) {
                log.error(e.toString());
                CacheMLActivities.addActivities(a.setEndDate().setStatus("Failed"));
            }
        }

    }


    public static Map<String, MLStocks> getMLStockCache() {
        return mlStockMap;
    }

    public static Map<String, MLStocks> getMLStockShortCache() {
        return mlStockShortMap;
    }

    public static MLRank getMlRankCache() {
        return mlRank;
    }

    public static void setMlRank(MLRank mlRank) {
        CacheMLStock.mlRank = mlRank;
    }

    public static JavaSparkContext getJavaSparkContext() {
        return sc;
    }



    public static void save(ModelType type) {
        deleteModel(CacheMLStock.dbNameModel, CacheMLStock.dbNameModelPerf);
        //SynchWorker.delete(); don't use this not works
        saveCommon(type, mlStockMap);
    }

    public static void saveShort(ModelType type) {

        deleteModel(CacheMLStock.dbNameModelShort, CacheMLStock.dbNameModelShortPerf);
        //SynchWorker.delete();
        saveCommon(type, mlStockShortMap );
    }


    private static void saveCommon(ModelType type,  Map<String, MLStocks> map) {

        if (map.values().isEmpty()) return;

        for (MLStocks mls : map.values()) {
            PeriodicityList.periodicityLong.stream().filter(p -> mls.getSock(p).isModelImprove()).forEach(p -> {
                mls.saveModel(p, type);
                mls.saveDB(p, type);
            });

            try {
                /* saveValidator Validator each time because old validator is deleted*/
                modelTypes.stream().filter(t -> mls.getStatus(t).getPerfList() != null).forEach(t -> {
                    try {
                        log.info("Save perf: " + mls.getCodif() + " for model: " + t);
                        mls.getStatus(t).savePerf(mls.getCodif(), t, mls.getDbNamePerf());
                        log.info("Save validator: " + mls.getCodif() + " for model: " + t);
                        mls.saveValidator(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                if (mls.getStatus(ModelType.ENSEMBLE).getPerfList() != null) {
                    mls.getStatus(ModelType.ENSEMBLE).savePerf(mls.getCodif(), ModelType.ENSEMBLE, mls.getDbNamePerf());
                }
            }
            catch (Exception e) {
                log.error("Save failed: " + e);
            }

        }

        //SynchWorker.save();  don't use this not works
    }


    /**
     * update perf list
     */
    public static void savePerf(ModelType type) {

        for (MLStocks mls : mlStockMap.values()) {
            try {
                mls.getStatus(type).savePerf(mls.getCodif(),type, mls.getDbNamePerf());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * update perf list with last value
     */
    @SuppressWarnings("unused")
    @Deprecated
    public static void saveLastPerf(ModelType type) {
        for (MLStocks mls : mlStockMap.values()) {
            try {
                mls.getStatus(type).saveLastPerf(mls.getCodif(),type, mls.getDbNamePerf());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * delete model directory from local storage
     */
    public static void deleteDB() {
        String path = MLStock.path;

        try {
            FileUtils.deleteDirectory(new File(path + "model"));
        } catch (IOException e) {
            log.error("Cannot remove folder model: " + e);
        }
    }


    /**
     * delete model directory from local storage and model form influx db perf and validator
     * @param modelDBName
     * @param perfDBName
     */
    private static void deleteModel(String modelDBName, String perfDBName) {
        String path = MLStock.path;

        try {
            FileUtils.deleteDirectory(new File(path + "model"));
            InfluxDaoConnector.deleteDB(modelDBName);
            InfluxDaoConnector.deleteDB(perfDBName);
        } catch (IOException e) {
            log.error("Cannot remove folder model: " + e);
        }
    }


    /**
     * Tricks to bypass Object design not efficiency
     * @param prefix
     * @return db validator name (short or long)
     */
    public static String guessDbName(String prefix) {
        if (prefix.equalsIgnoreCase(CacheMLStock.NO_EXTENDED)) return CacheMLStock.dbNameModel;
        if (prefix.equalsIgnoreCase(CacheMLStock.SHORT_EXTENDED)) return CacheMLStock.dbNameModelShort;

        return null;

    }


    /**
     *
     * sync server function
     * not use in main jar
     * never work fine ... so dont watse time on this for moment
     */



    /*
        @SuppressWarnings("unused")
    public static void saveDB(ModelType type) {
        List<StockGeneral> sl = new ArrayList(CacheStockGeneral.getIsinCache().values());
        for (StockGeneral s : sl) {
            MLStocks mls = new MLStocks(s.getCodif());
            mls.saveDB(type);
        }

        List<StockSector> ss = new ArrayList(CacheStockSector.getSectorCache().values());
        for (StockSector s : ss) {
            MLStocks mls = new MLStocks(s.getCodif());
            mls.saveDB(type);
        }
    }

    @SuppressWarnings("unused")
    public static void loadDB() {
        List<StockGeneral> sl = new ArrayList(CacheStockGeneral.getIsinCache().values());
        for (StockGeneral s : sl) {
            MLStocks mls = new MLStocks(s.getCodif());
            modelTypes.forEach(t -> {
                mls.loadDB(t);
            });

        }

        List<StockSector> ss = new ArrayList(CacheStockSector.getSectorCache().values());
        for (StockSector s : ss) {
            MLStocks mls = new MLStocks(s.getCodif());
            modelTypes.forEach(t -> {
                mls.loadDB(t);
            });

        }
    }*/


}

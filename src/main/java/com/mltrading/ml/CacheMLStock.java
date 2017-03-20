package com.mltrading.ml;


import com.mltrading.config.MLProperties;
import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockSector;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.util.MLActivities;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gmo on 15/12/2015.
 */
public class CacheMLStock {
    //

    public static String SPARK_DEFAULT_MEMORY = "4g";

    public static int RENDERING = 300;
    public static int RANGE_MAX = 1500;

    private static final Logger log = LoggerFactory.getLogger(CacheMLStock.class);

    private static final Map<String, MLStocks> mlStockMap;

    static {
        System.setProperty("hadoop.home.dir", MLProperties.getProperty("spark.hadoop.path"));
        System.setProperty("spark.sql.warehouse.dir", MLProperties.getProperty("spark.warehouse"));
        mlStockMap = new HashMap<>();
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
        SynchWorker.delete();
        // load model on worker
        SynchWorker.load();

        load(new ArrayList(CacheStockGeneral.getIsinCache().values()));
        load(new ArrayList(CacheStockSector.getSectorCache().values()));

        CacheMLActivities.addActivities(g.setEndDate());
    }


    /**
     * load model for each stock
     * @param sl stock list (px1 or sector)
     */
    public static void load(List<? extends StockHistory> sl) {


        //load model local
        for (StockHistory s : sl) {
            MLStocks mls = new MLStocks(s.getCodif());
            mls.distibute();
        }


        for (StockHistory s : sl) {
            MLActivities a = new MLActivities("CacheMLStock", "", "load", 0, 0, false);
            try {

                MLStocks mls = new MLStocks(s.getCodif());
                mls.load();
                mls.getStatus().loadPerf(s.getCodif());
                mlStockMap.put(s.getCodif(), mls);
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

    public static JavaSparkContext getJavaSparkContext() {
        return sc;
    }


    public static void save() {
        deleteModel();
        SynchWorker.delete();

        for (MLStocks mls : mlStockMap.values()) {
            PeriodicityList.periodicity.forEach(p -> {
                if (mls.getSock(p).isModelImprove() == true) {
                    mls.saveModel(p);
                    mls.saveDB(p);
                }
            });
            /* saveValidator Validator each time because old validator is deleted*/
            mls.getStatus().savePerf(mls.getCodif());
            mls.saveValidator();

        }

        SynchWorker.save();
    }

    /**
     * update perf list with last value
     */
    public static void savePerf() {
        for (MLStocks mls : mlStockMap.values()) {
            mls.getStatus().saveLastPerf(mls.getCodif());
        }
    }


    public static void deleteDB() {
        String path = MLStock.path;

        try {
            FileUtils.deleteDirectory(new File(path + "model"));
        } catch (IOException e) {
            log.error("Cannot remove folder model: " + e);
        }
    }


    public static void deleteModel() {
        String path = MLStock.path;

        try {
            FileUtils.deleteDirectory(new File(path + "model"));
            InfluxDaoConnector.deleteDB(MatrixValidator.dbNameModel);
        } catch (IOException e) {
            log.error("Cannot remove folder model: " + e);
        }
    }



    /**
     *
     * sync server function
     * not use in main jar
     *
     */


    public static void saveDB() {
        List<StockGeneral> sl = new ArrayList(CacheStockGeneral.getIsinCache().values());
        for (StockGeneral s : sl) {
            MLStocks mls = new MLStocks(s.getCodif());
            mls.saveDB();
        }
    }


    public static void loadDB() {
        List<StockGeneral> sl = new ArrayList(CacheStockGeneral.getIsinCache().values());
        for (StockGeneral s : sl) {
            MLStocks mls = new MLStocks(s.getCodif());
            mls.loadDB();
        }
    }

}

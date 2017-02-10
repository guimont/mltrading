package com.mltrading.ml;


import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.util.MLActivities;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
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


    private static final Logger log = LoggerFactory.getLogger(CacheMLStock.class);

    private static final Map<String, MLStocks> mlStockMap;
    static {
        System.setProperty("hadoop.home.dir", "C:\\spark-2.0.0-bin-hadoop2.7\\");
        System.setProperty("spark.sql.warehouse.dir","file:///C:/temp");
        mlStockMap = new HashMap<>();
    }

    //static SparkConf sparkConf = new SparkConf().setAppName("JavaRandomForest").setMaster("local[*]");
    static SparkConf sparkConf = new SparkConf().setSparkHome("file:///C:/temp").setAppName("JavaRandomForest").setMaster("spark://NB120249:7077").setJars(new String[]{"target/com.mltrading-1.0-SNAPSHOT.jar"});
    static JavaSparkContext sc = new JavaSparkContext(sparkConf);





    public static void load(List<StockGeneral> sl) {

        MLActivities g = new MLActivities("CacheMLStock", "","load",0,0,false);

        CacheMLActivities.addActivities(g);

        deleteDB();
        SynchWorker.delete();
        // load model on worker
        SynchWorker.load();


        //load model local
        for (StockGeneral s : sl) {
            MLStocks mls = new MLStocks(s.getCodif());
            mls.distibute();
        }


        for (StockGeneral s : sl) {
            MLActivities a = new MLActivities("CacheMLStock", "","load",0,0,false);
            try {

                MLStocks mls = new MLStocks(s.getCodif());
                mls.load();
                mls.getStatus().loadPerf(s.getCodif());
                mlStockMap.put(s.getCodif(), mls);
                CacheMLActivities.addActivities(a.setEndDate().setStatus("Success"));

            }catch (Exception e) {
                log.error(e.toString());
                CacheMLActivities.addActivities(a.setEndDate().setStatus("Failed"));
            }
        }

        CacheMLActivities.addActivities(g.setEndDate());

    }


    public static Map<String,MLStocks> getMLStockCache() {
        return mlStockMap;
    }
    public static JavaSparkContext getJavaSparkContext() {
        return sc;
    }

    public static void saveDB() {
        List<StockGeneral> sl = new ArrayList(CacheStockGeneral.getIsinCache().values());
        for (StockGeneral s:sl) {
            MLStocks mls = new MLStocks(s.getCodif());
            mls.saveDB();
        }
    }


    public static void loadDB() {
        List<StockGeneral> sl = new ArrayList(CacheStockGeneral.getIsinCache().values());
        for (StockGeneral s:sl) {
            MLStocks mls = new MLStocks(s.getCodif());
            mls.loadDB();
        }
    }


    public static void save() {
        deleteModel();
        SynchWorker.delete();

        for (MLStocks mls:mlStockMap.values()) {
            mls.save();
            mls.saveDB();
            mls.getStatus().savePerf(mls.getCodif());
        }

        SynchWorker.save();
    }

    /**
     * update perf list with last value
     */
    public static void savePerf() {
        for (MLStocks mls:mlStockMap.values()) {
            mls.getStatus().saveLastPerf(mls.getCodif());
        }
    }


    public static void deleteDB() {
        String path="c:/";
        if (System.getProperty("os.name").contains("Windows"))
            path = "/";
        try {
            FileUtils.deleteDirectory(new File(path+"model"));
        } catch (IOException e) {
            log.error("Cannot remove folder model: " + e);
        }
    }


    public static void deleteModel() {
        String path="c:/";
        if (System.getProperty("os.name").contains("Windows"))
            path = "/";
        try {
            FileUtils.deleteDirectory(new File(path+"model"));
            InfluxDaoConnector.deleteDB(MatrixValidator.dbNameModel);
        } catch (IOException e) {
            log.error("Cannot remove folder model: " + e);
        }
    }


}

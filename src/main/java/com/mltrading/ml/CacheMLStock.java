package com.mltrading.ml;


import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.models.stock.StockGeneral;
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

        //JavaRDD<StockGeneral> data = sc.parallelize(sl);

        deleteModel();

        for (StockGeneral s : sl) {
            MLStocks mls = new MLStocks(s.getCodif());
            mls.distibute();
            //mls.send(sc);
        }


        //to test
        /*List<MLStocks>  resMLLoad= data.map(
            new Function<StockGeneral, MLStocks>() {
                public MLStocks call(StockGeneral s) {
                    try {
                        MLStocks mls = new MLStocks(s.getCodif());
                        mls.load();
                        return mls;
                    } catch (Exception e) {
                        log.error(e.toString());
                        return null;
                    }
                }
            }
        ).collect();


        for (MLStocks mls:resMLLoad) {
            mls.getStatus().loadPerf(mls.getCodif());
            mlStockMap.put(mls.getCodif(), mls);
        }*/


        /*
        List<MLStocks>  resMLLoad= data.map(
            new Function<StockGeneral, MLStocks>() {
                public MLStocks call(StockGeneral s) {
                    try {
                        MLStocks mls = new MLStocks(s.getCodif());
                        mls.distibute();
                        return mls;
                    } catch (Exception e) {
                        log.error(e.toString());
                        return null;
                    }
                }
            }
        ).collect();


        if (resMLLoad != null) {
            for (MLStocks mls : resMLLoad) {

                System.out.println(mls.getCodif());
            }
        }*/


        for (StockGeneral s : sl) {
            try {
                MLStocks mls = new MLStocks(s.getCodif());
                mls.load();
                mls.getStatus().loadPerf(s.getCodif());
                mlStockMap.put(s.getCodif(), mls);

            }catch (Exception e) {
                log.error(e.toString());
            }
        }

    }


    public static Map<String,MLStocks> getMLStockCache() {
        return mlStockMap;
    }
    public static JavaSparkContext getJavaSparkContext() {
        return sc;
    }

    public static void saveDB() {
        for (MLStocks mls:mlStockMap.values()) {
            mls.saveDB();
        }
    }


    public static void save() {
        deleteModel();
        for (MLStocks mls:mlStockMap.values()) {
            mls.save();
            mls.getStatus().savePerf(mls.getCodif());
        }
    }

    /**
     * update perf list with last value
     */
    public static void savePerf() {
        for (MLStocks mls:mlStockMap.values()) {
            mls.getStatus().saveLastPerf(mls.getCodif());
        }
    }


    public static void deleteModel() {
        try {
            FileUtils.deleteDirectory(new File("model"));
            InfluxDaoConnector.deleteDB(MatrixValidator.dbNameModel);
        } catch (IOException e) {
            log.error("Cannot remove folder model: " + e);
        }
    }


}

package com.mltrading.ml;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gmo on 15/12/2015.
 */
public class CacheMLStock {
    static SparkConf sparkConf = new SparkConf().setAppName("JavaRandomForest").setMaster("local[*]");
    static JavaSparkContext sc = new JavaSparkContext(sparkConf);

    private static final Map<String, MLStocks> mlStockMap;
    static {
        System.setProperty("hadoop.home.dir", "C:\\spark-1.6.0-bin-hadoop2.6\\");
        mlStockMap = new HashMap<>();
    }


    public static Map<String,MLStocks> getMLStockCache() {
        return mlStockMap;
    }
    public static JavaSparkContext getJavaSparkContext() {
        return sc;
    }

}

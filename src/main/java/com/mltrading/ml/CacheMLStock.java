package com.mltrading.ml;


import com.mltrading.models.stock.Stock;
import com.mltrading.models.stock.StockGeneral;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gmo on 15/12/2015.
 */
public class CacheMLStock {
    static SparkConf sparkConf = new SparkConf().setAppName("JavaRandomForest").setMaster("local[*]");
    static JavaSparkContext sc = new JavaSparkContext(sparkConf);

    private static final Logger log = LoggerFactory.getLogger(CacheMLStock.class);

    private static final Map<String, MLStocks> mlStockMap;
    static {
        System.setProperty("hadoop.home.dir", "C:\\spark-1.6.0-bin-hadoop2.6\\");
        mlStockMap = new HashMap<>();
    }

    public static void load(List<StockGeneral> sl) {
        for (StockGeneral s : sl) {
            try {
                MLStocks mls = new MLStocks(s.getCodif());
                mls.getMlD1().loadModel();
                mls.getMlD5().loadModel();
                mls.getMlD20().loadModel();

                mls.getMlD1().getValidator().loadValidator(s.getCodif() + "VD1");
                mls.getMlD5().getValidator().loadValidator(s.getCodif() + "VD5");
                mls.getMlD20().getValidator().loadValidator(s.getCodif() + "VD20");

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

    public static void save() {
        for (MLStocks mls:mlStockMap.values()) {
            if (mls != null && mls.getMlD1() != null) mls.getMlD1().save();
            if (mls != null && mls.getMlD5() != null) mls.getMlD5().save();
            if (mls != null && mls.getMlD20() != null) mls.getMlD20().save();
        }
    }
}

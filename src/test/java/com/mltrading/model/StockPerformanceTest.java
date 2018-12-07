package com.mltrading.model;

import com.mltrading.config.MLProperties;
import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MLStocks;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockPerformance;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import org.junit.Test;

/**
 * Created by gmo on 17/03/2017.
 */
public class StockPerformanceTest {

    @Test
    public void testLoad() {
        MLProperties.load();

        for (StockHistory sh :CacheStockGeneral.getIsinCache().values()) {
            System.out.println(sh.getCodif());
        }

        /*load all model to make this operation .. hard to test in unit test
        /*for (StockHistory sh :CacheStockGeneral.getIsinCache().values()) {
            MLStocks mls = new MLStocks(sh.getCodif());
            mls.loadValidator();
            CacheMLStock.getMLStockCache().put(sh.getCodif(), mls);

            StockPerformance sp = new StockPerformance();
            sp.load(sh.getCodif());
        }*/



    }

}

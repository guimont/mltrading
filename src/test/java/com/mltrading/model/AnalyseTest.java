package com.mltrading.model;

import com.mltrading.config.MLProperties;
import com.mltrading.models.parser.Analyse;
import com.mltrading.models.stock.cache.CacheStockHistory;
import org.junit.Test;


public class AnalyseTest {


    @Test
    public void testAnalyse() {
        try {
            MLProperties.load();
            CacheStockHistory cache = CacheStockHistory.CacheStockHistoryHolder();
            Analyse a = new Analyse();
            a.processAnalysisAll("GYRI1Y");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}

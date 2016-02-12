package com.mltrading.ml;

import com.google.inject.Inject;
import com.mltrading.models.stock.Stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gmo on 08/01/2016.
 */
@Service
public class MlForecast {

    private static final Logger log = LoggerFactory.getLogger(MlForecast.class);

    public void processList(List<Stock> l) {

        for (Stock s : l) {
            RandomForestStock rfs = new RandomForestStock();
            MLStocks mls = rfs.processRF(s);

            if (null != mls) {
                mls.calculeAvgPrd();
                CacheMLStock.getMLStockCache().put(mls.getCodif(),mls);
            }

        }

        //check

        log.info("result mlf size: " + CacheMLStock.getMLStockCache().size());

        for (MLStocks mls:CacheMLStock.getMLStockCache().values()) {
            log.info("perf list result size: " + mls.getPerfList().size());
            //log.info("test data count: " + mls.getTestData().count()); too verbous spark log for count function
        }

    }

}

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
            MLStocks mls = rfs.processRF(s, true , false);

            if (null != mls) {
                mls.getStatus().calculeAvgPrd();
                MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());

                if (ref != null) {
                    if (mls.getStatus().getErrorRateD1() < ref.getStatus().getErrorRateD1()) {
                        //TODO dont replace all
                        CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                    }
                } else {
                    CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                }
            }

        }

        //check

        log.info("result mlf size: " + CacheMLStock.getMLStockCache().size());

        for (MLStocks mls:CacheMLStock.getMLStockCache().values()) {
            log.info("perf list result size: " + mls.getStatus().getPerfList().size());
            //log.info("test data count: " + mls.getTestData().count()); too verbous spark log for count function
        }

    }


    public void optimize(Stock s, int loop) {

        for (int i = 0; i< loop; i++) {
            RandomForestStock rfs = new RandomForestStock();
            MLStocks mls = rfs.processRF(s, true, false);

            if (null != mls) {
                mls.getStatus().calculeAvgPrd();
                mls.getMlD1().getValidator().save(mls.getCodif(), mls.getStatus().getErrorRateD1());
                MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());

                if (ref != null) {
                    if (mls.getStatus().getErrorRateD1() < ref.getStatus().getErrorRateD1()) {
                        //TODO dont replace all
                        CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                    }
                } else {
                    CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                }
            }
        }
    }

    public void optimizeFeature(Stock s, int loop) {

        for (int i = 0; i< loop; i++) {
            RandomForestStock rfs = new RandomForestStock();
            MLStocks mls = rfs.processRF(s, false, true);

            if (null != mls) {
                mls.getStatus().calculeAvgPrd();
                mls.getMlD1().getValidator().save(mls.getCodif(), mls.getStatus().getErrorRateD1());
                MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());

                if (ref != null) {
                    if (mls.getStatus().getErrorRateD1() < ref.getStatus().getErrorRateD1()) {
                        //TODO dont replace all
                        CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                    }
                } else {
                    CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                }
            }
        }
    }
}

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
                mls.getMlD1().getValidator().save(mls.getCodif(), mls.getStatus().getErrorRateD1(), mls.getStatus().getAvgD1());
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

        /**
         * to have some results faster, 2 iterations loop
         */
        for (int i = 0; i< loop; i++) {
            for (int j = 0; j < loop; j++) {
                RandomForestStock rfs = new RandomForestStock();
                MLStocks mls = rfs.processRF(s, false, true);

                if (null != mls) {
                    mls.getStatus().calculeAvgPrd();
                    mls.getMlD1().getValidator().save(mls.getCodif() + "VD1", mls.getStatus().getErrorRateD1(), mls.getStatus().getAvgD1());
                    mls.getMlD5().getValidator().save(mls.getCodif() + "VD5", mls.getStatus().getErrorRateD5(), mls.getStatus().getAvgD5());
                    mls.getMlD20().getValidator().save(mls.getCodif() + "VD20", mls.getStatus().getErrorRateD20(), mls.getStatus().getAvgD20());
                    MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());

                    if (ref != null) {
                        /**for 1 day prevision*/
                        if (mls.getStatus().getErrorRateD1() <= ref.getStatus().getErrorRateD1() ||
                            (mls.getStatus().getErrorRateD1() == ref.getStatus().getErrorRateD1() &&
                                mls.getStatus().getAvgD1() < ref.getStatus().getAvgD1())) {
                                ref.getMlD1().setValidator(mls.getMlD1().getValidator());
                                ref.getMlD1().setModel(mls.getMlD1().getModel());
                                ref.getStatus().setAvgD1(mls.getStatus().getAvgD1());
                                ref.getStatus().setErrorRateD1(mls.getStatus().getErrorRateD1());

                                try {
                                    ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), 1);
                                } catch (Exception e) {
                                }
                            }


                        /**for 5 day prevision*/
                        if (mls.getStatus().getErrorRateD5() < ref.getStatus().getErrorRateD5() ||
                            (mls.getStatus().getErrorRateD5() == ref.getStatus().getErrorRateD5() &&
                                mls.getStatus().getAvgD5() < ref.getStatus().getAvgD5())) {
                                ref.getMlD5().setValidator(mls.getMlD5().getValidator());
                                ref.getMlD5().setModel(mls.getMlD5().getModel());
                                ref.getStatus().setAvgD5(mls.getStatus().getAvgD5());
                                ref.getStatus().setErrorRateD5(mls.getStatus().getErrorRateD5());
                                try {
                                    ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), 5);
                                } catch (Exception e) {
                                    log.error("Cannot replace element for period 5 days" + e);
                                }
                            }


                        /**for 20 day prevision*/
                        if (mls.getStatus().getErrorRateD20() < ref.getStatus().getErrorRateD20() ||
                            (mls.getStatus().getErrorRateD20() == ref.getStatus().getErrorRateD20() &&
                                mls.getStatus().getAvgD20() < ref.getStatus().getAvgD20())) {
                                ref.getMlD20().setValidator(mls.getMlD20().getValidator());
                                ref.getMlD20().setModel(mls.getMlD20().getModel());
                                ref.getStatus().setAvgD20(mls.getStatus().getAvgD20());
                                ref.getStatus().setErrorRateD20(mls.getStatus().getErrorRateD20());
                                try {
                                    ref.getStatus().replaceElementList(mls.getStatus().getPerfList(), 20);
                                } catch (Exception e) {
                                    log.error("Cannot replace element for period 20 days" + e);
                                }
                            }

                    } else {
                        CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                    }
                }
            }
        }
    }
}

package com.mltrading.ml;

import com.google.inject.Inject;
import com.mltrading.models.stock.Stock;

import com.mltrading.models.stock.StockGeneral;
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

    public void processList(List<StockGeneral> l) {

        for (StockGeneral s : l) {
            RandomForestStock rfs = new RandomForestStock();
            MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
            if (mls != null) {
                mls = rfs.processRFResult(s, mls);
                mls.getStatus().calculeAvgPrd();
            }
        }

        //check

        log.info("result mlf size: " + CacheMLStock.getMLStockCache().size());

        for (MLStocks mls:CacheMLStock.getMLStockCache().values()) {
            log.info("perf list result size: " + mls.getStatus().getPerfList().size());
        }

    }


    public static enum Method {
        RandomForest,
        LinearRegression
    }

    public static enum Type {
        Feature,
        None, RF
    }

    public void optimize(StockGeneral s, int loop, Method method, Type type) {

        /**
         * to have some results faster, 2 iterations loop
         */

        for (int i = 0; i< loop; i++) {


            MLStocks mls = new MLStocks(s.getCodif());

            if (type == Type.Feature ) {
                mls.getMlD1().getValidator().generateFeature();
                mls.getMlD5().setValidator(mls.getMlD1().getValidator().clone());
                mls.getMlD20().setValidator(mls.getMlD1().getValidator().clone());
            }

            if (type == Type.RF ) {
                mls.getMlD1().getValidator().numTrees = 20 + i*40;
                mls.getMlD5().setValidator(mls.getMlD1().getValidator().clone());
                mls.getMlD20().setValidator(mls.getMlD1().getValidator().clone());
            }


            String saveCode;
            if (method == Method.LinearRegression) {
                LinearRegressionStock rfs = new LinearRegressionStock();
                mls = rfs.processRF(s, mls);
                saveCode = "L";
            } else {
                RandomForestStock rfs = new RandomForestStock();
                mls = rfs.processRF(s, mls);
                saveCode = "V";
            }

            if (null != mls) {
                mls.getStatus().calculeAvgPrd();
                mls.getMlD1().getValidator().save(mls.getCodif() + saveCode + "D1", mls.getStatus().getErrorRateD1(), mls.getStatus().getAvgD1());
                mls.getMlD5().getValidator().save(mls.getCodif() + saveCode + "D5", mls.getStatus().getErrorRateD5(), mls.getStatus().getAvgD5());
                mls.getMlD20().getValidator().save(mls.getCodif() + saveCode + "D20", mls.getStatus().getErrorRateD20(), mls.getStatus().getAvgD20());
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

package com.mltrading.ml.util;

import com.mltrading.ml.*;
import com.mltrading.ml.model.RandomForestStock;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockSector;
import com.mltrading.models.util.MLActivities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Created by gmo on 15/06/2017.
 */




public class Combination extends Evaluate{

    private static final Random random = new Random(0);
    private static final Logger log = LoggerFactory.getLogger(Combination.class);

    MatrixValidator mv;

    public static Combination newInstance() {
        return new Combination();
    }


    public Combination(MatrixValidator mv) {
        this.mv = mv;
    }

    public Combination() {
        mv = new MatrixValidator();
        mv.generateRandomModel(0);
    }

    static int SCORENOTREACHABLE =  500;
    public int evaluate(String codif, PredictionPeriodicity p) {

        final MLStocks mls = new MLStocks(codif);

        CacheMLActivities.addActivities(new MLActivities("optimize", codif, "start", 0, 0, false));
        MLStocks ref = CacheMLStock.getMLStockCache().get(mls.getCodif());


       mls.setValidators(mv);


        String saveCode;
        {
            RandomForestStock rfs = new RandomForestStock();
            rfs.processRFRef(codif, mls, false);
            saveCode = "V";
        }

        if (null != mls) {
            mls.getStatus().calculeAvgPrd();

            mls.getValidator(p).save(mls.getCodif() + saveCode +
                p, mls.getStatus().getErrorRate(p), mls.getStatus().getAvg(p));



            if (ref != null) {


                    if ( MlForecast.checkResult(mls, ref, p))
                        CacheMLActivities.addActivities(new MLActivities("optimize", codif, "increase model: " + p, 0, 1, true));
                    else
                        CacheMLActivities.addActivities(new MLActivities("optimize", codif, "not increase model: " + p, 0, 0, true));



            } else {
                    /* empty ref so improve scoring yes*/
                mls.setScoring(true);
                CacheMLStock.getMLStockCache().put(mls.getCodif(), mls);
                CacheMLActivities.addActivities(new MLActivities("optimize", codif, "empty model", 0, 0, true));
            }
        } else {
            CacheMLActivities.addActivities(new MLActivities("optimize", codif, "failed", 0, 0, true));
        }

        //inverse score
        return SCORENOTREACHABLE - mls.getStatus().getErrorRate(p);
    }



    public Combination merge(Combination other) {
        this.mv.merge(other.mv);
        return new Combination(this.mv);
    }

    public Combination mutate() {
        return new Combination();
    }



}

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


    public MatrixValidator getMv() {
        return mv;
    }

    public void setMv(MatrixValidator mv) {
        this.mv = mv;
    }

    public Combination(MatrixValidator mv) {
        this.mv = mv;
    }

    public Combination() {
        mv = new MatrixValidator();
        mv.generateRandomModel(0);
    }

    static int SCORENOTREACHABLE =  500;
    public double evaluate(String codif, PredictionPeriodicity p) {

        final MLStocks mls = new MLStocks(codif);

        CacheMLActivities.addActivities(new MLActivities("optimize", codif, "start", 0, 0, false));

        mls.setValidator(p,mv);

        String saveCode;
        {
            RandomForestStock rfs = new RandomForestStock();
            rfs.processRFRef(codif, mls, false,p);
            saveCode = "V";
        }

        if (null != mls) {
            mls.getStatus().calculeAvgPrd();

            mls.getValidator(p).save(mls.getCodif() + saveCode +
                p, mls.getStatus().getErrorRate(p), mls.getStatus().getAvg(p));

        } else {
            CacheMLActivities.addActivities(new MLActivities("optimize", codif, "failed", 0, 0, true));
        }

        //inverse score
        return SCORENOTREACHABLE - convert(mls.getStatus().getErrorRate(p),mls.getStatus().getAvg(p));
    }


    private double convert(int error, double stdDev) {
        int std = Math.abs((int) Math.rint(stdDev*1000));
        String convert = error+"."+std;
        return new Double(convert);
    }



    public Combination merge(Combination other) {
        this.mv.merge(other.mv);
        return new Combination(this.mv);
    }

    public Combination mutate() {
        return new Combination();
    }



}

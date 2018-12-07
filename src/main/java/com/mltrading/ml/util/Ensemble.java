package com.mltrading.ml.util;

import com.mltrading.ml.*;
import com.mltrading.ml.model.GradiantBoostStock;
import com.mltrading.ml.model.ModelType;
import com.mltrading.ml.model.RandomForestStock;
import com.mltrading.models.util.MLActivities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by gmo on 15/06/2017.
 */




public class Ensemble {

    private static final Random random = new Random(0);
    private static final Logger log = LoggerFactory.getLogger(Ensemble.class);

    Double rangeList[] = {0., 0.1, 0.25 ,0.35 , 0.5, 0.65, 0.75, 1. , 1.25 ,1.35, 1.5, 1.75, 10., 1000.};

    Double ratio;

    public static Ensemble newInstance() {
        return new Ensemble();
    }


    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Ensemble(Double ratio) {
        this.ratio = ratio;
    }

    public Ensemble() {
        Random generator = new Random();
        int index = generator.nextInt(13);
        this.ratio = rangeList[index];
    }

    static int SCORENOTREACHABLE =  500;
    static int BADSCORE =  0;
    public double evaluate(MLStocks mls) {

        List<MLPerformances> mlPerformancesRF =  mls.getStatus(ModelType.RANDOMFOREST).getPerfList();
        List<MLPerformances> mlPerformancesGBT =  mls.getStatus(ModelType.GRADIANTBOOSTTREE).getPerfList();


        if (mlPerformancesRF .size() == 0 || mlPerformancesGBT.size()==0)
            return BADSCORE;

        int size = mlPerformancesRF .size();

        List<MLPerformances> listEnsemble = new ArrayList<>();

        for (int index = 0; index< size; index++){

            MLPerformances mlRF = mlPerformancesRF.get(index);
            MLPerformances mlGBT = mlPerformancesGBT.get(index);
            MLPerformances perf = new MLPerformances(mlRF.getDate());

            CacheMLStock.periodicity.forEach(p -> {
                MLPerformance mpRF = mlRF.getMl(p);
                MLPerformance mpGBT = mlGBT.getMl(p);


                perf.setMl(MLPerformance.calculYields(mlRF.getMl(p).getDate(),
                    (mpRF.getPrediction() * this.getRatio() + mpGBT.getPrediction())/(1. + this.ratio),
                    mpRF.getRealvalue(), mpRF.getCurrentValue()), p);

            });
            listEnsemble.add(perf);




        }

        MLStatus checkStatus = new MLStatus();

        CacheMLStock.periodicity.forEach(p -> {
            try {
                checkStatus.setPerfList(listEnsemble, p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        checkStatus.calculeAvgPrd();


        //dont store result now
        //mls.getStatus(ModelType.ENSEMBLE).setPerfList(listEnsemble);

        //inverse score
        return SCORENOTREACHABLE - convert(checkStatus.getErrorRate(PredictionPeriodicity.D20),
            checkStatus.getAvg(PredictionPeriodicity.D20));
    }


    private double convert(int error, double stdDev) {
        int std = Math.abs((int) Math.rint(stdDev*1000));
        String convert = error+"."+std;
        return new Double(convert);
    }


    public Ensemble merge(Ensemble other) {

        return new Ensemble(other.getRatio());
    }

    public Ensemble mutate() {
        Random generator = new Random(111);
        return new Ensemble(generator.nextDouble()*10);
    }



}

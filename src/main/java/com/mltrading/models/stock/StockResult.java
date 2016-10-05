package com.mltrading.models.stock;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by gmo on 03/10/2016.
 */
public class StockResult {
    List<StockGeneral> top;
    List<StockGeneral> flop;
    List<StockGeneral> data;


    public static  StockResult generate() {
        StockResult res = new StockResult();
        res.data = new ArrayList<>(CacheStockGeneral.getCache().values());



        for (StockGeneral d: res.data) {

            double note = d.getPrediction().getConfidenceD20()*d.getPrediction().getConfidenceD5();
        }

        final Comparator<Double> comp = (p1, p2) -> Double.compare( p1, p2);

        double top = res.data.stream().map(s -> StockHistory.getStockHistoryLast(s.getCodif(),1).get(0).getConsensusNote()).max(comp).orElse(0.);


        return res;
    }

}

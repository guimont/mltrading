package com.mltrading.ml;



/* create rank for stock*/
/*for each stock => predict ranking */

import com.mltrading.ml.model.RandomForestRanking;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MLStockRanking {


    public static final int RANGE_MAX = 300;
    public static final int RENDERING = 100;

    public void optimize() {

        List<StockGeneral> l = new ArrayList(CacheStockGeneral.getIsinCache().values());
        for (StockGeneral s : l) {
            MLRank ref = CacheMLStock.getMlRankCache().get((s.getCodif()));
            RandomForestRanking rfr = new RandomForestRanking();
            MLRank mlr = new MLRank(s.getCodif());

            rfr.processRanking(s.getCode(), s.getCodif(), mlr);
            mlr.getStatus().calculeAvgPrd();

            if (ref != null) {
                if (compareResult(mlr.getStatus(), ref.getStatus(), PredictionPeriodicity.D20)) {
                    CacheMLStock.getMlRankCache().put(s.getCodif(), mlr);
                }
            }
            else {
                CacheMLStock.getMlRankCache().put(s.getCodif(), mlr);
            }
        }
    }






    /**
     * compare result
     *
     * @param mls
     * @param ref
     * @param period
     * @return
     */
    public static  boolean compareResult(MLStatus mls, MLStatus ref, PredictionPeriodicity period) {
        return mls.getErrorRate(period) <= ref.getErrorRate(period) ||
            (mls.getErrorRate(period) == ref.getErrorRate(period) &&
                mls.getAvg(period) < ref.getAvg(period));
    }

}

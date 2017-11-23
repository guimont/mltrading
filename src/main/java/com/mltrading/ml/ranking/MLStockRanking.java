package com.mltrading.ml.ranking;



/* create rank for stock*/
/*for each stock => predict ranking */

import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MLStatus;
import com.mltrading.ml.PredictionPeriodicity;
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

            MLRank ref = CacheMLStock.getMlRankCache();
            RandomForestRanking rfr = new RandomForestRanking();
            MLRank mlr = new MLRank();

            rfr.processRanking(l, mlr);
            mlr.getStatus().calculeAvgPrd();

            if (ref != null) {
                if (compareResult(mlr.getStatus(), ref.getStatus(), PredictionPeriodicity.D20)) {
                    CacheMLStock.setMlRankCache(mlr);
                }
            }
            else {
                CacheMLStock.setMlRankCache(mlr);
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

package com.mltrading.ml;

import com.google.inject.Inject;
import com.mltrading.models.stock.Stock;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gmo on 08/01/2016.
 */
@Service
public class MlForecast {



    public void processList(List<Stock> l) {

        for (Stock s : l) {
            RandomForestStock rfs = new RandomForestStock();
            MLStock mls = rfs.processRF(s);
            if (null != mls)
                CacheMLStock.getMLStockCache().put(mls.getCodif(),mls);
        }

    }

}

package com.mltrading.models.stock.cache;

import com.mltrading.models.stock.StockAnalyse;


import java.util.List;
import java.util.Map;

/**
 * Created by gmo on 09/03/2017.
 */
public class CacheStockAnalyse extends CacheStockTimeSeries<String, Integer,StockAnalyse> {


    final static CacheStockAnalyse instance = new CacheStockAnalyse();


    public static CacheStockAnalyse CacheStockAnalyseHolder() {
        /** Instance unique non pre-initialise */
        return instance;
    }



    private CacheStockAnalyse() {
        super();
    }


    /**
     * @param code
     * @param date
     * @return
     */
    public StockAnalyse getStockAnalyse(final String code, String date) {

        List<StockAnalyse> list = getInCache(code);
        Integer index = getInCache(list, code, date);

        return findElement(list, index);

    }



    /**
     * return index and filled cache if empty
     * @param code
     * @return
     */
    private  Integer getInCache(List<StockAnalyse> list , String code, String date) {
        Map<String, Integer> indexMap = this.indexCache.get(code);
        if (indexMap == null) fillCache(code);
        Integer res =  this.indexCache.get(code).get(date);
        if (res == null) {
            int curser = 0;
            while (curser  < list.size()) {
                if (list.get(curser).getDay().compareTo(date) < 0) curser ++;
                else {
                    res = curser;
                    break;
                }
            }
        }

        return res;
    }


    /**
     * fill cache
     * @param code
     * @return
     */
    @Override
    protected synchronized List<StockAnalyse> fillCache(final String code) {

        return timeSeries.extractAnalyse(code, this.indexCache, this.historyCache);
    }

}

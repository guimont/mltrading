package com.mltrading.models.stock.cache;

import com.mltrading.models.stock.StockAnalyse;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;


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

    @Override
    protected Integer apply(int curser) {
        return curser;
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
     * Use getStockHistory in memory
     * @param code
     * @param date
     * @return
     */
    public boolean isInStockAanlyse(final String code, String date) {
        return isInStockAanlyse(code,date,true);
    }


    /**
     * return true if date is in cache
     * @param code
     * @param date
     * @param inMemory
     * @return
     */
    public boolean isInStockAanlyse(final String code, String date, boolean inMemory) {

        if (inMemory == true) {
            Integer index = getExactlyInCache(code, date);
            return index!=null ? true : false;
        }
        else
            throw  new NotImplementedException();
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

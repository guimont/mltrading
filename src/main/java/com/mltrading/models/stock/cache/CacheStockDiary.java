package com.mltrading.models.stock.cache;

import com.mltrading.dao.TimeSeriesDao.DataSeriesDao;
import com.mltrading.dao.TimeSeriesDao.impl.DataSeriesDaoInfluxImpl;
import com.mltrading.models.stock.StockDocument;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CacheStockDiary extends CacheStockTimeSeries<String, Integer,StockDocument> {

    protected static DataSeriesDao dataSeries = null;


    @Override
    protected Integer apply(int cursor) {
        return cursor;
    }

    @Override
    protected List<StockDocument> fillCache(String code) {
        return dataSeries.extract(code,this.indexCache, this.historyCache);
    }

    private CacheStockDiary() {
        dataSeries = new DataSeriesDaoInfluxImpl();
    }

    final static CacheStockDiary instance = new CacheStockDiary();


    public static CacheStockDiary CacheStockHistoryHolder() {
        /** Instance unique non pre-initialise */
        return instance;
    }

    /**
     *  get count last element
     * @param code
     * @param count
     * @return
     */
    public List<StockDocument> getStockHistoryLastInvert(final String code, int count) {return getStockHistoryLastInvert(code, count, true);}


    /**
     * get count last element
     * @param code
     * @param count
     * @param inMemory
     * @return
     */
    public List<StockDocument> getStockHistoryLastInvert(final String code, int count, boolean inMemory) {
        if (inMemory == true) {
            List<StockDocument> list = getInCache(code);
            if (list == null || list.size()-count < 0) return null;
            List<StockDocument> subList= new CopyOnWriteArrayList(list.subList(list.size() - count, list.size()));
            Collections.sort(subList);
            return(subList);
        }
        else
            throw  new NotImplementedException();

    }


    public StockDocument getStockDocumentDayAfter(final String code, String date) {
        return getStockDocumentDayAfter(code,date,true);
    }

    /**
     * return element a day after
     * @param code
     * @param date
     * @param inMemory
     * @return
     */
    public StockDocument getStockDocumentDayAfter(final String code, String date, boolean inMemory) {
        if (inMemory == true) {

            List<StockDocument> list = getInCache(code);
            Integer index = getInCache(list, code, date);

            /* order is most recent to oldest*/
            return findElement(list, index+1);
        }
        else
            throw  new NotImplementedException();
    }


}

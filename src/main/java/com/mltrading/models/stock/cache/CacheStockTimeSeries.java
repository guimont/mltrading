package com.mltrading.models.stock.cache;



import com.mltrading.dao.TimeSeriesDao.TimeSeriesDao;
import com.mltrading.dao.TimeSeriesDao.impl.TimeSeriesDaoInfluxImpl;
import com.mltrading.models.stock.StockBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gmo on 09/03/2017.
 */
public abstract class CacheStockTimeSeries<K,V,W extends StockBase> {

    protected HashMap<K, Map<K,V>> indexCache = new HashMap<>();
    protected Map<K,List<W>> historyCache = new HashMap<>();

    protected static TimeSeriesDao timeSeries = null;
    protected CacheStockTimeSeries() {
        timeSeries = new TimeSeriesDaoInfluxImpl();
    }


    /**
     * Clear cache
     */
    public void clearCache() {
        indexCache.clear();
        historyCache.clear();
    }


    /**
     * find element for a date
     * @param list
     * @param index
     * @return
     */

    protected W findElement(List<W> list , Integer index) {
        if (list == null) return null;

        if (index != null)
            return index < list.size()  ? list.get(index) : null;

        return null;
    }

    /**
     * return index and filled cache if empty
     * @param code
     * @return
     */
    protected  V getInCache(List<W> list , K code, String date) {
        Map<K, V> indexMap = this.indexCache.get(code);
        if (indexMap == null) fillCache(code);
        V res =  this.indexCache.get(code).get(date);
        if (res == null) {
            int cursor = 0;
            res = apply(list.size()-1); /*if date not find take last*/
            while (cursor  < list.size()) {
                if (list.get(cursor).getDay().compareTo(date) < 0) cursor ++;
                else {
                    res = apply(cursor);
                    break;
                }
            }
        }
        return res;
    }

    /**
     * apply generic type
     * @param cursor
     * @return
     */
    protected abstract V apply(int cursor);


    /**
     * return list and filled cache if empty
     * @param code
     * @return
     */
    protected  List<W> getInCache(K code) {
        List<W> list = this.historyCache.get(code);
        if (list == null)
            list = fillCache(code);

        return list;
    }


    /**
     * fill cache
     * @param code
     * @return
     */
    protected abstract List<W> fillCache(K code);


}

package com.mltrading.dao.TimeSeriesDao;

import com.mltrading.models.stock.StockAnalyse;
import com.mltrading.models.stock.StockHistory;

import java.util.List;
import java.util.Map;

/**
 * Created by gmo on 08/03/2017.
 */
public interface TimeSeriesDao {

    public List<StockHistory> extract(final String code);
    public List<StockHistory> extract(final String code, final Map<String,Map<String, Integer>> indexCache ,final Map<String,List<StockHistory>> historyCache );
    public StockHistory extractSpecific(final String code, final String date);
    public StockHistory extractSpecificAfter(final String code, final String date);
    public StockHistory extractSpecificBefore(final String code, final String date);
    public StockHistory extractSpecificOffset(final String code, final String date, int offset);
    public List<StockHistory>  extractLasts(final String code, int count);
    List<StockHistory> extractDateInvert(final String code, final String date, int offset);

    public List<StockAnalyse> getAnalyse(final String code);
    public StockAnalyse getAnalyse(final String code, final String date);
}

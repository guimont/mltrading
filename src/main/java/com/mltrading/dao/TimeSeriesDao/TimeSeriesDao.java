package com.mltrading.dao.TimeSeriesDao;

import com.mltrading.models.stock.StockAnalyse;
import com.mltrading.models.stock.StockHistory;

import java.util.List;
import java.util.Map;

/**
 * Created by gmo on 08/03/2017.
 */
public interface TimeSeriesDao {

    List<StockHistory> extract(final String code);
    List<StockHistory> extract(final String code, final Map<String,Map<String, Integer>> indexCache ,final Map<String,List<StockHistory>> historyCache );
    StockHistory extractSpecific(final String code, final String date);
    StockHistory extractSpecificAfter(final String code, final String date);
    StockHistory extractSpecificBefore(final String code, final String date);
    StockHistory extractSpecificOffset(final String code, final String date, int offset);
    List<StockHistory>  extractLasts(final String code, int count);
    List<StockHistory> extractDateInvert(final String code, final String date, int offset);
    StockHistory extractLastHistory(final String code);

    List<StockAnalyse> extractAnalyse(final String code);
    StockAnalyse extractAnalyseSpecific(final String code, final String date);
    List<StockAnalyse> extractAnalyse(final String code, final Map<String,Map<String, Integer>> indexCache ,final Map<String,List<StockAnalyse>> historyCache );
}

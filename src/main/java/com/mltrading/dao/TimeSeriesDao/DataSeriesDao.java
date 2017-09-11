package com.mltrading.dao.TimeSeriesDao;

import com.mltrading.models.stock.StockDocument;



import java.util.List;
import java.util.Map;

public interface DataSeriesDao {

    List<StockDocument> extract(final String code, final Map<String,Map<String, Integer>> indexCache , final Map<String,List<StockDocument>> historyCache );

}

package com.mltrading.models.parser;


import com.mltrading.models.stock.StockGeneral;

/**
 * Created by gmo on 23/11/2015.
 */
public interface HistoryParser {

    void fetch();

    void fetchSpecific(StockGeneral g);

    void fetchCurrent( int period);

}

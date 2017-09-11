package com.mltrading.models.parser;



/**
 * Created by gmo on 23/11/2015.
 */
public interface HistoryRawMaterialsParser {

    void fetch(String host);

    void fetchCurrent(int period);

}

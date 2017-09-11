package com.mltrading.models.parser;


/**
 * Created by gmo on 23/11/2015.
 */
public interface HistoryIndiceParser {

    void fetch();

    void fetchCurrent(int period);
}



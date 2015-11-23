package com.mltrading.models.parser;

import com.google.inject.AbstractModule;
import com.mltrading.models.parser.impl.HistoryParserYahoo;
import com.mltrading.models.parser.impl.RealTimeParserBoursorama;
import com.mltrading.models.parser.impl.RealTimeParserYahoo;

/**
 * Created by gmo on 20/11/2015.
 */
public class ServiceParser extends AbstractModule {


    @Override
    protected void configure() {
        bind(RealTimeParser.class).to(RealTimeParserYahoo.class);
        bind(HistoryParser.class).to(HistoryParserYahoo.class);
    }


}

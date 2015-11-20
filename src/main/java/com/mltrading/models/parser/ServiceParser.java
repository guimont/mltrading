package com.mltrading.models.parser;

import com.google.inject.AbstractModule;
import com.mltrading.models.parser.impl.RealTimeParserBoursorama;

/**
 * Created by gmo on 20/11/2015.
 */
public class ServiceParser extends AbstractModule {


    @Override
    protected void configure() {
        bind(RealTimeParser.class).to(RealTimeParserBoursorama.class);
    }
}

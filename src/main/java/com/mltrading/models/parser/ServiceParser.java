package com.mltrading.models.parser;

import com.google.inject.AbstractModule;
import com.mltrading.models.parser.impl.*;

/**
 * Created by gmo on 20/11/2015.
 */
public class ServiceParser extends AbstractModule {


    @Override
    protected void configure() {
        bind(RealTimeParser.class).to(RealTimeParserYahoo.class);
        bind(HistoryParser.class).to(HistoryParserYahoo.class);
        bind(HistoryRawMaterialsParser.class).to(HistoryLocalRawMaterials.class);
        bind(ConsensusParser.class).to(ConsensusParserInvestir.class);
        bind(VolatilityParser.class).to(VolatilityGoogle.class);
    }


}

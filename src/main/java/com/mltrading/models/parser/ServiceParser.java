package com.mltrading.models.parser;

import com.google.inject.AbstractModule;
import com.mltrading.models.parser.impl.*;

/**
 * Created by gmo on 20/11/2015.
 */
public class ServiceParser extends AbstractModule {


    @Override
    protected void configure() {
        bind(RealTimeParser.class).to(RealTimeParserBoursorama.class);
        bind(HistoryParser.class).to(HistoryParserGoogle.class);
        bind(HistoryRawMaterialsParser.class).to(HistoryLocalRawMaterials.class);
        bind(ConsensusParser.class).to(ConsensusParserInvestir.class);
        bind(VolatilityParser.class).to(VolatilityGoogle.class);
        bind(HistorySectorParser.class).to(HistorySectorParserGoogle.class);
        bind(HistoryIndiceParser.class).to(HistoryIndiceParserGoogle.class);
        bind(StockParser.class).to(StockParserInvestir.class);
        bind(ArticleParser.class).to(ArticleParserEchos.class);
        bind(ArticlesParser.class).to(ArticlesParserEchos.class);
        bind(ArticleNotation.class).to(ArticleParserNotation.class);
        bind(DiaryParser.class).to(DiaryParserBoursorama.class);
    }


}

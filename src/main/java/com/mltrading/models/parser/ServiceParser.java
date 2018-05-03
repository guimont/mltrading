package com.mltrading.models.parser;

import com.google.inject.AbstractModule;
import com.mltrading.models.parser.impl.*;

import com.mltrading.models.parser.impl.boursorama.DiaryParserBoursorama;
import com.mltrading.models.parser.impl.boursorama.RealTimeParserBoursorama;
import com.mltrading.models.parser.impl.investing.HistoryLocalRawMaterials;
import com.mltrading.models.parser.impl.investing.HistoryVolatilityParserInvesting;
import com.mltrading.models.parser.impl.investir.HistoryIndiceParserInvestir;
import com.mltrading.models.parser.impl.investir.HistoryParserInvestir;
import com.mltrading.models.parser.impl.investir.HistorySectorParserInvestir;

/**
 * Created by gmo on 20/11/2015.
 */
public class ServiceParser extends AbstractModule {


    @Override
    protected void configure() {
        bind(RealTimeParser.class).to(RealTimeParserBoursorama.class);
        bind(HistoryParser.class).to(HistoryParserInvestir.class);
        bind(HistoryRawMaterialsParser.class).to(HistoryLocalRawMaterials.class);
        bind(ConsensusParser.class).to(ConsensusParserInvestir.class);
        bind(VolatilityParser.class).to(HistoryVolatilityParserInvesting.class);
        bind(HistorySectorParser.class).to(HistorySectorParserInvestir.class);
        bind(HistoryIndiceParser.class).to(HistoryIndiceParserInvestir.class);
        bind(StockParser.class).to(StockParserInvestir.class);
        bind(ArticleParser.class).to(ArticleParserEchos.class);
        bind(ArticlesParser.class).to(ArticlesParserEchos.class);
        bind(ArticleNotation.class).to(ArticleParserNotation.class);
        bind(DiaryParser.class).to(DiaryParserBoursorama.class);
    }


}

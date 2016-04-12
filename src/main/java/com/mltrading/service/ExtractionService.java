package com.mltrading.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mltrading.models.parser.*;
import com.mltrading.models.parser.impl.ArticleParserEchos;
import com.mltrading.models.parser.impl.ArticlesParserEchos;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockRawMat;
import com.mltrading.repository.StockRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import javax.inject.Inject;

/**
 * Created by gmo on 04/01/2016.
 */
@Service
public class ExtractionService {

    Injector injector = Guice.createInjector(new ServiceParser());
    RealTimeParser rtPrice = injector.getInstance( RealTimeParser.class );
    HistoryParser histParser = injector.getInstance(HistoryParser.class);
    HistoryRawMaterialsParser rawParser = injector.getInstance(HistoryRawMaterialsParser.class);
    HistoryIndiceParser indiceParser= injector.getInstance(HistoryIndiceParser.class);
    HistorySectorParser sectorParser = injector.getInstance(HistorySectorParser.class);
    VolatilityParser vola = injector.getInstance(VolatilityParser.class);
    StockParser stock = injector.getInstance(StockParser.class);
    ArticlesParser articles =  injector.getInstance(ArticlesParser.class);
    ArticleParser article =  injector.getInstance(ArticleParser.class);


    public void extractStock(StockRepository stockRepository) {
        stock.fetch(stockRepository);
    }

    public void extractFull() {
        histParser.fetch();
        indiceParser.fetch();
        sectorParser.fetch();
        vola.fetch();
        articles.fetch();
        article.fetch();
        Analyse a = new Analyse();
        a.processAll();
    }

    public void extractRawFull(String host) {
        rawParser.fetch(host);
    }

    public void extractSectorFull() {
        sectorParser.fetch();
        Analyse a = new Analyse();
        a.processSectorAll();
    }

    public void extractIndiceFull() {
        indiceParser.fetch();
        Analyse a = new Analyse();
        a.processIndiceAll();
    }

    public void extractVcacFull() {
        vola.fetch();
        Analyse a = new Analyse();
        a.processVcacAll();
    }

    public void extractArticlesFull() {
        articles.fetch();
    }

    public void extractArticleFull() {
        article.fetch();
    }


    public void extractionCurrent(int period) {
        histParser.fetchCurrent(period);
        indiceParser.fetchCurrent(period);
        sectorParser.fetchCurrent(period);
        vola.fetchCurrent(period);
        rawParser.fetchCurrent(period);
        articles.fetchCurrent();
        article.fetchCurrent();
        Analyse a = new Analyse();
        a.processDaily(period);
    }
    //

    public  void extractionSpecific(String code) {
        StockGeneral g = CacheStockGeneral.getIsinCache().get(code);
        histParser.fetchSpecific(g);
        Analyse a = new Analyse();
        a.processAnalysisAll(g.getCode(), Analyse.columnStock);
        articles.fetchSpecific(g);
        article.fetchSpecific(g);
    }


    public int getLastUpdateRef() {
        String l = StockHistory.getLastDateHistory("FR0000045072");
        DateTime timeInsert = new DateTime(l);
        DateTime timeNow = new DateTime(System.currentTimeMillis());

        int diff =
            timeNow.getDayOfMonth() - timeInsert.getDayOfMonth();

        l = StockHistory.getLastDateHistory("FRIN");
        timeInsert = new DateTime(l);
        diff = Math.max(diff, timeNow.getDayOfMonth() - timeInsert.getDayOfMonth());

        l = StockHistory.getLastDateHistory("EFCHI");
        timeInsert = new DateTime(l);
        diff = Math.max(diff, timeNow.getDayOfMonth() - timeInsert.getDayOfMonth());

        l = StockRawMat.getLastDateHistory("PETB");
        timeInsert = new DateTime(l);
        diff = Math.max(diff, timeNow.getDayOfMonth() - timeInsert.getDayOfMonth());


        diff -=1;

        return diff;
        //return 0;
    }


    public void processAT() {
        Analyse a = new Analyse();
        a.processAll();
    }
}

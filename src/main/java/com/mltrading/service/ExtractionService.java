package com.mltrading.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mltrading.models.parser.*;
import com.mltrading.models.stock.cache.CacheStockAnalyse;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockRawMat;
import com.mltrading.models.stock.cache.CacheStockHistory;
import com.mltrading.models.util.CsvFileReader;
import com.mltrading.repository.ArticleRepository;
import com.mltrading.repository.StockRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

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
    ArticleNotation notation =  injector.getInstance(ArticleNotation.class);
    DiaryParser diaryParser =  injector.getInstance(DiaryParser.class);


    public void extractStock(StockRepository stockRepository) {
        stock.fetch(stockRepository);
    }

    /**
     * Extract full data from web
     * complete with ddd import data from file
     * @param articleRepository
     */
    public void extractFull(ArticleRepository articleRepository) {
        histParser.fetch();
        indiceParser.fetch();
        sectorParser.fetch();
        vola.fetch();

        CsvFileReader csvFR = new CsvFileReader();
        csvFR.readData();
    }

    public void extractRawFull(String host) {
        rawParser.fetch(host);
        rawParser.fetchCurrent(30);
    }

    public void extractSectorFull() {
        sectorParser.fetch();
        /*Analyse a = new Analyse();
        a.processSectorAll();*/
    }

    public void extractIndiceFull() {
        indiceParser.fetch();
        /*Analyse a = new Analyse();
        a.processIndiceAll();*/
    }

    public void extractVcacFull() {
        vola.fetch();
        /*Analyse a = new Analyse();
        a.processVcacAll();*/
    }

    public void extractArticlesFull() {
        articles.fetch();
    }

    public void extractArticleFull(ArticleRepository articleRepository) {
        article.fetch(articleRepository);
    }


    public void extractionCurrent(int period) {
        histParser.fetchCurrent(period);
        indiceParser.fetchCurrent(period);
        sectorParser.fetchCurrent(period);
        vola.fetchCurrent(period);
        rawParser.fetchCurrent(period);
        //articles.fetchCurrent();
        //article.fetchCurrent();
        Analyse a = new Analyse();
        a.processDaily(period);

        CacheStockHistory.CacheStockHistoryHolder().clearCache();
        CacheStockAnalyse.CacheStockAnalyseHolder().clearCache();
    }
    //

    public  void extractionSpecific(String code) {
        StockGeneral g = CacheStockGeneral.getIsinCache().get(code);
        histParser.fetchSpecific(g);
        Analyse a = new Analyse();
        a.processAnalysisAll(g.getCode());
        //articles.fetchSpecific(g);
        //article.fetchSpecific(g);
    }

    static int DAYS_BY_MONTH = 31;
    static int DAYS_BY_YEAR = 365;

    private int check_diff( DateTime timeInsert , DateTime timeNow ) {
        int shift = (timeNow.getYear() - timeInsert.getYear()) * DAYS_BY_YEAR;
        return (timeNow.getDayOfMonth()+timeNow.getMonthOfYear()*DAYS_BY_MONTH + shift) - (timeInsert.getDayOfMonth()+timeInsert.getMonthOfYear()*DAYS_BY_MONTH);
    }

    public int getLastUpdateRef() {
        String l = StockHistory.getLastDateHistory("ORA");
        DateTime timeInsert = new DateTime(l);
        DateTime timeNow = new DateTime(System.currentTimeMillis());

        int diff = check_diff(timeInsert,timeNow);


        l = StockHistory.getLastDateHistory("FRIN");
        timeInsert = new DateTime(l);
        diff = Math.max(diff,  check_diff(timeInsert,timeNow));

        l = StockHistory.getLastDateHistory("PX1");
        timeInsert = new DateTime(l);
        diff = Math.max(diff,  check_diff(timeInsert,timeNow));

        l = StockRawMat.getLastDateHistory("PETB");
        timeInsert = new DateTime(l);
        diff = Math.max(diff,  check_diff(timeInsert,timeNow));


        //diff -=1;

        return diff;
        //return 0;
    }


    public void processAT() {
        Analyse a = new Analyse();
        a.processAll();
    }

    public void extractNotationFull(ArticleRepository articleRepository) {
        notation.fetch(articleRepository);
    }
}

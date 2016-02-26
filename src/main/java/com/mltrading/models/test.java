package com.mltrading.models;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mltrading.domain.User;
import com.mltrading.ml.RandomForestStock;
import com.mltrading.ml.Validator;
import com.mltrading.models.parser.*;
import com.mltrading.models.parser.impl.CheckConsistency;
import com.mltrading.models.parser.impl.HistorySectorParserYahoo;
import com.mltrading.models.parser.impl.RealTimeParserBoursorama;
import com.mltrading.models.parser.impl.RealTimeParserYahoo;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockAnalyse;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.service.ExtractionService;

import com.mltrading.service.UserService;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by gmo on 18/06/2015.
 */


public class test {

    private static ExtractionService service = new ExtractionService();

    @Inject
    private static UserService userService;


    public static void main(String[] args) {


        Injector injector = Guice.createInjector(new ServiceParser());
        HistorySectorParser sectorParser = injector.getInstance(HistorySectorParser.class);
        HistoryParser histParser = injector.getInstance(HistoryParser.class);
        ConsensusParser consensus = injector.getInstance(ConsensusParser.class);

        HistoryIndiceParser indiceParser= injector.getInstance(HistoryIndiceParser.class);
        VolatilityParser vola = injector.getInstance(VolatilityParser.class);
        HistoryRawMaterialsParser rawParser = injector.getInstance(HistoryRawMaterialsParser.class);

        //rawParser.fetch();
        //rawParser.fetchCurrent(2);

        //indiceParser.fetchMonthly();

        //histParser.fetchMonthly();

        //service.extractionCurrent();

        //RealTimeParserBoursorama.loaderCache();
        //RealTimeParserYahoo.loaderCache();

        /*StockGeneral g = CacheStockGeneral.getIsinCache().get("BE0003470755");
        histParser.fetchSpecific(g);

        //histParser.fetch();*/
        //CheckConsistency.countfrom();

        /*histParser.fetchMonthly();
        indiceParser.fetchMonthly();
        sectorParser.fetchMonthly();
        vola.fetchMonthly();     */
        /*Analyse a = new Analyse();
        a.processSectorAll();

        /*HistorySectorParserYahoo hspy = new HistorySectorParserYahoo();

        hspy.loaderSpecific("");*/

        /*RealTimeParser rtPrice = injector.getInstance( RealTimeParser.class );
        HistoryParser histParser = injector.getInstance(HistoryParser.class);
        HistoryRawMaterialsParser rawParser = injector.getInstance(HistoryRawMaterialsParser.class);
        HistoryIndiceParser indiceParser= injector.getInstance(HistoryIndiceParser.class);
        HistorySectorParser sectorParser = injector.getInstance(HistorySectorParser.class);

        ConsensusParser consensus = injector.getInstance(ConsensusParser.class);*/
        //VolatilityParser vola = injector.getInstance(VolatilityParser.class);

        /*StockParser stock = injector.getInstance(StockParser.class);
        //stock.fetch();

        User toto =userService.getUserWithAuthorities();

        ScheduleParserGeneral g = new ScheduleParserGeneral();
        //g.start();


        /*InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root");
        influxDB.ping();
        /*String dbName = "aTimeSeries";
        influxDB.createDatabase(dbName);
        influxDB.deleteDatabase(dbName);*/

        //ParserMain.loaderAll();
        //ParserHistory.loader();
        //System.out.println("parsing ok");
        //rtPrice.refreshCache();
        //histParser.fetch();
        //rawParser.fetch();
        //consensus.fetch();
        //sectorParser.fetch();
        //indiceParser.fetch();
        //vola.fetch();



        /*QueryResult res =InfluxDaoConnector.getPoints("SELECT * FROM FR0000131708");

        int len = res.getResults().get(0).getSeries().get(0).getValues().size();
        List<Object> l = res.getResults().get(0).getSeries().get(0).getValues().get(len-50);

        System.out.print(l.get(0).toString());

        String  query= "SELECT mean(value) FROM FR0000131708 where time > '" + l.get(0)+"'";
        QueryResult meanQ =InfluxDaoConnector.getPoints(query);

        BatchPoints bp = InfluxDaoConnector.getBatchPoints();

        Point pt = Point.measurement("FR0000131708").time(new DateTime( res.getResults().get(0).getSeries().get(0).getValues().get(len-1).get(0)).getMillis(), TimeUnit.MILLISECONDS)
                .field("mm50",meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(1))
                .build();
        bp.point(pt);

        InfluxDaoConnector.writePoints(bp);


        System.out.print(meanQ);*/

        /*RealTimeParserYahoo rt = new RealTimeParserYahoo();
        rt.refreshCache();*/




        //StockHistory sh = StockHistory.getStockHistory("FR0000045072", "2015-04-29T22:00:00Z");
        //StockHistory sh = StockHistory.getStockHistoryDayOffset("FR0000045072", "2016-01-28", 10);
        //List<StockHistory> shL = StockHistory.getStockHistoryList("FR0000045072");

        //System.out.println(shL.size());

        //System.out.println(sh.toString());

        //sectorParser.fetch();

        //RandomForestStock rf = new RandomForestStock();
        //rf.processRF();


        //service.extractFull();


       /* Validator v = new Validator();
        v.loadValidator("ORAVD1");*/


        rawParser.fetchCurrent(2);

        /*String l = StockHistory.getLastDateHistory("FR0000045072");
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

        System.out.print(diff);

        /*sectorParser.fetch();
        Analyse a = new Analyse();
        a.processSectorAll();*/


    }

}

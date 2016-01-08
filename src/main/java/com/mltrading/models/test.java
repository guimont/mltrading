package com.mltrading.models;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mltrading.domain.User;
import com.mltrading.ml.RandomForestStock;
import com.mltrading.models.parser.*;
import com.mltrading.models.parser.impl.RealTimeParserBoursorama;
import com.mltrading.models.stock.StockAnalyse;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.service.ExtractionService;

import com.mltrading.service.UserService;

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


        /*Injector injector = Guice.createInjector(new ServiceParser());

        RealTimeParser rtPrice = injector.getInstance( RealTimeParser.class );
        HistoryParser histParser = injector.getInstance(HistoryParser.class);
        HistoryRawMaterialsParser rawParser = injector.getInstance(HistoryRawMaterialsParser.class);
        HistoryIndiceParser indiceParser= injector.getInstance(HistoryIndiceParser.class);
        HistorySectorParser sectorParser = injector.getInstance(HistorySectorParser.class);

        ConsensusParser consensus = injector.getInstance(ConsensusParser.class);
        VolatilityParser vola = injector.getInstance(VolatilityParser.class);

        StockParser stock = injector.getInstance(StockParser.class);
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


        /*Analyse a = new Analyse();
        a.processAll();*/

        /*StockHistory sh = StockHistory.getStockHistory("FR0000045072", "2015-04-29T22:00:00Z");
        List<StockHistory> shL = StockHistory.getStockHistoryList("FR0000045072");

        System.out.println(shL.size());

        System.out.println(sh.toString());*/


        RandomForestStock rf = new RandomForestStock();
        rf.processRF();


        //service.extractFull();


    }

}

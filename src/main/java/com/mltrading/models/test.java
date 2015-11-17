package com.mltrading.models;

import com.mltrading.models.parser.Analyse;
import com.mltrading.models.parser.ParserMain;
import com.mltrading.models.stock.StockAnalyse;
import com.mltrading.models.stock.StockHistory;

/**
 * Created by gmo on 18/06/2015.
 */
public class test {

    public static void main(String[] args) {

        //ScheduleParserGeneral g = new ScheduleParserGeneral();
        //g.start();


        /*InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root");
        influxDB.ping();
        /*String dbName = "aTimeSeries";
        influxDB.createDatabase(dbName);
        influxDB.deleteDatabase(dbName);*/

        ParserMain.loaderAll();
        //ParserHistory.loader();
        System.out.println("parsing ok");


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

        StockHistory a = StockHistory.getStockHistory("FR0000045072", "2013-03-03T23:00:00Z");
        System.out.println(a.toString());






    }

}

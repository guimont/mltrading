package com.mltrading.models.parser.impl.investing;



import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.models.parser.HistoryCommon;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.VolatilityParser;
import com.mltrading.models.stock.StockHistory;

import com.mltrading.models.stock.cache.CacheStockHistory;
import org.influxdb.dto.BatchPoints;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

public class HistoryVolatilityParserInvesting extends ParserCommon implements VolatilityParser,HistoryCommon {

    final String refCode = "tr";
    final static String url = "https://fr.investing.com/indices/cac-40-vix-historical-data";
    static String code = "VCAC";
    static String name = "CAC 40 VOLA IDX";

    @Override
    public void fetch() {

    }

    @Override
    public void fetchCurrent(int period) {
        try {
            parser(url, period);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parser(String url,  int range) throws MalformedURLException, InterruptedException {
        String text = loadUrl(new URL(url));

        Document doc = Jsoup.parse(text);
        BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Elements links = doc.select(refCode).select("tr");
        int count = 0;
        for (Element link : links) {

            if (link.hasClass("row"))
                continue;

            if (link.child(0).tag().getName().equals("th"))
                continue;

            if (count++ >= range)
                break;


            StockHistory ind = new StockHistory();
            ind.setCode(code);
            ind.setCodif(code);
            ind.setName(name);

            ind.setDayInvest(link.child(0).text());

            if (CacheStockHistory.CacheStockHistoryHolder().isInStockHistory(code, ind.getDay()))
                continue;


            if (checkCurentDay(ind.getDay()))
                continue;

            ind.setValue(new Double(link.child(1).text().replaceAll(" ", "")
                .replace(",", ".").replace("-","0").replaceAll(String.valueOf((char) 160), "")));


            ind.setHighest(new Double(link.child(2).text().replaceAll(" ", "")
                .replace(",", ".").replace("-","0").replaceAll(String.valueOf((char) 160), "")));


            ind.setLowest(new Double(link.child(3).text().replaceAll(" ", "")
                .replace(",", ".").replace("-","0").replaceAll(String.valueOf((char) 160), "")));

            ind.setOpening(new Double(link.child(4).text().replaceAll(" ", "")
                .replace(",", ".").replace("-","0").replaceAll(String.valueOf((char) 160), "")));

            try {
                ind.setVolume(new Double(0));
            } catch (Exception e) {
                ind.setOpening(new Double(0));
            }
            ind.setConsensusNote(new Double(0));
            //saveHistory(bp, ind);
            System.out.println(ind.toString());


        }
        InfluxDaoConnector.writePoints(bp);

    }

}

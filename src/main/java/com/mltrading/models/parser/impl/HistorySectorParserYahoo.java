package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;
import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.models.parser.HistoryParser;
import com.mltrading.models.parser.HistorySectorParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 23/11/2015.
 */

@Singleton
public class HistorySectorParserYahoo implements HistorySectorParser {
    @Override
    public void fetch() {
        loader();
    }

    static String startUrl="https://fr.finance.yahoo.com/q/hp?s=";
    static String endUrl ="&a=00&b=3&c=2010&g=d&z=66&y=";
    static int PAGINATION = 66;
    static String refCode = "tbody";
    static int MAXPAGE = 1518;


    public void loader() {

        for (StockSector g : CacheStockSector.getSectorCache().values()) {

            for (int numPage = 0; numPage <= 150; numPage += PAGINATION) {
                try {
                    String text;
                    String url = startUrl + g.getCode() + "." + g.getPlace() + endUrl + numPage;

                    text = ParserCommon.loadUrl(new URL(url));

                    Document doc = Jsoup.parse(text);
                    BatchPoints bp = InfluxDaoConnector.getBatchPoints();


                    Elements links = doc.select(refCode);
                    for (Element link : links) {

                        if (link.children().size() > 40) {
                            Elements sublinks = link.children().select("tr");
                            for (Element elt : sublinks) {
                                Elements t = elt.select("td");
                                if (t.size() > 3) {

                                    StockHistory hist = new StockHistory();
                                    hist.setCode(g.getCode());
                                    hist.setDayYahoo(t.get(0).text());
                                    hist.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", ".")));
                                    hist.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", ".")));
                                    hist.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", ".")));
                                    hist.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", ".")));
                                    HistoryParser.saveHistory(bp, hist);
                                    System.out.println(hist.toString());
                                }
                            }
                        }
                    }
                    InfluxDaoConnector.writePoints(bp);


                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ERROR for : " + g.getCode());
                }
            }

        }
    }
}

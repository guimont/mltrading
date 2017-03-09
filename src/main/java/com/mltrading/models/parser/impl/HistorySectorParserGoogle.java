package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;
import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.models.parser.HistoryParser;
import com.mltrading.models.parser.HistorySectorParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.cache.CacheStockSector;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockSector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 15/01/2016.
 */

@Singleton
public class HistorySectorParserGoogle implements HistorySectorParser {

    public void fetch() {
        /*fix problem for specific day
        HistorySectorParserYahoo hspy = new HistorySectorParserYahoo();
        hspy.loaderSpecific("");
        USE IMPORT SYSTEM*/
        loader();
    }

    @Override
    public void fetchCurrent(int period) {
        loaderFrom(period);
    }


    static String startUrl="http://www.google.com/finance/historical?q=INDEXEURO%3A";
    static String endUrl ="&startdate=Jan+1%2C+2010&num=200&start=";
    static int PAGINATION = 200;
    static String refCode = "tbody";
    static int MAXPAGE = 1720;


    /**
     * not very nice .. code duplicate and exit not nice
     * @param range
     */
    public void loaderFrom(int range) {

        for (StockSector g : CacheStockSector.getSectorCache().values()) {

            try {
                String text;
                String url = startUrl + g.getCode()+ endUrl + 0;

                text = ParserCommon.loadUrl(new URL(url));

                Document doc = Jsoup.parse(text);
                BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);


                Elements links = doc.select(refCode);
                int count = 0;
                for (Element link : links) {

                    if (link.children().size() > 40) {
                        Elements sublinks = link.children().select("tr");
                        for (Element elt : sublinks) {
                            Elements t = elt.select("td");
                            if (t.size() > 3) {

                                StockHistory stock = new StockHistory();
                                stock.setCode(g.getCode());
                                stock.setCodif(g.getCode());
                                stock.setDayGoogle(t.get(0).text());
                                try {
                                    stock.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", "")));
                                } catch (Exception e) {
                                    stock.setOpening(new Double(0));
                                }

                                try {
                                    stock.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", "")));
                                } catch (Exception e) {
                                    stock.setOpening(new Double(0));
                                }

                                try {
                                    stock.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", "")));
                                } catch (Exception e) {
                                    stock.setOpening(new Double(0));
                                }
                                stock.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", "")));

                                try {
                                    stock.setVolume(new Double(0));
                                } catch (Exception e) {
                                    stock.setOpening(new Double(0));
                                }
                                stock.setConsensusNote(new Double(0));
                                HistoryParser.saveHistory(bp, stock);
                                System.out.println(stock.toString());
                                if (++count >= range)
                                    break;
                            }
                        }
                    }
                }
                InfluxDaoConnector.writePoints(bp);



            }  catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR for : " + g.getName());
            }

        }
    }


    public void loader() {

        for (StockSector g : CacheStockSector.getSectorCache().values()) {

            for (int numPage = 0; numPage <= MAXPAGE; numPage += PAGINATION) {
                try {
                    String text;
                    String url = startUrl + g.getCode()+ endUrl + numPage;

                    text = ParserCommon.loadUrl(new URL(url));

                    Document doc = Jsoup.parse(text);
                    BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);


                    Elements links = doc.select(refCode);
                    for (Element link : links) {

                        if (link.children().size() > 40) {
                            Elements sublinks = link.children().select("tr");
                            for (Element elt : sublinks) {
                                Elements t = elt.select("td");
                                if (t.size() > 3) {

                                    StockHistory stock = new StockHistory();
                                    stock.setCode(g.getCode());
                                    stock.setCodif(g.getCode());
                                    stock.setDayGoogle(t.get(0).text());
                                    stock.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", "")));
                                    stock.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", "")));
                                    stock.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", "")));
                                    stock.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", "")));
                                    stock.setVolume(new Double(0));
                                    stock.setConsensusNote(new Double(0));
                                    HistoryParser.saveHistory(bp, stock);
                                    System.out.println(stock.toString());
                                }
                            }
                        }
                    }
                    InfluxDaoConnector.writePoints(bp);



                }  catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ERROR for : " + g.getName());
                }
            }
        }
    }
}

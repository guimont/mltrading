package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;
import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.models.parser.HistoryIndiceParser;
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
 * Created by gmo on 15/01/2016.
 */

@Singleton

public class HistoryIndiceParserGoogle implements HistoryIndiceParser {

    public void fetch() {
        loader();
    }

    @Override
    public void fetchCurrent(int period) {

        loaderFrom(period);
    }



    //http://www.google.com/finance/historical?q=INDEXEURO%3APX1&ei=f0PfVtHBGcqisgGFiZrQDw
    //http://www.google.com/finance/historical?q=INDEXEURO%3AFRAD&num=200&start=0
    static String startUrl="http://www.google.com/finance/historical?q=";
    static String separator = "%3A";
    static String endUrl ="&startdate=Jan+1%2C+2010&num=200&start=";
    static int PAGINATION = 200;
    static String refCode = "tbody";
    static int MAXPAGE = 1720;


    /**
     * not very nice .. code duplicate and exit not nice
     * @param range
     */
    public void loaderFrom(int range) {

        for (StockIndice g : CacheStockIndice.getIndiceCache().values()) {

            try {
                String text;
                String url = startUrl + g.getPlace() + separator + g.getCodeUrl()+ endUrl + 0;

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
                                stock.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", "")));
                                stock.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", "")));
                                stock.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", "")));
                                stock.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", "")));
                                stock.setVolume(new Double(0));
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

        for (StockIndice g : CacheStockIndice.getIndiceCache().values()) {

            for (int numPage = 0; numPage <= MAXPAGE; numPage += PAGINATION) {
                try {
                    String text;
                    String url = startUrl + g.getPlace() + separator + g.getCodeUrl()+ endUrl + numPage;

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
                                    try {
                                        stock.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", "")));
                                    } catch (Exception e) {
                                        stock.setOpening(new Double(0));
                                    }

                                    try {
                                        stock.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", "")));
                                    } catch (Exception e) {
                                        stock.setHighest(new Double(0));
                                    }
                                    try {
                                        stock.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", "")));
                                    } catch (Exception e) {
                                        stock.setLowest(new Double(0));
                                    }
                                    stock.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", "")));
                                    try {
                                        stock.setVolume(new Double(0));
                                    } catch (Exception e) {
                                        stock.setVolume(new Double(0));
                                    }
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

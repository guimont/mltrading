package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;
import com.mltrading.dao.InfluxDaoConnector;

import com.mltrading.models.parser.HistoryParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.*;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import org.influxdb.dto.BatchPoints;
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
public class HistoryParserGoogle implements HistoryParser {
    @Override
    public void fetch() {
        loader();
    }

    @Override
    public void fetchSpecific(StockGeneral g) {
        loaderSpecific( g);
    }

    @Override
    public void fetchCurrent(int period) {
        loaderFrom(period);
    }



// http://www.google.com/finance/historical?q=EPA%3AAC&startdate=Jan%2029%2C%202011&num=50&start=0
    static String startUrl="http://www.google.com/finance/historical?q=";
    static String midUrl="%3A";
    static String endUrl ="&startdate=Jan+1%2C+2010&num=200&start=";
    static String refCode = "tbody";
    static int MAXPAGE = 1720;
    static int PAGINATION = 200;


    /**
     * not very nice .. code duplicate and exit not nice
     * @param range
     */
    public  void loaderFrom(int range) {

        int numPage;
        boolean retry = false;
        for (StockGeneral g : CacheStockGeneral.getIsinCache().values()) {
            Consensus cnote = ConsensusParserInvestir.fetchStock(g.getCode());

            String url = startUrl + getPlace(g.getPlace()) + midUrl + g.getRealCodif()  + endUrl + 0;

            try {
                String text;
                int loopPage = 0;

                //inifinite loop
                do {
                    text = ParserCommon.loadUrl(new URL(url));
                    if (text == null) retry = true;
                    else retry = false;
                } while (retry);


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
                                loopPage++;
                                StockHistory hist = new StockHistory(g);
                                hist.setDayGoogle(t.get(0).text());

                                hist.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", "").replace("-","0")));
                                hist.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", "").replace("-","0")));
                                hist.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", "").replace("-","0")));
                                hist.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", "").replace("-","0")));
                                hist.setVolume(new Double(t.get(5).text().replaceAll(" ", "").replace(",", "").replace("-", "0")));
                                hist.setConsensusNote(cnote.getNotation(cnote.getIndice(loopPage + 0)).getAvg());

                                HistoryParser.saveHistory(bp, hist);
                                System.out.println(hist.toString());
                                if (++count >= range)
                                    break;
                            }
                        }
                    }
                }
                InfluxDaoConnector.writePoints(bp);


            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR for : " + g.getName());
            }
        }

    }

    private String getPlace(String place) {
        if (place.equalsIgnoreCase("xbru")) return "EBR";

        return "EPA";
    }

    public  void loaderSpecific(StockGeneral g) {

        int numPage;
        boolean retry;
        String startUrl="http://www.google.com/finance/historical?q=FRA%3A";

        Consensus cnote = ConsensusParserInvestir.fetchStock(g.getCode());
        for(numPage =0; numPage <= MAXPAGE ; numPage += PAGINATION) {
            String url = startUrl + g.getCodif() + endUrl + numPage;
            try {
                String text;
                int loopPage = 0;

                //inifinite loop
                do {
                    text = ParserCommon.loadUrl(new URL(url));
                    if (text == null) retry = true;
                    else retry = false;
                } while (retry);


                Document doc = Jsoup.parse(text);
                BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);

                Elements links = doc.select(refCode);

                for (Element link : links) {

                    if (link.children().size() > 40) {
                        Elements sublinks = link.children().select("tr");
                        for (Element elt : sublinks) {
                            Elements t = elt.select("td");
                            if (t.size() > 3) {
                                loopPage ++;
                                StockHistory hist = new StockHistory(g);
                                hist.setDayGoogle(t.get(0).text());

                                hist.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", "").replace("-","0")));
                                hist.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", "").replace("-","0")));
                                hist.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", "").replace("-","0")));
                                hist.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", "").replace("-","0")));
                                hist.setVolume(new Double(t.get(5).text().replaceAll(" ", "").replace(",", "").replace("-","0")));
                                hist.setConsensusNote(cnote.getNotation(cnote.getIndice(loopPage + numPage)).getAvg());

                                HistoryParser.saveHistory(bp, hist); //dont save no trading day
                                System.out.println(hist.toString());
                            }
                        }
                    }
                }
                InfluxDaoConnector.writePoints(bp);


            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR for : " + g.getName());
            }
        }


    }




    public  void loader() {

        int numPage;
        boolean retry = false;
        for (StockGeneral g: CacheStockGeneral.getIsinCache().values()) {
            Consensus cnote = ConsensusParserInvestir.fetchStock(g.getCode());
            for(numPage =0; numPage <= MAXPAGE ; numPage += PAGINATION) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String url = startUrl + getPlace(g.getPlace()) + midUrl + g.getRealCodif()  + endUrl + numPage;

                try {
                    String text;
                    int loopPage = 0;

                    //inifinite loop
                    do {
                        text = ParserCommon.loadUrl(new URL(url));
                        if (text == null) retry = true;
                        else retry = false;
                    } while (retry);


                    if (g.getRealCodif().equalsIgnoreCase("SGO"))
                        System.out.println("break point");

                    Document doc = Jsoup.parse(text);
                    BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);

                    Elements links = doc.select(refCode);
                    for (Element link : links) {

                        if (link.children().size() > 40) {
                            Elements sublinks = link.children().select("tr");
                            for (Element elt : sublinks) {
                                Elements t = elt.select("td");
                                if (t.size() > 3) {
                                    loopPage ++;
                                    StockHistory hist = new StockHistory(g);
                                    hist.setDayGoogle(t.get(0).text());
                                    try {
                                        hist.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", "")));
                                    } catch (Exception e) {
                                        hist.setOpening(new Double(0));
                                    }
                                    try {
                                        hist.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", "")));
                                    } catch (Exception e) {
                                        hist.setHighest(new Double(0));
                                    }
                                    try {
                                        hist.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", "")));
                                    } catch (Exception e) {
                                        hist.setLowest(new Double(0));
                                    }
                                    //mandatory no catch
                                    hist.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", "")));
                                    try {
                                        hist.setVolume(new Double(t.get(5).text().replaceAll(" ", "").replace(",", "")));
                                    } catch (Exception e) {
                                        hist.setVolume(new Double(0));
                                    }
                                    hist.setConsensusNote(cnote.getNotation(cnote.getIndice(loopPage+numPage)).getAvg());
                                    HistoryParser.saveHistory(bp, hist);
                                    System.out.println(hist.toString());
                                }
                            }
                        }
                    }


                    InfluxDaoConnector.writePoints(bp);


                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ERROR for : " + g.getName());
                }
            }
        }
    }
}

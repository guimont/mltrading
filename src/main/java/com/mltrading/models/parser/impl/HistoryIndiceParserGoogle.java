package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;
import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.models.parser.HistoryIndiceParser;
import com.mltrading.models.parser.HistorySectorParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.CacheStockSector;
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
@Deprecated
public class HistoryIndiceParserGoogle implements HistoryIndiceParser {

    public void fetch() {
        /*fix problem for specific day*/
        //loader();
    }

    @Override
    public void fetchCurrent(int period) {

        //loaderFrom(period);
    }


    //http://www.google.com/finance/historical?q=INDEXEURO%3APX1&ei=f0PfVtHBGcqisgGFiZrQDw
    //http://www.google.com/finance/historical?q=INDEXEURO%3AFRAD&num=200&start=0
    static String startUrl="http://www.google.com/finance/historical?q=INDEXEURO%3A";
    static String endUrl ="&startdate=Dec%204%2C%202013&num=200&start=";
    static int PAGINATION = 200;
    static String refCode = "tbody";
    static int MAXPAGE = 1518;


    /**
     * not very nice .. code duplicate and exit not nice
     * @param range
     */
    /*
    public void loaderFrom(int range) {

        for (StockSector g : CacheStockSector.getSectorCache().values()) {

            try {
                String text;
                String url = startUrl + g.getCode()+ endUrl + 0;

                text = ParserCommon.loadUrl(new URL(url));

                Document doc = Jsoup.parse(text);
                BatchPoints bp = InfluxDaoConnector.getBatchPoints();


                Elements links = doc.select(refCode);
                int count = 0;
                for (Element link : links) {

                    if (link.children().size() > 40) {
                        Elements sublinks = link.children().select("tr");
                        for (Element elt : sublinks) {
                            Elements t = elt.select("td");
                            if (t.size() > 3) {

                                StockSector sect = new StockSector(g.getCode(), g.getName(), g.getPlace());
                                sect.setDayGoogle(t.get(0).text());
                                sect.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", "")));
                                sect.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", "")));
                                sect.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", "")));
                                sect.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", "")));
                                sect.setVolume(new Double(0));
                                HistorySectorParser.saveHistory(bp, sect);
                                System.out.println(sect.toString());
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
                    BatchPoints bp = InfluxDaoConnector.getBatchPoints();


                    Elements links = doc.select(refCode);
                    for (Element link : links) {

                        if (link.children().size() > 40) {
                            Elements sublinks = link.children().select("tr");
                            for (Element elt : sublinks) {
                                Elements t = elt.select("td");
                                if (t.size() > 3) {

                                    StockSector sect = new StockSector(g.getCode(), g.getName(), g.getPlace());
                                    sect.setDayGoogle(t.get(0).text());
                                    sect.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", "")));
                                    sect.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", "")));
                                    sect.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", "")));
                                    sect.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", "")));
                                    sect.setVolume(new Double(0));
                                    HistorySectorParser.saveHistory(bp, sect);
                                    System.out.println(sect.toString());
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
    }*/
}

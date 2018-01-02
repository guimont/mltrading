package com.mltrading.models.parser.impl.google;

import com.google.inject.Singleton;
import com.mltrading.dao.InfluxDaoConnector;

import com.mltrading.models.parser.HistoryCommon;
import com.mltrading.models.parser.HistoryParser;
import com.mltrading.models.parser.HistorySectorParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.cache.CacheStockSector;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockSector;
import org.influxdb.dto.BatchPoints;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gmo on 15/01/2016.
 */

@Singleton
public class HistorySectorParserGoogle extends ParserCommon implements HistorySectorParser,HistoryCommon {

    public void fetch() {
        loader();
    }

    @Override
    public void fetchCurrent(int period) {
        loaderFrom(period);
    }


    static String startUrl="http://finance.google.com/finance/historical?q=INDEXEURO%3A";
    static String endUrl ="&startdate=Jan+1%2C+2010&num=200&start=";
    static int PAGINATION = 200;
    static String refCode = "tbody";
    static int MAX_PAGE = 1720;


    /**
     *
     * @param range
     */
    public void loaderFrom(int range) {

        for (StockSector g : CacheStockSector.getSectorCache().values()) {

            try {
                String url = startUrl + g.getCode()+ endUrl + 0;
                parser(url,g,range);

            }  catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR for : " + g.getName());
            }

        }
    }


    /**
     *
     */
    public void loader() {

        for (StockSector g : CacheStockSector.getSectorCache().values()) {

            for (int numPage = 0; numPage <= MAX_PAGE; numPage += PAGINATION) {
                try {
                    String url = startUrl + g.getCode()+ endUrl + numPage;
                    parser(url, g, NO_RANGE);
                }  catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ERROR for : " + g.getName());
                }
            }
        }
    }


    private void parser(String url, StockSector g, int range) throws MalformedURLException {
        String text = loadUrl(new URL(url));

        Document doc = Jsoup.parse(text);
        BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
                            stock.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", "").replace("-","0")));
                        } catch (Exception e) {
                            stock.setOpening(new Double(0));
                        }

                        try {
                            stock.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", "").replace("-","0")));
                        } catch (Exception e) {
                            stock.setOpening(new Double(0));
                        }

                        try {
                            stock.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", "").replace("-","0")));
                        } catch (Exception e) {
                            stock.setOpening(new Double(0));
                        }
                        stock.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", "").replace("-","0")));

                        try {
                            stock.setVolume(new Double(0));
                        } catch (Exception e) {
                            stock.setOpening(new Double(0));
                        }
                        stock.setConsensusNote(new Double(0));
                        saveHistory(bp, stock);
                        System.out.println(stock.toString());
                        if (++count >= range)
                            break;
                    }
                }
            }
        }
        InfluxDaoConnector.writePoints(bp);

    }
}

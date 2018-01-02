package com.mltrading.models.parser.impl.google;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.models.parser.HistoryCommon;
import com.mltrading.models.parser.HistoryParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.VolatilityParser;

import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;
import org.influxdb.dto.BatchPoints;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 03/12/2015.
 */
public class VolatilityGoogle extends ParserCommon implements VolatilityParser,HistoryCommon {

    //INDEXCBOE:VIX
    //INDEXEURO:VFTSE
    //VXN nasdaq
    //INDEXCBOE:VXFXI china

    static String vol = "http://finance.google.com/finance/historical?q=INDEXEURO%3AVCAC&startdate=Jan+1%2C+2010&num=200&start=";
    static int PAGINATION = 200;
    static String refCode = "tbody";
    static int MAXPAGE = 1720;
    static String code = "VCAC";
    static String name = "CAC 40 VOLA IDX";

    @Override
    public void fetch() {
        loader();
    }

    @Override
    public void fetchCurrent(int period) {
        loaderFrom(period);
    }



    /**
     *
     * @param range
     */
    public void loaderFrom(int range) {

        parser(range);

        /*try {
            String text;
            String url = vol + 0;

            text = loadUrl(new URL(url));

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

                            StockHistory ind = new StockHistory();
                            ind.setCode(code);
                            ind.setCodif(code);
                            ind.setName(name);
                            ind.setDayGoogle(t.get(0).text());
                            ind.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", ".")));
                            ind.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", ".")));
                            ind.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", ".")));
                            ind.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", ".")));
                            ind.setVolume(new Double(0));
                            saveHistory(bp, ind);
                            System.out.println(ind.toString());
                            if (count++ >= range)
                                break;
                        }
                    }
                }
            }
            InfluxDaoConnector.writePoints(bp);


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR for : " + code);
        }*/
    }


    public void loader() {

        parser(NO_RANGE);
    }

    private void parser(int range) {

        int count = 0;
        for (int numPage = 0; numPage <= MAXPAGE; numPage += PAGINATION) {
            try {
                String text;
                String url = vol + numPage;

                text = loadUrl(new URL(url));

                Document doc = Jsoup.parse(text);
                BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);

                Elements links = doc.select(refCode);
                for (Element link : links) {

                    if (link.children().size() > 40) {
                        Elements sublinks = link.children().select("tr");
                        for (Element elt : sublinks) {
                            Elements t = elt.select("td");
                            if (t.size() > 3) {

                                StockHistory ind = new StockHistory();
                                ind.setCode(code);
                                ind.setCodif(code);
                                ind.setName(name);
                                ind.setDayGoogle(t.get(0).text());
                                try {
                                    ind.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", ".")));
                                } catch (Exception e) {
                                    ind.setOpening(new Double(0));
                                }

                                try {
                                    ind.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", ".")));
                                } catch (Exception e) {
                                    ind.setHighest(new Double(0));
                                }

                                try {
                                    ind.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", ".")));
                                } catch (Exception e) {
                                    ind.setLowest(new Double(0));
                                }
                                ind.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", ".")));
                                ind.setVolume(new Double(0));
                                saveHistory(bp, ind);
                                System.out.println(ind.toString());
                                if (count++ >= range)
                                    break;
                            }
                        }
                    }
                }
                InfluxDaoConnector.writePoints(bp);


            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR for : " + code);
            }
        }

    }
}

package com.mltrading.models.parser.impl;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.models.parser.HistoryIndiceParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.VolatilityParser;

import com.mltrading.models.stock.StockIndice;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 03/12/2015.
 */
public class VolatilityGoogle implements VolatilityParser {

    static String vol = "http://www.google.com/finance/historical?cid=6404916&startdate=Dec%204%2C%202013&enddate=Dec%203%2C%202015&num=200&start=";
    static int PAGINATION = 200;
    static String refCode = "tbody";
    static int MAXPAGE = 1518;
    static String code = "VCAC";
    static String name = "CAC 40 VOLA IDX";

    @Override
    public void fetch() {
        loader();
    }

    public void loader() {

        for (int numPage = 0; numPage <= MAXPAGE; numPage += PAGINATION) {
            try {
                String text;
                String url = vol + numPage;

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

                                StockIndice ind = new StockIndice(code, name);
                                ind.setDayGoogle(t.get(0).text());
                                ind.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", ".")));
                                ind.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", ".")));
                                ind.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", ".")));
                                ind.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", ".")));
                                ind.setVolume(new Double(0));
                                HistoryIndiceParser.saveHistory(bp, ind);
                                System.out.println(ind.toString());
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

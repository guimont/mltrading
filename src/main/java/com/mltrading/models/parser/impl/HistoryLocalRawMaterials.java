package com.mltrading.models.parser.impl;

import com.mltrading.dao.InfluxDaoConnector;

import com.mltrading.models.parser.HistoryParser;
import com.mltrading.models.parser.HistoryRawMaterialsParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.cache.CacheRawMaterial;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockRawMat;
import org.influxdb.dto.BatchPoints;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gmo on 02/12/2015.
 */
public class HistoryLocalRawMaterials implements HistoryRawMaterialsParser {

    final static String localUrl = "http://";
    final static String path =   "/raw/";


    @Override
    public void fetch(String host) {
        loader(host);
    }

    @Override
    public void fetchCurrent(int period) {
        loaderCurrent(period);
    }


    final String refCode = "tr";

    public  void loader(String host) {
        for (StockRawMat r: CacheRawMaterial.getCache().values()) {
            try {
                String url = localUrl + host + path + r.getName() + ".html";
                System.out.println("url: " + url);
                String text = ParserCommon.loadUrl(new URL(url));

                Document doc = Jsoup.parse(text);
                Elements links = doc.select(refCode);

                BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);
                doc.select("tr").get(7).child(0).hasAttr("data-real-value"); //true


                for (Element t : links) {
                    if (t.child(0).hasAttr("data-real-value")) {
                        StockHistory raw = new StockHistory();
                        raw.setCode(r.getCode());
                        raw.setCodif(r.getCode());
                        raw.setDayInvest(t.children().get(0).text());
                        raw.setValue(new Double(t.children().get(1).text().replaceAll(" ", "").replaceAll(".", "").replace(",", ".").replace("-", "0")));
                        raw.setOpening(new Double(t.children().get(2).text().replaceAll(" ", "").replaceAll(".", "").replace(",", ".").replace("-", "0")));
                        raw.setHighest(new Double(t.children().get(3).text().replaceAll(" ", "").replaceAll(".", "").replace(",", ".").replace("-", "0")));
                        raw.setLowest(new Double(t.children().get(4).text().replaceAll(" ", "").replaceAll(".", "").replace(",", ".").replace("-", "0")));


                        HistoryParser.saveHistory(bp, raw);

                    }
                }
                InfluxDaoConnector.writePoints(bp);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    public  void loaderCurrent(int range) {

        for (StockRawMat r: CacheRawMaterial.getCache().values()) {
            try {

                String text = ParserCommon.loadUrl(new URL(r.getUrl()));
                Document doc = Jsoup.parse(text);
                Elements links = doc.select(refCode);


                BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);


                doc.select("tr").get(7).child(0).hasAttr("data-real-value"); //true

                int count = 0;
                for (Element t : links) {
                    if (t.child(0).hasAttr("data-real-value")) {
                        StockHistory raw = new StockHistory();
                        raw.setCode(r.getCode());
                        raw.setCodif(r.getCode());
                        raw.setDayInvest(t.children().get(0).text());
                        raw.setValue(new Double(t.children().get(1).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                        raw.setOpening(new Double(t.children().get(2).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                        raw.setHighest(new Double(t.children().get(3).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                        raw.setLowest(new Double(t.children().get(4).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));

                        HistoryParser.saveHistory(bp, raw);
                        if (++count >= range)
                            break;
                    }
                }
                InfluxDaoConnector.writePoints(bp);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

}

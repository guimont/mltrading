package com.mltrading.models.parser.impl.investing;

import com.mltrading.dao.InfluxDaoConnector;

import com.mltrading.models.parser.HistoryCommon;
import com.mltrading.models.parser.HistoryParser;
import com.mltrading.models.parser.HistoryRawMaterialsParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.cache.CacheRawMaterial;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockRawMat;
import com.mltrading.models.stock.cache.CacheStockHistory;
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
public class HistoryLocalRawMaterials extends ParserCommon implements HistoryRawMaterialsParser,HistoryCommon {

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


    /**
     * parse local server with raw html page (manually extract)
     * @param host
     */
    public  void loader(String host) {
        for (StockRawMat r: CacheRawMaterial.getCache().values()) {
            try {
                String url = localUrl + host + path + r.getName() + ".html";
                System.out.println("url: " + url);
                parser(r,NO_RANGE,url);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * parse raw investing.com
     * @param range
     */
    public  void loaderCurrent(int range) {

        for (StockRawMat r: CacheRawMaterial.getCache().values()) {
            try {
               parser(r,range,r.getUrl());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void parser(StockRawMat r, int range, String url) throws MalformedURLException, InterruptedException {
        String text = loadUrl(new URL(url));
        Document doc = Jsoup.parse(text);
        Elements links = doc.select(refCode);


        BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);


        doc.select("tr").get(7).child(0).hasAttr("data-real-value"); //true

        int count = 0;
        for (Element t : links) {
            if (t.child(0).hasAttr("data-real-value")) {
                try {

                    if (count++ >= range)
                        break;

                    StockHistory raw = new StockHistory();
                    raw.setCode(r.getCode());
                    raw.setCodif(r.getCode());
                    raw.setDayInvest(t.children().get(0).text());

                    if (CacheStockHistory.CacheStockHistoryHolder().isInStockHistory(r.getCode(), raw.getDay()))
                        continue;

                    if (checkCurentDay(raw.getDay()))
                        continue;

                    raw.setValue(new Double(t.children().get(1).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                    raw.setOpening(new Double(t.children().get(2).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                    raw.setHighest(new Double(t.children().get(3).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                    raw.setLowest(new Double(t.children().get(4).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                    System.out.println(raw.toString());
                    saveHistory(bp, raw);
                }
                catch (Exception e) {
                    System.out.println("url: " + url +" Failed");
                    continue;
                }

                if (++count >= range)
                    break;
            }
        }
        InfluxDaoConnector.writePoints(bp);

    }

}

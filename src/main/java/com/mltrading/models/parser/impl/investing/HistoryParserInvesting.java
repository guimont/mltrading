package com.mltrading.models.parser.impl.investing;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.models.parser.HistoryCommon;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;

import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockHistory;
import org.influxdb.dto.BatchPoints;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

public class HistoryParserInvesting extends ParserCommon implements HistoryCommon {

    final static String localUrl = "http://";
    final static String path =   "/raw/";



    final String refCode = "tr";


    /**
     * parse local server with raw html page (manually extract)
     * @param host
     */
    public  void loader(String host, String  key, String code) {

            try {
                String url = localUrl + host + path + key + ".html";
                System.out.println("url: " + url);

                parser(CacheStockGeneral.getIsinCache().get(code),NO_RANGE,url);

            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    private void parser(StockGeneral r, int range, String url) throws MalformedURLException, InterruptedException {
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

                    StockHistory stock = new StockHistory(r);

                    stock.setDayInvest(t.children().get(0).text());

                    if (CacheStockHistory.CacheStockHistoryHolder().isInStockHistory(r.getCode(), stock.getDay()))
                        continue;

                    if (checkCurentDay(stock.getDay()))
                        continue;

                    stock.setValue(new Double(t.children().get(1).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                    stock.setOpening(new Double(t.children().get(2).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                    stock.setHighest(new Double(t.children().get(3).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                    stock.setLowest(new Double(t.children().get(4).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                    stock.setVolume(new Double(t.children().get(5).text().replaceAll(" ", "").replace("", "")
                        .replace(",", "").replace("-", "0").replace("K", "0").replace("M", "0000")));

                    stock.setConsensusNote(new Double(0));;
                    System.out.println(stock.toString());
                    saveHistory(bp, stock);
                }
                catch (Exception e) {
                    System.out.println("url: " + url +" Failed");
                    continue;
                }

                if (++count >= range)
                    break;
            }
        }

        System.out.println("stock number " + count);
        InfluxDaoConnector.writePoints(bp);

    }

}
